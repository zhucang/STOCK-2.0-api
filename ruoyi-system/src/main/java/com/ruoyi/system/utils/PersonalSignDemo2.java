//package com.ruoyi.system.utils;
//
//import org.web3j.crypto.*;
//import org.web3j.utils.Numeric;
//
//import java.math.BigInteger;
//
//public class PersonalSignDemo2 {
//    public static void main(String[] args) throws Exception {
//        // 生成私钥和地址
//        ECKeyPair keyPair = Keys.createEcKeyPair();
//        String privateKeyHex = keyPair.getPrivateKey().toString(16);
//        String publicAddress = "0x" + Keys.getAddress(keyPair.getPublicKey());
//
//        System.out.println("Private key: " + privateKeyHex);
//        System.out.println("Address: " + publicAddress);
//
//        // 消息
//        String message = "Hello Ethereum!";
//        System.out.println("Message: " + message);
//
//        // 签名
//        byte[] messageBytes = message.getBytes();
//        Sign.SignatureData signature = Sign.signPrefixedMessage(messageBytes, keyPair);
//        String signatureHex = Numeric.toHexString(signature.getR()) +
//                              Numeric.toHexString(signature.getS()).substring(2) +
//                              String.format("%02x", signature.getV()[0]);
//        System.out.println("Signature (hex): " + signatureHex);
//
//        // 恢复地址
//        BigInteger pubKey = Sign.signedPrefixedMessageToKey(messageBytes, signature);
//        String recoveredAddress = "0x" + Keys.getAddress(pubKey);
//        System.out.println("Recovered Address: " + recoveredAddress);
//
//        // 转 hex
//        String signatureHex2 = EthereumSignatureUtils.signatureToHex(signature);
//        System.out.println("SignatureHex2: " + signatureHex2);
//
//        // 恢复地址
//        String recovered = EthereumSignatureUtils.recoverAddress(message, signatureHex2);
//        System.out.println("RecoveredAddress2: " + recovered);
//
//
//        // 验证
//        if (recoveredAddress.equalsIgnoreCase(publicAddress)) {
//            System.out.println("✅ Signature valid, address matches!");
//        } else {
//            System.out.println("❌ Signature invalid, address mismatch!");
//        }
//    }
//}
