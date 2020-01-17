package com.carpool.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.carpool.entity.AddressEntity;
import com.carpool.entity.SchedulingEntity;
import com.carpool.entity.StatisticalData;
import com.carpool.entity.UserEntity;
import com.carpool.service.AddressService;
import com.carpool.service.SchedulingService;
import com.carpool.service.SchedulingService;
import com.carpool.service.UserService;
import com.carpool.utils.DateUtils;
import com.carpool.utils.PageUtils;
import com.carpool.utils.Query;
import com.carpool.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-19 12:53:26
 */
@RestController
@RequestMapping("scheduling")
public class SchedulingController {
    @Autowired
    private SchedulingService schedulingService;
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private UserService userService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("scheduling:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        
        List<SchedulingEntity> schedulingList = schedulingService.queryList(query);
        for(SchedulingEntity se : schedulingList) {
        	AddressEntity fromae = addressService.queryObject(se.getFromAddressId());
        	AddressEntity toae = addressService.queryObject(se.getToAddressId());
        	se.setFromAddressVo(fromae);
        	se.setToAddressVo(toae);
        	se.setArriveTimeS(DateUtils.timeToStr(se.getArriveTime(), "yyyy-MM-dd HH:mm"));
        	
        }
        int total = schedulingService.queryTotal(params);
        PageUtils pageUtil = new PageUtils(schedulingList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("scheduling:info")
    public R info(@PathVariable("id") Long id) {
        SchedulingEntity scheduling = schedulingService.queryObject(id);
        Long arriveTime = scheduling.getArriveTime();
        String arriveTimeS = DateUtils.timeToStr(arriveTime, "yyyy-MM-dd HH:mm");
        scheduling.setArriveTimeS(arriveTimeS);
        return R.ok().put("scheduling", scheduling);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("scheduling:save")
    public R save(@RequestBody SchedulingEntity scheduling) {
        schedulingService.save(scheduling);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("scheduling:update")
    public R update(@RequestBody SchedulingEntity scheduling) {
        schedulingService.update(scheduling);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("scheduling:delete")
    public R delete(@RequestBody Integer[] ids) {
        schedulingService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<SchedulingEntity> list = schedulingService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 统计
     */
    @RequestMapping("/statistical")
    public R statistical () {
    	Map<String, Object> params = new HashMap<String, Object>();
        int userSum = userService.queryTotal(params);
        int actSum = schedulingService.queryTotal(params);
        
        params.put("registerTime", DateUtils.format(new Date()));
        int userSumToday = userService.queryTotal(params);
        
        params.put("date", DateUtils.format(new Date()));
        int actSumToday = schedulingService.queryTotal(params);

        StatisticalData statisticalData = new StatisticalData();
        statisticalData.setUserSum(userSum);
        statisticalData.setActSum(actSum);
        statisticalData.setUserSumToday(userSumToday);
        statisticalData.setActSumToday(actSumToday);
        return R.ok().put("statisticalData", statisticalData);
    }
}
