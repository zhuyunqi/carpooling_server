package com.carpool.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.carpool.entity.ActivityEntity;
import com.carpool.entity.StatisticalData;
import com.carpool.entity.UserEntity;
import com.carpool.service.ActivityService;
import com.carpool.service.UserService;
import com.carpool.utils.DateUtils;
import com.carpool.utils.PageUtils;
import com.carpool.utils.Query;
import com.carpool.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("activity:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        
        List<ActivityEntity> activityList = activityService.queryList(query);
        for(ActivityEntity ae : activityList) {
        	ae.setDateS(DateUtils.timeToStr(ae.getDate(), "yyyy-MM-dd HH:mm"));
        }
        int total = activityService.queryTotal(params);
        PageUtils pageUtil = new PageUtils(activityList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("activity:info")
    public R info(@PathVariable("id") Integer id) {
        ActivityEntity activity = activityService.queryObject(id);
        activity.setDateS(DateUtils.timeToStr(activity.getDate(), "yyyy-MM-dd HH:mm"));
        List<UserEntity> userList = userService.queryListByActId(activity.getId());
        List<UserEntity> userNoCurUserList = new ArrayList<UserEntity>();
        if(null != userList && userList.size() > 0) {
        	for(UserEntity user : userList) {
            	if(user.getId().longValue() != activity.getUserId().longValue()) {
            		userNoCurUserList.add(user);
            	}
            }
        }
        activity.setEnrollList(userNoCurUserList);
        return R.ok().put("activity", activity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("activity:save")
    public R save(@RequestBody ActivityEntity activity) {
        activityService.save(activity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("activity:update")
    public R update(@RequestBody ActivityEntity activity) {
        activityService.update(activity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("activity:delete")
    public R delete(@RequestBody Integer[] ids) {
//    	for(Integer id : ids) {
//    		List<UserEntity> userList = userService.queryListByActId(Long.valueOf(id));
//    		if(null != userList && userList.size() > 0) {
//    			return R.error("已有用户报名，不能删除！");
//    		}
//    	}
        activityService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ActivityEntity> list = activityService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 统计
     */
    @RequestMapping("/statistical")
    public R statistical () {
    	Map<String, Object> params = new HashMap<String, Object>();
        int userSum = userService.queryTotal(params);
        int actSum = activityService.queryTotal(params);
        
        params.put("registerTime", DateUtils.format(new Date()));
        int userSumToday = userService.queryTotal(params);
        
        params.put("date", DateUtils.format(new Date()));
        int actSumToday = activityService.queryTotal(params);

        StatisticalData statisticalData = new StatisticalData();
        statisticalData.setUserSum(userSum);
        statisticalData.setActSum(actSum);
        statisticalData.setUserSumToday(userSumToday);
        statisticalData.setActSumToday(actSumToday);
        return R.ok().put("statisticalData", statisticalData);
    }
}
