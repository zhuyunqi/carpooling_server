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
import com.carpool.entitycarpool.ContractCarpoolVo;
import com.carpool.entitycarpool.SchedulingCarpoolDTO;
import com.carpool.entitycarpool.SchedulingCarpoolVo;
import com.carpool.service.ApiAddressService;
import com.carpool.service.ApiContractService;
import com.carpool.service.ApiSchedulingService;
import com.carpool.service.ApiTopicService;
import com.carpool.service.ApiUserService;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiPageUtils;
import com.carpool.util.IMUtils;
import com.carpool.util.LocationUtils;
import com.carpool.utils.Query;
import com.carpool.utils.StringUtils;
import com.github.pagehelper.PageHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "行程相关")
@RestController
@RequestMapping("/api/scheduling")
public class ApiSchedulingController extends ApiBaseAction {
    @Autowired
    private ApiSchedulingService schedulingService;
    @Autowired
    private ApiContractService contractService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiAddressService addressService;
    @Autowired
    private ApiTopicService topicService;

    /**
     * 新建行程
     */
    @ApiOperation(value = "新建行程")
    @PostMapping("v1/saveScheduling")
    public Object saveSchedulingV1(@LoginUser UserVo loginUser, @RequestBody SchedulingCarpoolVo schedulingCarpoolVo) {
    	schedulingCarpoolVo.setCreateTime(new Date());
    	
    	AddressVo fromAddressVo = schedulingCarpoolVo.getFromAddressVo();
    	AddressVo toAddressVo = schedulingCarpoolVo.getToAddressVo();
    	
    	Long userId = schedulingCarpoolVo.getUserId();
    	UserVo userVo = userService.queryObject(userId);
    	Map<String, Object> addressMap = new HashMap<String, Object>();
    	addressMap.put("userId", userId);
    	addressMap.put("longitude", fromAddressVo.getLongitude());
    	addressMap.put("latitude", fromAddressVo.getLatitude());
    	List<AddressVo> fromAddressList = addressService.queryList(addressMap);
    	if(null == fromAddressList || fromAddressList.size() <=0){
        	fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
        	fromAddressVo.setUserId(userId);
        	
        	fromAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	fromAddressVo.setUserName(userVo.getNickname());
        	addressService.save(fromAddressVo);
    	}else {
    		fromAddressVo.setId(fromAddressList.get(0).getId());
    	}
     	
    	addressMap.put("longitude", toAddressVo.getLongitude());
    	addressMap.put("latitude", toAddressVo.getLatitude());
    	List<AddressVo> toAddressList = addressService.queryList(addressMap);
    	if(null == toAddressList || toAddressList.size() <=0){
        	toAddressVo.setDetailInfo(toAddressVo.getAddress());
        	toAddressVo.setUserId(userId);
        	
        	toAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	toAddressVo.setUserName(userVo.getNickname());
        	addressService.save(toAddressVo);
    	}else {
    		toAddressVo.setId(toAddressList.get(0).getId());
    	}
    	
    	schedulingCarpoolVo.setStatus(0);
    	schedulingCarpoolVo.setFromAddressId(fromAddressVo.getId());
    	schedulingCarpoolVo.setToAddressId(toAddressVo.getId());
    	
    	schedulingCarpoolVo.setImUserId(userVo.getImUserId());
    	schedulingService.save(schedulingCarpoolVo);
    	return toResponsObject(200, "新建行程成功", "");
    }
    
    /**
     * 编辑行程
     */
    @ApiOperation(value = "编辑行程")
    @PostMapping("v1/updateScheduling")
    public Object updateSchedulingV1(@LoginUser UserVo loginUser, @RequestBody SchedulingCarpoolVo schedulingCarpoolVo) {
    	if(schedulingCarpoolVo.getStatus() == 1) {
    		return toResponsObject(500, "fail", "不能重新编辑，该行程已匹配");
    	}
    	
    	AddressVo fromAddressVo = schedulingCarpoolVo.getFromAddressVo();
    	AddressVo toAddressVo = schedulingCarpoolVo.getToAddressVo();
    	
    	Long userId = schedulingCarpoolVo.getUserId();
    	Map<String, Object> addressMap = new HashMap<String, Object>();
    	addressMap.put("userId", userId);
    	addressMap.put("longitude", fromAddressVo.getLongitude());
    	addressMap.put("latitude", fromAddressVo.getLatitude());
    	List<AddressVo> fromAddressList = addressService.queryList(addressMap);
    	if(null == fromAddressList || fromAddressList.size() <=0){
        	fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
        	fromAddressVo.setUserId(userId);
        	
        	fromAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	fromAddressVo.setUserName(userService.queryObject(userId).getNickname());
        	addressService.save(fromAddressVo);
    	}else {
    		fromAddressVo.setId(fromAddressList.get(0).getId());
    		fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
        	fromAddressVo.setUserId(userId);
        	
        	fromAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	fromAddressVo.setUserName(userService.queryObject(userId).getNickname());
        	addressService.update(fromAddressVo);
    	}
     	
    	addressMap.put("longitude", toAddressVo.getLongitude());
    	addressMap.put("latitude", toAddressVo.getLatitude());
    	List<AddressVo> toAddressList = addressService.queryList(addressMap);
    	if(null == toAddressList || toAddressList.size() <=0){
        	toAddressVo.setDetailInfo(toAddressVo.getAddress());
        	toAddressVo.setUserId(userId);
        	
        	toAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	toAddressVo.setUserName(userService.queryObject(userId).getNickname());
        	addressService.save(toAddressVo);
    	}else {
    		toAddressVo.setId(toAddressList.get(0).getId());
    		toAddressVo.setDetailInfo(toAddressVo.getAddress());
        	toAddressVo.setUserId(userId);
        	
        	toAddressVo.setSubject(schedulingCarpoolVo.getSubject());
        	toAddressVo.setUserName(userService.queryObject(userId).getNickname());
        	addressService.update(toAddressVo);
    	}
    	
    	schedulingCarpoolVo.setFromAddressId(fromAddressVo.getId());
    	schedulingCarpoolVo.setToAddressId(toAddressVo.getId());
    	schedulingService.update(schedulingCarpoolVo);
    	return toResponsObject(200, "编辑行程成功", "");
    }
    
    /**
     * 我的行程
     */
    @ApiOperation(value = "我的行程")
    @PostMapping("v1/myScheduling")
    public Object mySchedulingV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	long userId = Long.valueOf(jsonMap.get("userId") +"");
    	logger.info("======= mySchedulingV1 userId ========" +userId);
//    	Long startArriveTime = Long.valueOf(jsonMap.get("startArriveTime") +"");
//    	Long endArriveTime = Long.valueOf(jsonMap.get("endArriveTime") +"");
//    	logger.info("======= mySchedulingV1 startArriveTime ========" +startArriveTime);
//    	logger.info("======= mySchedulingV1 endArriveTime ========" +endArriveTime);
    	Integer page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(null != jsonMap.get("pageSize")) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	logger.info("======= mySchedulingV1 page ========" +page);
    	logger.info("======= mySchedulingV1 pageSize ========" +pageSize);
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", userId);
//    	map.put("startArriveTime", startArriveTime);
//    	map.put("endArriveTime", endArriveTime);
    	map.put("page", page);
    	map.put("limit", pageSize);
    	map.put("order", "desc");
    	map.put("sidx", "id");
    	Query query = new Query(map);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	logger.info("======= mySchedulingV1 query.get(\"offset\") ========" +query.get("offset"));
    	logger.info("======= mySchedulingV1 query.get(\"limit\") ========" +query.get("limit"));
    	List<SchedulingCarpoolVo> aclist = schedulingService.queryList(query);
    	logger.info("======= mySchedulingV1 aclist ========" +aclist.size());
    	for(SchedulingCarpoolVo scv : aclist) {
    		Long userIdi = scv.getUserId();
    		if(null != userIdi) {
    			UserVo userVoi = userService.queryObject(userIdi);
    			scv.setUserVo(userVoi);
    		}
    		
    		scv.setFromAddressVo(addressService.queryObject(scv.getFromAddressId()));
    		scv.setToAddressVo(addressService.queryObject(scv.getToAddressId()));
    	}
    	int total = schedulingService.queryTotal(query);
    	logger.info("======= mySchedulingV1 total ========" +total);
        ApiPageUtils pageUtil = new ApiPageUtils(aclist, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 我的已匹配的行程列表
     */
    @ApiOperation(value = "我的已匹配的行程列表")
    @PostMapping("v1/mySchedulingContract")
    public Object mySchedulingContractV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId")) {
    		return toResponsObject(500, "fail", "参数userId不能为空");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") +"");
    	Long startArriveTime = Long.valueOf(jsonMap.get("startArriveTime") +"");
    	Long endArriveTime = Long.valueOf(jsonMap.get("endArriveTime") +"");
    	logger.info("======= mySchedulingV1 startArriveTime ========" +startArriveTime);
    	logger.info("======= mySchedulingV1 endArriveTime ========" +endArriveTime);
    	Integer page = 1;
    	if(null != jsonMap.get("page")) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(null != jsonMap.get("pageSize")) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("userId", userId);
    	//map.put("status", 1);
    	map.put("startArriveTime", startArriveTime);
    	map.put("endArriveTime", endArriveTime);
    	map.put("page", page);
    	map.put("limit", pageSize);
    	map.put("order", "desc");
    	map.put("sidx", "id");
    	Query query = new Query(map);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<SchedulingCarpoolVo> aclist = schedulingService.queryList(query);
    	
    	UserVo userVo = userService.queryObject(userId);
    	for(SchedulingCarpoolVo scv : aclist) {//已匹配的行程的创建者就是合约的接受者
    		Long userIdi = scv.getUserId();
    		if(null != userIdi) {
    			UserVo userVoi = userService.queryObject(userIdi);
    			scv.setUserVo(userVoi);
    			
    			scv.setIsFriend(IMUtils.isFriend(userVo.getImUserId(), userVoi.getImUserId()));
    		}
    		
    		ContractCarpoolVo ccv = contractService.queryObject(scv.getContractId());
    		if(null != ccv) {
    			long cjconUserId = ccv.getUserId();
    			UserVo cjcontractUserVo = userService.queryObject(cjconUserId);
        		scv.setCjContractUserVo(cjcontractUserVo);
        		
        		logger.info("======== mySchedulingContractV1 userIdi =======" +userIdi);
        		logger.info("======== mySchedulingContractV1 cjconUserId =======" +cjconUserId);
        		logger.info("======== mySchedulingContractV1 qyconUserId =======" +ccv.getQyUserId());
        		logger.info("======== mySchedulingContractV1 userType =======" +ccv.getUserType());
        		if(userIdi.equals(cjconUserId)) {
        			scv.setUserType(ccv.getUserType());
        		}else if(userIdi.equals(ccv.getQyUserId())) {
        			scv.setUserType(ccv.getUserType() == 0 ? 1:0);//从行程过来的合约，已匹配的行程的创建者就是合约的接受者
        		}
        		
//        		Map<String, Object> map2 = new HashMap<String, Object>();
//    	    	map2.put("userId", userId);
//    	    	map2.put("fuserId", cjconUserId);
//    	    	List<UserFriendCarpoolVo> ufcvList = userFriendService.queryList(map2);
//    	    	if(null == ufcvList || ufcvList.size() <= 0) {
//    	    		scv.setIsFriend(false);
//    	    	}else {
//    	    		scv.setIsFriend(true);
//    	    	}
    		}
    		
    		
    		scv.setFromAddressVo(addressService.queryObject(scv.getFromAddressId()));
    		scv.setToAddressVo(addressService.queryObject(scv.getToAddressId()));
    	}
    	int total = schedulingService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(aclist, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 行程匹配
     */
    @ApiOperation(value = "行程匹配")
    @IgnoreAuth
    @PostMapping("v1/scheduling")
    public Object schedulingV1(@LoginUser UserVo loginUser, @RequestBody SchedulingCarpoolDTO schedulingCarpoolDTO) {
    	
    	AddressVo fromAddressVo = schedulingCarpoolDTO.getFromAddressVo();
    	AddressVo toAddressVo = schedulingCarpoolDTO.getToAddressVo();
    	
    	Long userId = schedulingCarpoolDTO.getUserId();
    	UserVo userVoNow = new UserVo();
    	if(null != userId){
    		userVoNow = userService.queryObject(userId);
    		schedulingCarpoolDTO.setImUserId(userVoNow.getImUserId());
    		Map<String, Object> addressMap = new HashMap<String, Object>();
        	addressMap.put("userId", userId);
        	logger.info("============= schedulingV1 userId ===========" +userId);
        	addressMap.put("longitude", fromAddressVo.getLongitude());
        	logger.info("============= schedulingV1 fromAddressVo.getLongitude() ===========" +fromAddressVo.getLongitude());
        	addressMap.put("latitude", fromAddressVo.getLatitude());
        	logger.info("============= schedulingV1 fromAddressVo.getLatitude() ===========" +fromAddressVo.getLatitude());
        	List<AddressVo> fromAddressList = addressService.queryList(addressMap);
        	if(null == fromAddressList || fromAddressList.size() <=0){
            	fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
            	fromAddressVo.setUserId(userId);
            	
            	fromAddressVo.setSubject(schedulingCarpoolDTO.getSubject());
            	fromAddressVo.setUserName(userVoNow.getNickname());
            	addressService.save(fromAddressVo);
        	}
         	
        	addressMap.put("longitude", toAddressVo.getLongitude());
        	addressMap.put("latitude", toAddressVo.getLatitude());
        	List<AddressVo> toAddressList = addressService.queryList(addressMap);
        	if(null == toAddressList || toAddressList.size() <=0){
            	toAddressVo.setDetailInfo(toAddressVo.getAddress());
            	toAddressVo.setUserId(userId);
            	
            	toAddressVo.setSubject(schedulingCarpoolDTO.getSubject());
            	toAddressVo.setUserName(userVoNow.getNickname());
            	addressService.save(toAddressVo);
        	}
    	}
    	
    	
    	
    	
    	
//    	Map<String, Object> param = new HashMap<String, Object>();
//    	param.put("subject", schedulingCarpoolDTO.getStatus());
//    	param.put("fromAddress", fromAddressVo.getAddress());
//    	param.put("toAddress", toAddressVo.getAddress());
//    	param.put("arriveTime", schedulingCarpoolDTO.getArriveTime());
    	
    	Map<String, Object> param1 = new HashMap<String, Object>();
    	param1.put("status", 0);
    	//param1.put("arriveTime", schedulingCarpoolDTO.getArriveTime());
    	logger.info("============= schedulingV1 arriveTime ===========" +schedulingCarpoolDTO.getArriveTime());
    	param1.put("page", schedulingCarpoolDTO.getPage());
    	param1.put("limit", schedulingCarpoolDTO.getPageSize());
    	param1.put("order", "desc");
    	param1.put("sidx", "id");
    	Query query = new Query(param1);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<SchedulingCarpoolVo> sclist = schedulingService.queryList(query);
    	
    	List<Map<String, Object>> peosclist = new ArrayList<Map<String, Object>>();
    	double fromlat1 = fromAddressVo.getLatitude().doubleValue();
		double fromlng1 = fromAddressVo.getLongitude().doubleValue();
		double tolat1 = toAddressVo.getLatitude().doubleValue();
		double tolng1 = toAddressVo.getLongitude().doubleValue();
		
		//String[] schedulingCyclesz = schedulingCarpoolVo.getSchedulingCycle().split(",");
		Map<String, Object> topicMap = new HashMap<String, Object>();
		topicMap.put("title", "行程匹配");
		List<TopicVo> tlist = topicService.queryList(topicMap);
		
		int scdistance = Integer.valueOf(tlist.get(2).getSubtitle()); // 单位是米
		logger.info("======== schedulingV1 scdistance  =======" +scdistance);
		int sctimeF =  Integer.valueOf(tlist.get(1).getSubtitle());  // 时间是分钟
		int sctimeT =  Integer.valueOf(tlist.get(0).getSubtitle());  // 时间是天
		int sctime = sctimeF + sctimeT * 24 * 60;
		logger.info("======== schedulingV1 sctime  =======" +sctime);
    	for(SchedulingCarpoolVo schedulingCarpoolVoi : sclist) {
    		AddressVo faddressVo = addressService.queryObject(schedulingCarpoolVoi.getFromAddressId());
    		AddressVo taddressVo = addressService.queryObject(schedulingCarpoolVoi.getToAddressId());
    		double fromlat2 = faddressVo.getLatitude().doubleValue();
    		double fromlng2 = faddressVo.getLongitude().doubleValue();
    		double tolat2 = taddressVo.getLatitude().doubleValue();
    		double tolng2 = taddressVo.getLongitude().doubleValue();
    		
    		double fromdistance = LocationUtils.getDistance(fromlat1, fromlng1, fromlat2, fromlng2);//两者出发的距离
    		double todistance = LocationUtils.getDistance(tolat1, tolng1, tolat2, tolng2);//两者目的距离
    		logger.info("======== schedulingV1 两者出发的距离  =======" +fromdistance);
    		logger.info("======== schedulingV1 两者目的距离  =======" +todistance);
    		
    		logger.info("======== schedulingV1 被匹配的到达时间  =======" +schedulingCarpoolVoi.getArriveTime());
    		logger.info("======== schedulingV1 求匹配的到达时间  =======" +schedulingCarpoolDTO.getArriveTime());
    		int timDifference = Integer.valueOf(Math.abs(schedulingCarpoolVoi.getArriveTime() - schedulingCarpoolDTO.getArriveTime()) / 60 + "");
    		logger.info("======== schedulingV1 匹配的时间差  =======" +timDifference);
    		
    		String[] weekNumz = null == schedulingCarpoolDTO.getWeekNum() ? null : schedulingCarpoolDTO.getWeekNum().split(",");
    		String[] weekNumzi = null == schedulingCarpoolVoi.getWeekNum() ? null : schedulingCarpoolVoi.getWeekNum().split(",");
    		logger.info("======== schedulingV1 weekNumz  =======" +weekNumz);
    		logger.info("======== schedulingV1 weekNumzi  =======" +weekNumzi);
    		
    		
    		if(fromdistance <= scdistance && todistance <= scdistance && timDifference <= sctime && StringUtils.hasSameElement(weekNumz, weekNumzi)) {
    			Map<String, Object> peosc = new HashMap<String, Object>();
    			UserVo userVo = userService.queryObject(schedulingCarpoolVoi.getUserId());
    			logger.info("============== schedulingV1 userVo.getUserId(): =============" +userVo.getUserId());
    			if(!userVo.getUserId().equals(userId)){//排除掉自己的行程
    				
    				//schedulingCarpoolVoi.getSchedulingCycle().split(",");
    				
    				schedulingCarpoolVoi.setFromAddressVo(faddressVo);
        			schedulingCarpoolVoi.setToAddressVo(taddressVo);
        			peosc.put("schedulingCarpoolVo", schedulingCarpoolVoi);
        			peosc.put("userVo", userVo);
        			
        			if(null == userId) {
        				peosc.put("isFriend", false);
        			}else {
        				peosc.put("isFriend", IMUtils.isFriend(userVoNow.getImUserId(), userVo.getImUserId()));
        			}
        			
        			
//        			Map<String, Object> map1 = new HashMap<String, Object>();
//        	    	map1.put("userId", userId);
//        	    	map1.put("fuserId", userVo.getUserId());
//        	    	List<UserFriendCarpoolVo> ufcvList = userFriendService.queryList(map1);
//        	    	if(null == ufcvList || ufcvList.size() <= 0) {
//        	    		peosc.put("isFriend", false);
//        	    	}else {
//        	    		peosc.put("isFriend", true);
//        	    	}
        	    	
        			peosclist.add(peosc);
    			}
    			
    		}
    	}
    	int total = schedulingService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(peosclist, total, query.getLimit(), query.getPage());
    	//param.put("data", peosclist); 
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 删除行程
     */
    @ApiOperation(value = "删除行程")
    @PostMapping("v1/deleteScheduling")
    public Object deleteSchedulingV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	long id = Long.valueOf(jsonMap.get("id") +"");
    	logger.info("======= mySchedulingV1 id ========" +id);
    	SchedulingCarpoolVo scv = schedulingService.queryObject(id);
    	if(null == scv) {
    		return toResponsObject(500, "fail", "不存在id为：" + id + "的行程！");
    	}
    	if(scv.getStatus() == 1 || scv.getStatus() == 2) {
    		return toResponsObject(500, "fail", "该行程已匹配无法删除！");
    	}
    	schedulingService.delete(id);
    	return toResponsObject(200, "success", "删除成功！");
    }
}