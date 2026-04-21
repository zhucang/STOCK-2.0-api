package com.ruoyi.system.utils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EthereumLoginUtils {

    /**
     * EIP-1271 magic value
     */
    private static final String MAGIC_VALUE = "0x1626ba7e";

    /**
     * ERC-6492 magic bytes（去掉 0x 后共 64 个 hex）
     */
    private static final String ERC6492_MAGIC =
            "6492649264926492649264926492649264926492649264926492649264926492";

    /**
     * 对外唯一入口
     */
    public static boolean verify(String rpcUrl, String address, String message, String signature) {
        try {
            if (isBlank(rpcUrl) || isBlank(address) || isBlank(message) || isBlank(signature)) {
                return false;
            }

            address = normalizeAddress(address);
            signature = normalizeHex(signature);

            // 1) 先判断是不是 ERC-6492 包装签名（你谷歌登录那条就是这种）
            if (isErc6492Signature(signature)) {
                return verify6492(rpcUrl, address, message, signature);
            }

            // 2) 先按普通 EOA 验
            try {
                String recoveredAddress = recoverAddress(message, signature);
                if (address.equalsIgnoreCase(recoveredAddress)) {
                    return true;
                }
            } catch (Exception ignored) {
            }

            // 3) 普通验签失败，再看是不是已部署合约钱包，走 1271
            String code = ethGetCode(rpcUrl, address);
            boolean isContract = !(isBlank(code) || "0x".equalsIgnoreCase(code) || "0x0".equalsIgnoreCase(code));

            if (isContract) {
                return verifyEip1271(rpcUrl, address, message, signature);
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 普通 EOA 恢复地址
     * 兼容 64 / 65 字节签名
     */
    public static String recoverAddress(String message, String signatureHex) throws Exception {
        byte[] sigBytes = Numeric.hexStringToByteArray(signatureHex);

        // 兼容 ERC-2098 64字节紧凑签名
        if (sigBytes.length == 64) {
            sigBytes = convert64To65(sigBytes);
        }

        if (sigBytes.length != 65) {
            throw new IllegalArgumentException("Signature must be 64 or 65 bytes");
        }

        byte[] r = Arrays.copyOfRange(sigBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(sigBytes, 32, 64);
        byte v = sigBytes[64];

        int vInt = v & 0xFF;
        if (vInt < 27) {
            vInt += 27;
        }

        Sign.SignatureData sigData = new Sign.SignatureData((byte) vInt, r, s);

        // 关键：保留你原来可用的 prefixed 验签逻辑
        BigInteger pubKey = Sign.signedPrefixedMessageToKey(
                message.getBytes(StandardCharsets.UTF_8),
                sigData
        );

        return "0x" + Keys.getAddress(pubKey);
    }

    /**
     * EIP-1271 验签
     */
    public static boolean verifyEip1271(String rpcUrl, String walletAddress, String message, String signatureHex) {
        try {
            String address = normalizeAddress(walletAddress);
            byte[] signatureBytes = Numeric.hexStringToByteArray(signatureHex);

            // personal_sign / EIP-191 hash
            byte[] messageHash = hashPersonalMessage(message);

            // 先看地址上有没有代码
            String code = ethGetCode(rpcUrl, address);
            if (isBlank(code) || "0x".equalsIgnoreCase(code) || "0x0".equalsIgnoreCase(code)) {
                return false;
            }

            Function function = new Function(
                    "isValidSignature",
                    Arrays.asList(
                            new Bytes32(messageHash),
                            new DynamicBytes(signatureBytes)
                    ),
                    Collections.singletonList(new TypeReference<Bytes4>() {})
            );

            String data = FunctionEncoder.encode(function);
            String value = ethCall(rpcUrl, address, data);

            if (isBlank(value) || "0x".equalsIgnoreCase(value)) {
                return false;
            }

            List<Type> result = FunctionReturnDecoder.decode(
                    value,
                    function.getOutputParameters()
            );

            if (result == null || result.isEmpty()) {
                return false;
            }

            Bytes4 returned = (Bytes4) result.get(0);
            String returnedHex = Numeric.toHexString(returned.getValue());

            return MAGIC_VALUE.equalsIgnoreCase(returnedHex);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ERC-6492 验签
     *
     * 说明：
     * 1. 6492 不是普通 EOA 签名
     * 2. 如果当前地址已经部署了合约，可解包后走 1271
     * 3. 如果当前地址还没部署（eth_getCode = 0x），这个类无法本地完成验证，返回 false
     */
    public static boolean verify6492(String rpcUrl, String address, String message, String wrappedSignature) {
        try {
            String code = ethGetCode(rpcUrl, address);

            // 先把 6492 包装里的原始签名解出来
            String innerSignature = extractInnerSignatureFrom6492(wrappedSignature);
            if (isBlank(innerSignature)) {
                return false;
            }

            // 已部署合约：解包后走 1271
            boolean isContract = !(isBlank(code) || "0x".equalsIgnoreCase(code) || "0x0".equalsIgnoreCase(code));
            if (isContract) {
                return verifyEip1271(rpcUrl, address, message, innerSignature);
            }

            // 未部署 counterfactual smart account：
            // 这里不能按 EOA 验，也不能直接 1271
            // 需要单独接入 ERC-6492 validator 才能校验
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否 ERC-6492 包装签名
     */
    public static boolean isErc6492Signature(String signatureHex) {
        if (isBlank(signatureHex)) {
            return false;
        }
        String clean = Numeric.cleanHexPrefix(signatureHex).toLowerCase();
        return clean.endsWith(ERC6492_MAGIC);
    }

    /**
     * 从 ERC-6492 包装签名里提取内部 original signature
     *
     * 6492 结构大致是：
     * abi.encode(create2Factory, factoryCalldata, originalERC1271Signature) + magicBytes
     *
     * 这里我们只取第三个参数 originalERC1271Signature
     */
    public static String extractInnerSignatureFrom6492(String wrappedSignature) {
        try {
            String clean = Numeric.cleanHexPrefix(wrappedSignature);

            if (!clean.toLowerCase().endsWith(ERC6492_MAGIC)) {
                return null;
            }

            // 去掉最后 32 bytes magic
            String payload = clean.substring(0, clean.length() - ERC6492_MAGIC.length());

            // 顶层 ABI 编码：
            // word0: address
            // word1: offset(factoryCalldata)
            // word2: offset(originalSignature)
            if (payload.length() < 64 * 3) {
                return null;
            }

            String offsetHex = payload.substring(64 * 2, 64 * 3);
            int sigOffsetBytes = new BigInteger(offsetHex, 16).intValue();

            int sigOffsetHexIndex = sigOffsetBytes * 2;
            if (payload.length() < sigOffsetHexIndex + 64) {
                return null;
            }

            String sigLenHex = payload.substring(sigOffsetHexIndex, sigOffsetHexIndex + 64);
            int sigLenBytes = new BigInteger(sigLenHex, 16).intValue();

            int sigStart = sigOffsetHexIndex + 64;
            int sigEnd = sigStart + sigLenBytes * 2;

            if (payload.length() < sigEnd) {
                return null;
            }

            String innerSig = payload.substring(sigStart, sigEnd);
            return "0x" + innerSig;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * eth_getCode
     */
    public static String ethGetCode(String rpcUrl, String address) {
        String params = "[\"" + address + "\",\"latest\"]";
        return rpc(rpcUrl, "eth_getCode", params);
    }

    /**
     * eth_call
     */
    public static String ethCall(String rpcUrl, String to, String data) {
        String params = "[{\"to\":\"" + to + "\",\"data\":\"" + data + "\"},\"latest\"]";
        return rpc(rpcUrl, "eth_call", params);
    }

    /**
     * 通用 JSON-RPC
     */
    private static String rpc(String rpcUrl, String method, String paramsJson) {
        HttpURLConnection conn = null;
        try {
            String body = "{"
                    + "\"jsonrpc\":\"2.0\","
                    + "\"method\":\"" + method + "\","
                    + "\"params\":" + paramsJson + ","
                    + "\"id\":1"
                    + "}";

            URL url = new URL(rpcUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            conn.getResponseCode() >= 200 && conn.getResponseCode() < 300
                                    ? conn.getInputStream()
                                    : conn.getErrorStream(),
                            StandardCharsets.UTF_8
                    )
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String res = sb.toString();

            int i = res.indexOf("\"result\":\"");
            if (i == -1) {
                return null;
            }

            int start = i + 10;
            int end = res.indexOf("\"", start);
            if (end <= start) {
                return null;
            }

            return res.substring(start, end);
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * personal_sign hash
     */
    private static byte[] hashPersonalMessage(String message) {
        byte[] msg = message.getBytes(StandardCharsets.UTF_8);
        String prefix = "\u0019Ethereum Signed Message:\n" + msg.length;
        byte[] result = new byte[prefix.getBytes(StandardCharsets.UTF_8).length + msg.length];

        byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(prefixBytes, 0, result, 0, prefixBytes.length);
        System.arraycopy(msg, 0, result, prefixBytes.length, msg.length);

        return Hash.sha3(result);
    }

    /**
     * 64字节紧凑签名转 65字节标准签名
     */
    private static byte[] convert64To65(byte[] sig) {
        byte[] r = Arrays.copyOfRange(sig, 0, 32);
        byte[] vs = Arrays.copyOfRange(sig, 32, 64);

        byte v = (byte) (((vs[31] & 0x80) != 0) ? 28 : 27);
        vs[31] &= 0x7F;

        byte[] result = new byte[65];
        System.arraycopy(r, 0, result, 0, 32);
        System.arraycopy(vs, 0, result, 32, 32);
        result[64] = v;

        return result;
    }

    private static String normalizeAddress(String address) {
        if (address == null) {
            return null;
        }
        String a = address.trim();
        if (!a.startsWith("0x") && !a.startsWith("0X")) {
            a = "0x" + a;
        }
        return a.toLowerCase();
    }

    private static String normalizeHex(String hex) {
        if (hex == null) {
            return null;
        }
        String s = hex.trim();
        if (!s.startsWith("0x") && !s.startsWith("0X")) {
            s = "0x" + s;
        }
        return s;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}