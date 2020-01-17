package com.carpool.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carpool.entity.AdEntity;
import com.carpool.entity.UserEntity;
import com.carpool.service.AdService;
import com.carpool.service.UserService;
import com.carpool.utils.PageUtils;
import com.carpool.utils.PushMsgUtils;
import com.carpool.utils.Query;
import com.carpool.utils.R;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-19 09:37:35
 */
@RestController
@RequestMapping("ad")
public class AdController {
    @Autowired
    private AdService adService;
    @Autowired
    private UserService userService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ad:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<AdEntity> adList = adService.queryList(query);
        int total = adService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(adList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ad:info")
    public R info(@PathVariable("id") Integer id) {
        AdEntity ad = adService.queryObject(id);

        return R.ok().put("ad", ad);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ad:save")
    public R save(@RequestBody AdEntity ad) {
    	ad.setCreateTime(new Date());
        adService.save(ad);
        List<UserEntity> uelist = userService.queryList(new HashMap<String, Object>());
        List<String> deviceTokenlist = new ArrayList<String>();
    	if(null != uelist && uelist.size() > 0) {
    		for(UserEntity ue : uelist) {
    			String deviceToken = ue.getDeviceToken();
    			if(null != deviceToken && !"".equals(deviceToken)) {
    				deviceTokenlist.add(deviceToken);
    			}
    		}
    		
    	}
        if(ad.getAdPositionId() == 2) {
        	try {
				PushMsgUtils.pushMsgNotification("你有一条新的推广消息", "default",false, deviceTokenlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(ad.getAdPositionId() == 3) {
        	try {
				PushMsgUtils.pushMsgNotification("你有一条新的系统消息", "default", false, deviceTokenlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ad:update")
    public R update(@RequestBody AdEntity ad) {
        adService.update(ad);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ad:delete")
    public R delete(@RequestBody Integer[] ids) {
        adService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<AdEntity> list = adService.queryList(params);

        return R.ok().put("list", list);
    }
}
