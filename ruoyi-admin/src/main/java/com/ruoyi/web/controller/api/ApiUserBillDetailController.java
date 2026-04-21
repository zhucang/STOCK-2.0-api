package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.service.IUserBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户流水记录Controller
 * 
 * @author ruoyi
 * @date 2023-10-29
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userBillDetail")
public class ApiUserBillDetailController extends BaseController
{
    @Autowired
    private IUserBillDetailService userBillDetailService;

    /**
     * 查询用户流水记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserBillDetail userBillDetail)
    {
        userBillDetail.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        userBillDetail.getParams().put("getLang",0);
        List<UserBillDetail> list = userBillDetailService.selectUserBillDetailList(userBillDetail);
        return getDataTable(list);
    }
}
