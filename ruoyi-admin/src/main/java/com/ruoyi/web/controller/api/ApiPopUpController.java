package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.PopUp;
import com.ruoyi.system.service.IPopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 弹窗信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-21
 */
@RestController
@RequestMapping("/api/popUp")
public class ApiPopUpController extends BaseController
{
    @Autowired
    private IPopUpService popUpService;

    /**
     * 查询弹窗信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PopUp popUp)
    {
        startPage();
        List<PopUp> list = popUpService.selectPopUpList(popUp);
        return getDataTable(list);
    }
}
