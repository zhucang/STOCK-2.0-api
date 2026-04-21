package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TrumpetInfo;
import com.ruoyi.system.service.ITrumpetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 喇叭信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/api/trumpetInfo")
public class ApiTrumpetInfoController extends BaseController
{
    @Autowired
    private ITrumpetInfoService trumpetInfoService;

    /**
     * 查询喇叭信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TrumpetInfo trumpetInfo)
    {
        startPage();
        startOrderBy("id desc");
        List<TrumpetInfo> list = trumpetInfoService.selectTrumpetInfoList(trumpetInfo);
        return getDataTable(list);
    }
}
