package com.carpool.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpool.annotation.IgnoreAuth;
import com.carpool.annotation.LoginUser;
import com.carpool.entity.AddressVo;
import com.carpool.entity.TopicVo;
import com.carpool.entity.UserVo;
import com.carpool.entitycarpool.ActivityCarpoolVo;
import com.carpool.entitycarpool.UserActivityCarpoolVo;
import com.carpool.entitycarpool.UserCollectCarpoolVo;
import com.carpool.service.ApiActivityService;
import com.carpool.service.ApiAddressService;
import com.carpool.service.ApiTopicService;
import com.carpool.service.ApiUserActivityService;
import com.carpool.service.ApiUserCollectService;
import com.carpool.service.ApiUserService;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiPageUtils;
import com.carpool.util.IMUtils;
import com.carpool.utils.Query;
import com.github.pagehelper.PageHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "活动相关")
@RestController
@RequestMapping("/api/activity")
public class ApiActivityController extends ApiBaseAction {
    @Autowired
    private ApiActivityService activityService;
    @Autowired
    private ApiUserActivityService userActivityService;
    @Autowired
    private ApiUserCollectService userCollectService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiAddressService addressService;
    @Autowired
    private ApiTopicService topicService;

    /**
     * 我的活动
     */
    @ApiOperation(value = "我的活动")
    @PostMapping("v1/myActivity")
    public Object myActivityV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	//long userId, String date,
//		@RequestParam(value = "page", defaultValue = "1") Integer page,
//        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId参数不能为空");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	
    	Long startDate = null == jsonMap.get("startDate") ? null : Long.valueOf(jsonMap.get("startDate") + "");
    	Long endDate = null == jsonMap.get("endDate") ? null : Long.valueOf(jsonMap.get("endDate") + "");
    	logger.info("======== activityListV1 startDate ========" +startDate);
    	logger.info("======== activityListV1 endDate ========" +endDate);
    	int page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	int pageSize = 10;
    	if(null != jsonMap.get("pageSize")) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", userId);
    	map.put("startDate", startDate);
    	map.put("endDate", endDate);
    	map.put("page", page);
    	map.put("limit", pageSize);
    	map.put("order", "desc");
    	map.put("sidx", "date");
    	Query query = new Query(map);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<ActivityCarpoolVo> aclist = activityService.queryList(query);
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	for(ActivityCarpoolVo acv : aclist) {
    		boolean isEnrollCurUser = false; 
    		map1.put("activityId", acv.getId());
    		List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(map1);
    		List<UserVo> enrollList = new ArrayList<UserVo>();
    		int flag = 0;
    		for(UserActivityCarpoolVo uacv : uacvList) {
    			UserVo userVo = userService.queryObject(uacv.getUserId());
    			enrollList.add(userVo);
    			flag++;
    			if(flag == 3) {
    				break;
    			}
    		}
    		for(UserActivityCarpoolVo uacv : uacvList) {
    			if(userId.equals(uacv.getUserId())) {
    				isEnrollCurUser = true;
    			}
    		}
    		acv.setEnrollList(enrollList);
    		acv.setUserCount(uacvList.size());
    		acv.setIsEnrollCurUser(isEnrollCurUser);
    		
    		acv.setAddressVo(addressService.queryObject(acv.getAddressId()));
    	}
    	int total = activityService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(aclist, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 我发布的活动列表
     */
    @ApiOperation(value = "我发布的活动列表")
    @IgnoreAuth
    @PostMapping("v1/activityList")
    public Object activityListV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	//long userId, String date,
//		@RequestParam(value = "page", defaultValue = "1") Integer page,
//        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId参数不能为空");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
//    	Long startDate = Long.valueOf(jsonMap.get("startDate") + "");
//    	Long endDate = Long.valueOf(jsonMap.get("endDate") + "");
//    	logger.info("======== activityListV1 startDate ========" +startDate);
//    	logger.info("======== activityListV1 endDate ========" +endDate);
    	int page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	int pageSize = 10;
    	if(null != jsonMap.get("pageSize")) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", userId);
//    	map.put("startDate", startDate);
//    	map.put("endDate", endDate);
    	map.put("page", page);
    	map.put("limit", pageSize);
    	map.put("order", "desc");
    	map.put("sidx", "date");
    	Query query = new Query(map);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<ActivityCarpoolVo> aclist = activityService.queryList(query);
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	for(ActivityCarpoolVo acv : aclist) {
    		boolean isEnrollCurUser = false; 
    		map1.put("activityId", acv.getId());
    		List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(map1);
    		List<UserVo> enrollList = new ArrayList<UserVo>();
    		int flag = 0;
    		for(UserActivityCarpoolVo uacv : uacvList) {
    			UserVo userVo = userService.queryObject(uacv.getUserId());
    			enrollList.add(userVo);
    			flag++;
    			if(flag == 3) {
    				break;
    			}
    		}
    		for(UserActivityCarpoolVo uacv : uacvList) {
    			if(userId.equals(uacv.getUserId())) {
    				isEnrollCurUser = true;
    			}
    		}
    		acv.setEnrollList(enrollList);
    		acv.setUserCount(uacvList.size());
    		acv.setIsEnrollCurUser(isEnrollCurUser);
    		
    		acv.setAddressVo(addressService.queryObject(acv.getAddressId()));
    	}
    	int total = activityService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(aclist, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 我报名的活动列表
     */
    @ApiOperation(value = "我报名的活动列表")
    @IgnoreAuth
    @PostMapping("v1/enrollActivityList")
    public Object enrollActivityListV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	//long userId, String date,
//		@RequestParam(value = "page", defaultValue = "1") Integer page,
//        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId参数不能为空");
    	}
    	Object userId = jsonMap.get("userId");
//    	Long startDate = Long.valueOf(jsonMap.get("startDate") + "");
//    	Long endDate = Long.valueOf(jsonMap.get("endDate") + "");
//    	logger.info("======== activityListV1 startDate ========" +startDate);
//    	logger.info("======== activityListV1 endDate ========" +endDate);
    	int page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	int pageSize = 10;
    	if(null != jsonMap.get("pageSize")) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	List<ActivityCarpoolVo> aclistn = new ArrayList<ActivityCarpoolVo>(); 
    	if(null != userId) {
    		userId = Long.valueOf(jsonMap.get("userId") + "");
    		Map<String, Object> mapuacv = new HashMap<String, Object>();
        	mapuacv.put("userId", userId);
        	mapuacv.put("page", page);
        	mapuacv.put("limit", pageSize);
        	mapuacv.put("sidx", "a.date");
        	mapuacv.put("order", "desc");
        	Query queryuacv = new Query(mapuacv);
        	PageHelper.startPage(queryuacv.getPage(), queryuacv.getLimit());
        	aclistn = activityService.queryEnrollList(queryuacv);
        	if(null != aclistn && aclistn.size() > 0) {
        		for(ActivityCarpoolVo acVo : aclistn) {
            		if(null != acVo) {
            			acVo.setIsEnrollCurUser(true);
                		acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
                		
                		if(Long.valueOf(userId + "").longValue() == acVo.getUserId().longValue()) {
                			acVo.setIsEdit(true);
                		}else {
                			acVo.setIsEdit(false);
                		}
            		}
        		}
        	}
        	int total = activityService.queryEnrollTotal(queryuacv);
        	ApiPageUtils pageUtil = new ApiPageUtils(aclistn, total, queryuacv.getLimit(), queryuacv.getPage());
        	return toResponsObject(200, "success", pageUtil);
    	}
    	ApiPageUtils pageUtil = new ApiPageUtils(aclistn, 0, 10, 1);
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 活动报名
     */
    @ApiOperation(value = "活动报名")
    @PostMapping("v1/enrollActivity")
    public Object enrollActivityV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {//, boolean forceEnroll
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId参数不能为空");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") + "");
    	
    	if(null == jsonMap.get("activityId")) {
    		return toResponsFail(500, "activityId参数不能为空");
    	}
    	long activityId = Long.valueOf(jsonMap.get("activityId") + "");
    	
    	UserActivityCarpoolVo uacv = new UserActivityCarpoolVo();
        uacv.setUserId(userId);
        uacv.setActivityId(activityId);
        userActivityService.save(uacv);
        
        ActivityCarpoolVo acv = activityService.queryObject(activityId);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("activityId", activityId);
		List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(map1);
		List<UserVo> enrollList = new ArrayList<UserVo>();
		int flag = 0;
		for(UserActivityCarpoolVo uacvi : uacvList) {
			UserVo userVo = userService.queryObject(uacvi.getUserId());
			enrollList.add(userVo);
			flag++;
			if(flag == 3) {
				break;
			}
		}
		acv.setEnrollList(enrollList);
		acv.setUserCount(uacvList.size());
        acv.setIsEnrollCurUser(true);
        
    	return toResponsObject(200, "success", acv);
    }
    
    /**
     * 活动人员
     */
    @ApiOperation(value = "活动人员")
    @PostMapping("v1/activityUser")
    public Object activityUserV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	if(null == jsonMap.get("activityId")) {
    		return toResponsFail(500, "activityId不能为null");
    	}
    	long activityId = Long.valueOf(jsonMap.get("activityId") + "");
    	
    	int page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") + "");
    	}
  		int pageSize = 10;
  		if(null != jsonMap.get("pageSize")) {
  			pageSize = Integer.valueOf(jsonMap.get("pageSize") + "");
  		}
    	
    	
    	long userId = loginUser.getUserId();//当前登录用户id
    	UserVo userVoNow = userService.queryObject(userId);
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	map1.put("activityId", activityId);
    	map1.put("page", page);
    	map1.put("limit", pageSize);
    	map1.put("order", "desc");
    	map1.put("sidx", "id");
    	Query query = new Query(map1);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(query);
    	
		List<Map<String, Object>> enrollList = new ArrayList<Map<String, Object>>();
		for(UserActivityCarpoolVo uacv : uacvList) {
			Map<String, Object> peosc = new HashMap<String, Object>();
			
			UserVo userVo = userService.queryObject(uacv.getUserId());
			peosc.put("userVo", userVo);
			
			peosc.put("isFriend", IMUtils.isFriend(userVoNow.getImUserId(), userVo.getImUserId()));
			
//			Map<String, Object> map2 = new HashMap<String, Object>();
//	    	map2.put("userId", userId);
//	    	map2.put("fuserId", userVo.getUserId());
//	    	List<UserFriendCarpoolVo> ufcvList = userFriendService.queryList(map2);
//	    	if(null == ufcvList || ufcvList.size() <= 0) {
//	    		peosc.put("isFriend", false);
//	    	}else {
//	    		peosc.put("isFriend", true);
//	    	}
			
	    	enrollList.add(peosc);
		}
		int total = userActivityService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(enrollList, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 活动详情
     */
    @ApiOperation(value = "活动详情")
    @IgnoreAuth
    @PostMapping("v1/activityDetail")
    public Object activityDetailV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	if(null == jsonMap.get("activityId")) {
    		return toResponsFail(500, "请传参数");
    	}
    	Object userId = jsonMap.get("userId");
    	long activityId = Long.valueOf(jsonMap.get("activityId") + "");
    	
    	
    	ActivityCarpoolVo acv = activityService.queryObject(activityId);
    	if(null == acv) {
    		return toResponsFail(500, "没有id为:"+activityId+"的活动");
    	}
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	map1.put("activityId", activityId);
		List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(map1);
		List<UserVo> enrollList = new ArrayList<UserVo>();
		int flag = 0;
		Boolean isEnrollCurUser = false;
		if(null != userId) {
			for(UserActivityCarpoolVo uacv : uacvList) {
				long userIdi = uacv.getUserId();
				UserVo userVo = userService.queryObject(userIdi);
				if(userIdi == Long.valueOf(userId + "")) {
					isEnrollCurUser = true;
				}
				enrollList.add(userVo);
				flag++;
				if(flag == 3) {
					break;
				}
			}
		}
		
		acv.setAddressVo(addressService.queryObject(acv.getAddressId()));
		
		acv.setEnrollList(enrollList); 
		acv.setUserCount(uacvList.size());
		acv.setIsEnrollCurUser(isEnrollCurUser);//当前用户是否已报名
    	return toResponsObject(200, "success", acv);
    }
    
    /**
     * 能否报名
     */
    @ApiOperation(value = "能否报名")
    @IgnoreAuth
    @PostMapping("v1/canEnroll")
    public Object canEnrollV1(@LoginUser UserVo loginUser, @RequestBody Map<String,Object> jsonMap) {
    	if(null == jsonMap.get("activityId") || null == jsonMap.get("userId")) {
    		return toResponsFail(500, "请传参数");
    	}
    	long activityId = Long.valueOf(jsonMap.get("activityId") + "");
    	long userId = Long.valueOf(jsonMap.get("userId") + "");
    	
    	ActivityCarpoolVo acv = activityService.queryObject(activityId);
    	if(null == acv) {
    		return toResponsFail(500, "没有id为:"+activityId+"的活动");
    	}
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	map1.put("activityId", activityId);
		List<UserActivityCarpoolVo> uacvList = userActivityService.queryList(map1);
		List<UserVo> enrollList = new ArrayList<UserVo>();
		int flag = 0;
		Boolean isEnrollCurUser = false;
		for(UserActivityCarpoolVo uacv : uacvList) {
			long userIdi = uacv.getUserId();
			UserVo userVo = userService.queryObject(userIdi);
			if(userIdi == userId) {
				isEnrollCurUser = true;
			}
			enrollList.add(userVo);
			flag++;
			if(flag == 3) {
				break;
			}
		}
		int enrollState = 0;
		if(isEnrollCurUser) {
			enrollState = 1;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("enrollState", enrollState);
    	return toResponsObject(200, "success", resultMap);
    }
    
    /**
     * 发起活动
     */
    @ApiOperation(value = "发起活动")
    @PostMapping("v1/launchActivity")
    public Object launchActivityV1(@LoginUser UserVo loginUser, @RequestBody ActivityCarpoolVo activityCarpoolVo) {
    	//activityCarpoolVo.setUserId(loginUser.getUserId());
    	AddressVo actAddressVo = activityCarpoolVo.getAddressVo();
    	
    	Long userId = activityCarpoolVo.getUserId();
    	UserVo userVo = userService.queryObject(userId);
    	Map<String, Object> addressMap = new HashMap<String, Object>();
    	addressMap.put("userId", userId);
    	addressMap.put("longitude", actAddressVo.getLongitude());
    	addressMap.put("latitude", actAddressVo.getLatitude());
    	List<AddressVo> addressList = addressService.queryList(addressMap);
    	if(null == addressList || addressList.size() <=0){
    		actAddressVo.setDetailInfo(actAddressVo.getAddress());
    		actAddressVo.setUserId(userId);
    		actAddressVo.setSubject(activityCarpoolVo.getName());
    		actAddressVo.setUserName(userVo.getNickname());
        	addressService.save(actAddressVo);
    	}else {
    		actAddressVo.setId(addressList.get(0).getId());
    	}
    	activityCarpoolVo.setAddressId(actAddressVo.getId());
    	activityCarpoolVo.setImUserId(userVo.getImUserId());
    	activityCarpoolVo.setCollect(0);
    	activityCarpoolVo.setCreateTime(new Date());
    	activityService.save(activityCarpoolVo);
    	
    	UserActivityCarpoolVo uacv = new UserActivityCarpoolVo();
    	uacv.setUserId(userId);
        uacv.setActivityId(activityCarpoolVo.getId());
    	userActivityService.save(uacv);
     	
    	return toResponsObject(200, "发起活动成功", "");
    }
    
    /**
     * 修改活动
     */
    @ApiOperation(value = "修改活动")
    @PostMapping("v1/updateActivity")
    public Object updateActivityV1(@LoginUser UserVo loginUser, @RequestBody ActivityCarpoolVo activityCarpoolVo) {
    	if(null == activityCarpoolVo || null == activityCarpoolVo.getId()) {
    		return toResponsFail(500, "id不能为空");
    	}
    	
    	AddressVo actAddressVo = activityCarpoolVo.getAddressVo();
    	
    	Long userId = activityCarpoolVo.getUserId();
    	UserVo userVo = userService.queryObject(userId);
    	Map<String, Object> addressMap = new HashMap<String, Object>();
    	addressMap.put("userId", userId);
    	addressMap.put("longitude", actAddressVo.getLongitude());
    	addressMap.put("latitude", actAddressVo.getLatitude());
    	List<AddressVo> addressList = addressService.queryList(addressMap);
    	if(null == addressList || addressList.size() <=0){
    		actAddressVo.setDetailInfo(actAddressVo.getAddress());
    		actAddressVo.setUserId(userId);
    		actAddressVo.setSubject(activityCarpoolVo.getName());
    		actAddressVo.setUserName(userVo.getNickname());
        	addressService.save(actAddressVo);
    	}else {
    		actAddressVo.setId(addressList.get(0).getId());
    	}
    	activityCarpoolVo.setAddressId(actAddressVo.getId());
    	activityCarpoolVo.setImUserId(userVo.getImUserId());
    	activityCarpoolVo.setUpdateTime(new Date());
    	activityService.update(activityCarpoolVo);
    	
    	return toResponsObject(200, "修改活动成功", "");
    }
    
    /**
     * 活动点赞
     */
    @ApiOperation(value = "活动点赞")
    @PostMapping("v1/collectActivity")
    public Object collectActivityV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	
    	
    	if(null == jsonMap.get("activityId")) {
    		return toResponsFail(500, "activityId不能为空 ");
    	}
    	long activityId = Long.valueOf(jsonMap.get("activityId") + "");
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", userId);
    	map.put("activityId", activityId);
    	List<UserCollectCarpoolVo> uccvlist = userCollectService.queryList(map);
    	if(null == uccvlist || uccvlist.size() <= 0) {
    		UserCollectCarpoolVo uacv = new UserCollectCarpoolVo();
    		uacv.setUserId(userId);
    		uacv.setActivityId(activityId);
        	uacv.setIsCollect(true);
        	userCollectService.save(uacv);
    	}else {
    		if(uccvlist.get(0).getIsCollect()) {
    			return toResponsFail(500, "该用户已点过赞，不能再点 ");
    		}
    	}
    	
    	logger.info("========== collectActivityV1 activityId: ========" +activityId);
    	ActivityCarpoolVo activityCarpoolVo = activityService.queryObject(activityId);
    	if(null == activityCarpoolVo) {
    		return toResponsFail(500, "没有id为： " + activityId + "的活动");
    	}
    	//userActivityService.queryList(map)
    	int collect = activityCarpoolVo.getCollect() == null ? 0 : activityCarpoolVo.getCollect();
    	activityCarpoolVo.setCollect(collect + 1);
    	activityService.update(activityCarpoolVo);
    	
    	if(null != uccvlist && uccvlist.size() > 0) {
    		UserCollectCarpoolVo uacv = uccvlist.get(0);
        	uacv.setIsCollect(true);
        	userCollectService.update(uacv);
    	}
    	
    	return toResponsObject(200, "success", activityCarpoolVo.getCollect());
    }
    
    /**
     * 热门活动
     */
    @ApiOperation(value = "热门活动")
    @IgnoreAuth
    @PostMapping("v1/hotActivity")
    public Object hotActivityV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	Integer page = 1;
    	if(jsonMap.get("page") != null) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(pageSize != null) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
        
        
	    Map<String, Object> map1 = new HashMap<String, Object>();
	    Map<String, Object> hotCmap = new HashMap<String, Object>();
        hotCmap.put("title", "热门活动");
        TopicVo tv = topicService.queryList(hotCmap).get(0);
        map1.put("hotCollect", tv.getSubtitle());
	    map1.put("page", page);
	    map1.put("limit", pageSize);
	    map1.put("order", "desc");
	    map1.put("sidx", "id");
		Query query = new Query(map1);
		PageHelper.startPage(query.getPage(), query.getLimit());
	    List<ActivityCarpoolVo> hotaclist = activityService.queryList(query);
	    Object userId = jsonMap.get("useId");
	    if(null != hotaclist && hotaclist.size() > 0) {
        	for(ActivityCarpoolVo acVo : hotaclist) {
            	acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
            	
            	if(null != userId) {
            		if(Long.valueOf(userId + "").longValue() == acVo.getUserId().longValue()) {
            			acVo.setIsEdit(true);
            		}else {
            			acVo.setIsEdit(false);
            		}
            	}
            }
        }
	    int total = activityService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(hotaclist, total, query.getLimit(), query.getPage());
	    return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 所有活动
     */
    @ApiOperation(value = "所有活动")
    @IgnoreAuth
    @PostMapping("v1/allActivity")
    public Object allActivityV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	Integer page = 1;
    	if(jsonMap.get("page") != null) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(pageSize != null) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
        
	    Map<String, Object> map1 = new HashMap<String, Object>();
	    map1.put("page", page);
	    map1.put("limit", pageSize);
	    map1.put("order", "desc");
	    map1.put("sidx", "date");
		Query query = new Query(map1);
		PageHelper.startPage(query.getPage(), query.getLimit());
	    List<ActivityCarpoolVo> allaclist = activityService.queryList(query);
	    
	    Object userId = jsonMap.get("userId");
	    if(null != allaclist && allaclist.size() > 0) {
        	for(ActivityCarpoolVo acVo : allaclist) {
            	acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
            	if(null != userId) {
            		if(Long.valueOf(userId + "").longValue() == acVo.getUserId().longValue()) {
            			acVo.setIsEdit(true);
            		}else {
            			acVo.setIsEdit(false);
            		}
            	}
            	
            }
        }
	    int total = activityService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(allaclist, total, query.getLimit(), query.getPage());
	    return toResponsObject(200, "success", pageUtil);
    }
    
    public static void main(String[] args) {
    	
    }
}