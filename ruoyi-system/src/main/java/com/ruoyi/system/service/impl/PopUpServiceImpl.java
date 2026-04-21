package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.PopUp;
import com.ruoyi.system.mapper.PopUpMapper;
import com.ruoyi.system.service.IPopUpService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 弹窗信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-21
 */
@Service
public class PopUpServiceImpl implements IPopUpService 
{
    @Resource
    private PopUpMapper popUpMapper;

    /**
     * 查询弹窗信息
     * 
     * @param id 弹窗信息主键
     * @return 弹窗信息
     */
    @Override
//    @Cacheable(value = CacheableKey.POP_UP + CacheableKey.ENTITY,key = "#id")
    public PopUp selectPopUpById(Long id)
    {
        return popUpMapper.selectPopUpById(id);
    }

    /**
     * 查询弹窗信息列表
     * 
     * @param popUp 弹窗信息
     * @return 弹窗信息
     */
    @Override
    @Cacheable(value = CacheableKey.POP_UP + CacheableKey.LIST,key = "#popUp.cacheableKey()")
    public List<PopUp> selectPopUpList(PopUp popUp)
    {
        return popUpMapper.selectPopUpList(popUp);
    }

    /**
     * 新增弹窗信息
     * 
     * @param popUp 弹窗信息
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.LIST,allEntries = true)
    public int insertPopUp(PopUp popUp)
    {
        List<PopUp> popUps = popUpMapper.selectPopUpList(new PopUp());
        if (popUps.size() > 0){
            throw new RuntimeException("只允许有一条弹窗");
        }
        popUp.setCreateTime(DateUtils.getNowDate());
        return popUpMapper.insertPopUp(popUp);
    }

    /**
     * 修改弹窗信息
     * 
     * @param popUp 弹窗信息
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.ENTITY,key = "#popUp.id"),
            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.LIST,allEntries = true)})
    public int updatePopUp(PopUp popUp)
    {
        return popUpMapper.updatePopUp(popUp);
    }

    /**
     * 批量删除弹窗信息
     * 
     * @param ids 需要删除的弹窗信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.LIST,allEntries = true)})
    public int deletePopUpByIds(Long[] ids)
    {
        PopUp search = new PopUp();
        search.getParams().put("ids", Arrays.asList(ids));
        List<PopUp> popUps = popUpMapper.selectPopUpList(search);
        //日志记录弹窗信息
        HttpUtils.getRequestLogParams().put("JSONArray:popUps", JSONObject.toJSONString(popUps));
        return popUpMapper.deletePopUpByIds(ids);
    }

    /**
     * 删除弹窗信息信息
     * 
     * @param id 弹窗信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.POP_UP + CacheableKey.LIST,allEntries = true)})
    public int deletePopUpById(Long id)
    {
        return popUpMapper.deletePopUpById(id);
    }
}
