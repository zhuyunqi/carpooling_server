package com.carpool.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpool.annotation.LoginUser;
import com.carpool.entity.AddressVo;
import com.carpool.entity.UserVo;
import com.carpool.entitycarpool.ContractCarpoolVo;
import com.carpool.entitycarpool.ContractWarnCarpoolVo;
import com.carpool.entitycarpool.SchedulingCarpoolVo;
import com.carpool.service.ApiAddressService;
import com.carpool.service.ApiContractService;
import com.carpool.service.ApiContractWarnService;
import com.carpool.service.ApiSchedulingService;
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
@Api(tags = "合约相关")
@RestController
@RequestMapping("/api/contract")
public class ApiContractController extends ApiBaseAction {
	private Logger log = Logger.getLogger(ApiContractController.class);
			
    @Autowired
    private ApiContractService contractService;
    @Autowired
    private ApiContractWarnService contractWarnService;
    @Autowired
    private ApiSchedulingService schedulingService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiAddressService addressService;

    /**
     * 发起合约
     */
    @ApiOperation(value = "发起合约")
    @PostMapping("v1/launchContract")
    public Object saveContractV1(@LoginUser UserVo loginUser, @RequestBody ContractCarpoolVo contractCarpoolVo) {
    	Long schedulingId = contractCarpoolVo.getSchedulingId();
    	log.info("========== saveContractV1 schedulingId ==============" +schedulingId);
    	if(0 == schedulingId.longValue() || -1 == schedulingId.longValue()) {
    		schedulingId = null;
    	}
    	if(schedulingId != null) {
    		SchedulingCarpoolVo scv = schedulingService.queryObject(schedulingId);
    		if(scv.getStatus() == 1) {
    			return toResponsFail(405, "对不起，手慢了，该行程已匹配过");
    		}
    		scv.setStatus(1);
    		schedulingService.update(scv);
    	}
    	
    	AddressVo fromAddressVo = contractCarpoolVo.getFromAddressVo();
    	AddressVo toAddressVo = contractCarpoolVo.getToAddressVo();
    	
    	Map<String, Object> addressMap = new HashMap<String, Object>();
    	Long userId = contractCarpoolVo.getUserId();
    	UserVo userVo = userService.queryObject(userId);
    	addressMap.put("userId", userId);
    	addressMap.put("longitude", fromAddressVo.getLongitude());
    	addressMap.put("latitude", fromAddressVo.getLatitude());
    	List<AddressVo> fromAddressList = addressService.queryList(addressMap);
    	if(null == fromAddressList || fromAddressList.size() <=0){
	    	fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
	    	fromAddressVo.setUserId(userId);
	    	
	    	fromAddressVo.setSubject(contractCarpoolVo.getSubject());
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
	    	
	    	toAddressVo.setSubject(contractCarpoolVo.getSubject());
	    	toAddressVo.setUserName(userVo.getNickname());
	    	addressService.save(toAddressVo);
    	}else {
    		toAddressVo.setId(toAddressList.get(0).getId());
    	}
    	
    	contractCarpoolVo.setFromAddressId(fromAddressVo.getId());
    	contractCarpoolVo.setToAddressId(toAddressVo.getId());
    	contractCarpoolVo.setStatus(0);
    	contractCarpoolVo.setImUserId(userVo.getImUserId());
    	contractService.save(contractCarpoolVo);
    	log.info("========== saveContractV1 id =========" +contractCarpoolVo.getId());
    	Map<String, Object> ccvIdM = new HashMap<String, Object>();
    	ccvIdM.put("contractId", contractCarpoolVo.getId());
    	return toResponsObject(200, "success", ccvIdM);
    }
    
    /**
     * 我的合约
     */
    @ApiOperation(value = "我的合约")
    @PostMapping("v1/getContractList")
    public Object getContractListV1(@LoginUser UserVo loginUser,@RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId 不能为空 ");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") +"").longValue();
    	
    	Integer type = null;
    	if(jsonMap.get("type") != null) {
    		type = Integer.valueOf(jsonMap.get("type") +"");
    	}
    	int page = 1;
    	if(jsonMap.get("page") != null) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	int pageSize = 10;
    	if(jsonMap.get("pageSize") != null) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	
    	log.info("======== getContractList userId ======" +userId);
    	log.info("======== getContractList type ======" +type);
    	//type 0:短期 1:长期 2:正常进行 3:历史合约
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(null != type) {
    		if(type == 0 || type == 1) {
        		map.put("contractType", type);
        		map.put("status", 1);
        	}else if(type == 2) {
        		map.put("status", 1);//接受合约即为该合约生效，开始进行
        	}else if(type == 3) {
        		map.put("status2", 2);//合约取消，合约结束即为历史合约
        		map.put("status3", 3);
        	}
    	}
    	
    	
    	map.put("userId", userId);
    	map.put("page", page);
    	map.put("limit", pageSize);
    	map.put("order", "desc");
    	map.put("sidx", "id");
    	Query query = new Query(map);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<ContractCarpoolVo> cclist = contractService.queryListByUserIdORqyUserId(query);
    	for(ContractCarpoolVo ccv : cclist) {
    		Long cjuserId = ccv.getUserId();
    		UserVo cjuserVo = userService.queryObject(cjuserId);
    		ccv.setCjUserVo(cjuserVo);
    		
    		Long qyuserId = ccv.getQyUserId();
    		if(null != qyuserId) {
    			UserVo qyuserVo = userService.queryObject(qyuserId);
    			ccv.setQyUserVo(qyuserVo);
    			
    			ccv.setIsFriend(IMUtils.isFriend(ccv.getImUserId(), qyuserVo.getImUserId()));
    			
//    			Map<String, Object> map2 = new HashMap<String, Object>();
//    	    	map2.put("userId", cjuserId);
//    	    	map2.put("fuserId", qyuserId);
//    	    	List<UserFriendCarpoolVo> ufcvList = userFriendService.queryList(map2);
//    	    	if(null == ufcvList || ufcvList.size() <= 0) {
//    	    		ccv.setIsFriend(false);
//    	    	}else {
//    	    		ccv.setIsFriend(true);
//    	    	}
    		}
    		
    		;
    		ccv.setFromAddressVo(addressService.queryObject(ccv.getFromAddressId()));
    		
    		ccv.setToAddressVo(addressService.queryObject(ccv.getToAddressId()));
    	}
    	
    	int total = contractService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(cclist, total, query.getLimit(), query.getPage());
        
    	return toResponsObject(200, "success", pageUtil);
    }
    
    /**
     * 合约详情
     */
    @ApiOperation(value = "合约详情")
    @PostMapping("v1/contractDetail")
    public Object contractDetailV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId")) {
    		return toResponsFail(500, "userId 或 contractId 不能为空 ");
    	}
    	long contractId = Long.valueOf(jsonMap.get("contractId") + "").longValue();
    	long userId = loginUser.getUserId().longValue();
    	ContractCarpoolVo ccv = contractService.queryObject(contractId);
    	if(null == ccv) {
    		return toResponsObject(500, "没有该合约", "");
    	}
    	Long cjuserId = ccv.getUserId();
		UserVo cjuserVo = userService.queryObject(cjuserId);
		ccv.setCjUserVo(cjuserVo);
		
		Long qyuserId = ccv.getQyUserId();
		if(null != qyuserId) {
			UserVo qyuserVo = userService.queryObject(qyuserId);
			ccv.setQyUserVo(qyuserVo);
		}
		
		if(null != cjuserId && userId == cjuserId.longValue()){
			ccv.setCurUserType(ccv.getUserType());
		}else if(null != qyuserId && userId == qyuserId.longValue()){
			ccv.setCurUserType(ccv.getUserType() == 0 ? 1 : 0);
		}
		
		ccv.setFromAddressVo(addressService.queryObject(ccv.getFromAddressId()));
		ccv.setToAddressVo(addressService.queryObject(ccv.getToAddressId()));
		
		Map<String, Object> mapWarn = new HashMap<String, Object>();
		mapWarn.put("contractId", ccv.getId());
		log.info("========= contractId ccv.getId() =========" + ccv.getId());
		mapWarn.put("userId", userId);
		log.info("========= contractId userId =========" + userId);
		List<ContractWarnCarpoolVo> contractWarnList = contractWarnService.queryList(mapWarn);
		if(null != contractWarnList && contractWarnList.size() > 0) {
			ccv.setNoticeTag(contractWarnList.get(0).getNoticeTag());
		}
		
		
    	return toResponsObject(200, "success", ccv);
    }
    
    /**
     * 评价合约
     */
    @ApiOperation(value = "评价合约")
    @PostMapping("v1/markContract")
    public Object markContractV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId") || null == jsonMap.get("markValue")) {
    		return toResponsFail(500, "userId 或 contractId 或 markValue不能为空 ");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	Long contractId = Long.valueOf(jsonMap.get("contractId") + "");
    	
    	int markValue = Integer.valueOf(jsonMap.get("markValue") + "");
    	String remark =  jsonMap.get("remark") + "";
    	ContractCarpoolVo ccv = contractService.queryObject(contractId);
    	if(null == ccv) {
    		return toResponsObject(500, "没有该合约", "");
    	}
    	if(userId.equals(ccv.getUserId())) {
    		ccv.setMarkValue(markValue);
    		ccv.setEvaluateRemark(remark);
    	}else if(userId.equals(ccv.getQyUserId())) {
    		ccv.setQyMarkValue(markValue);
    		ccv.setQyEvaluateRemark(remark);
    	}else {
    		return toResponsObject(500, "fail", "不是合约参与人，无权评价");
    	}
    	
    	contractService.update(ccv);
    	return toResponsObject(200, "success", "评价成功");
    }
    
    /**
     * 接受合约
     */
    @ApiOperation(value = "接受合约")
    @PostMapping("v1/acceptContract")
    public Object acceptContractV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId")) {
    		return toResponsFail(500, "userId 或 contractId 不能为空 ");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") + "");
    	UserVo userVo = userService.queryObject(userId);
    	long contractId = Long.valueOf(jsonMap.get("contractId") + "");
    	
    	ContractCarpoolVo contractCarpoolVo = contractService.queryObject(contractId);
    	if(null == contractCarpoolVo) {
    		return toResponsObject(500, "fail", "没有id为："+contractId+"的合约");
    	}
    	if(contractCarpoolVo.getStatus() == 0) {
    		contractCarpoolVo.setQyUserId(userId);
    		contractCarpoolVo.setTargetIMUserId(userVo.getImUserId());
    		contractCarpoolVo.setStatus(1);
        	
        	AddressVo fromAddressVo = addressService.queryObject(contractCarpoolVo.getFromAddressId());
        	AddressVo toAddressVo = addressService.queryObject(contractCarpoolVo.getToAddressId());
        	
        	Map<String, Object> addressMap = new HashMap<String, Object>();
        	addressMap.put("userId", userId);
        	addressMap.put("longitude", fromAddressVo.getLongitude());
        	addressMap.put("latitude", fromAddressVo.getLatitude());
        	List<AddressVo> fromAddressList = addressService.queryList(addressMap);
        	if(null == fromAddressList || fromAddressList.size() <=0){
            	fromAddressVo.setDetailInfo(fromAddressVo.getAddress());
            	fromAddressVo.setUserId(userId);
            	
            	fromAddressVo.setSubject(contractCarpoolVo.getSubject());
            	fromAddressVo.setUserName(userVo.getNickname());
            	addressService.save(fromAddressVo);
        	}
         	
        	addressMap.put("longitude", toAddressVo.getLongitude());
        	addressMap.put("latitude", toAddressVo.getLatitude());
        	List<AddressVo> toAddressList = addressService.queryList(addressMap);
        	if(null == toAddressList || toAddressList.size() <=0){
            	toAddressVo.setDetailInfo(toAddressVo.getAddress());
            	toAddressVo.setUserId(userId);
            	
            	toAddressVo.setSubject(contractCarpoolVo.getSubject());
            	toAddressVo.setUserName(userVo.getNickname());
            	addressService.save(toAddressVo);
        	}
        	
        	SchedulingCarpoolVo scv = schedulingService.queryObject(contractCarpoolVo.getSchedulingId());
        	if(null != scv) {
        		scv.setStatus(2);
            	scv.setContractId(contractId);
            	schedulingService.update(scv);//标记该合约对应的行程已匹配
        	}
        	
        	contractService.update(contractCarpoolVo);
        	return toResponsObject(200, "success", "接受合约成功");
    	}
    	return toResponsObject(500, "fail", "id为："+contractId+"的合约不是新建合约");
    }
    
    /**
     * 取消合约
     */
    @ApiOperation(value = "取消合约")
    @PostMapping("v1/cancelContract")
    public Object cancelContractV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId")) {
    		return toResponsFail(500, "userId 或 contractId 不能为空 ");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	Long contractId = Long.valueOf(jsonMap.get("contractId") + "");
    	
    	ContractCarpoolVo ccv = contractService.queryObject(contractId);
    	if(null == ccv) {
    		return toResponsObject(500, "fail", "没有id为："+contractId+"的合约");
    	}
    	
    	if(ccv.getStatus() != 0 && ccv.getStatus() != 1) {
			return toResponsObject(500, "fail", "该合约已进行或已取消或已完成，不能取消");
		}
    	
    	if(!userId.equals(ccv.getUserId()) && !userId.equals(ccv.getQyUserId())) {
    		return toResponsObject(500, "fail", "你即不是发起者也不是签约者，不能取消合约");
    	}
    	
    	ccv.setStatus(2);
//    	if(userId == ccv.getUserId()) {
//    		ccv.setQyUserId(null);
//    		ccv.setStatus(2);
//    	}else {
//    		if(null != ccv.getQyUserId() && userId == ccv.getQyUserId()) {
//    			ccv.setQyUserId(null);
//    			ccv.setStatus(0);
//    		}
//    	}
    	contractService.update(ccv);
    	
    	SchedulingCarpoolVo scv = schedulingService.queryObject(ccv.getSchedulingId());
    	if(null != scv) {
    		scv.setStatus(0);//合约取消，行程状态回到新建状态，允许再匹配
    		schedulingService.update(scv);
    	}
    	return toResponsObject(200, "success", "取消合约成功");
    }
    
    /**
     * 确认上车
     */
    @ApiOperation(value = "确认上车")
    @PostMapping("v1/confirmOnCar")
    public Object confirmOnCarV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId")) {
    		return toResponsFail(500, "userId 或 contractId 不能为空 ");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	Long contractId = Long.valueOf(jsonMap.get("contractId") + "");
    	
    	ContractCarpoolVo ccv = contractService.queryObject(contractId);
    	if(null == ccv) {
    		return toResponsObject(500, "fail", "没有id为："+contractId+"的合约");
    	}
    	
    	if(ccv.getStatus() != 1) {
			return toResponsObject(500, "fail", "该合约未接受，无法确认上车");
		}
    	
    	if(!userId.equals(ccv.getUserId()) && !userId.equals(ccv.getQyUserId())) {
    			return toResponsObject(500, "fail", "你即不是发起者也不是签约者，不能确认上车");
    	}
    	
    	if(ccv.getUserType() == 1 && userId.equals(ccv.getUserId()) || ccv.getUserType() == 0 && userId.equals(ccv.getQyUserId())){//只能是司机才有确认上车
    		ccv.setRidingStatus(1);
        	ccv.setOnCarTimestamp(System.currentTimeMillis());
        	contractService.update(ccv);
        	return toResponsObject(200, "success", "确认上车成功");
    	}else{
    		return toResponsObject(500, "fail", "不是司机，确认上车失败");
    	}
    	
    	
    }
    
    /**
     * 确认到达
     */
    @ApiOperation(value = "确认到达")
    @PostMapping("v1/confirmArrive")
    public Object confirmArriveV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId") || null == jsonMap.get("contractId")) {
    		return toResponsFail(500, "userId 或 contractId 不能为空 ");
    	}
    	Long userId = Long.valueOf(jsonMap.get("userId") + "");
    	Long contractId = Long.valueOf(jsonMap.get("contractId") + "");
    	
    	ContractCarpoolVo ccv = contractService.queryObject(contractId);
    	if(null == ccv) {
    		return toResponsObject(500, "fail", "没有id为："+contractId+"的合约");
    	}
    	
    	if(ccv.getStatus() != 1) {
			return toResponsObject(500, "fail", "该合约已取消或已完成或未接受，无法确认到达");
		}
    	if(ccv.getRidingStatus() == 0) {
			return toResponsObject(500, "fail", "乘客未上车，无法确认到达");
		}
    	
    	if(!userId.equals(ccv.getUserId()) && !userId.equals(ccv.getQyUserId())) {
    			return toResponsObject(500, "fail", "你即不是发起者也不是签约者，不能确认上车");
    	}
    	
    	if(userId.equals(ccv.getUserId())) {
    		if(ccv.getRidingStatus() == 3) {
    			ccv.setRidingStatus(4);
    			if(ccv.getContractType() == 0) {
    	    		ccv.setStatus(3);
    	    		UserVo cjuserVo = userService.queryObject(userId);
    	    		cjuserVo.setContractCount(cjuserVo.getContractCount() + 1);
    	    		userService.update(cjuserVo);
    	    		
    	    		UserVo qyuserVo = userService.queryObject(ccv.getQyUserId());
    	    		qyuserVo.setContractCount(qyuserVo.getContractCount() + 1);
    	    		userService.update(qyuserVo);
    	    		
    	    		
    	    	}
    		}else{
    			ccv.setRidingStatus(2);
    		}
    	}else if(userId.equals(ccv.getQyUserId())) {
    		if(ccv.getRidingStatus() == 2) {
    			ccv.setRidingStatus(4);
    			if(ccv.getContractType() == 0) {
    	    		ccv.setStatus(3);
    	    		UserVo qyuserVo = userService.queryObject(userId);
    	    		qyuserVo.setContractCount(qyuserVo.getContractCount() + 1);
    	    		userService.update(qyuserVo);
    	    		
    	    		UserVo cjuserVo = userService.queryObject(ccv.getUserId());
    	    		cjuserVo.setContractCount(cjuserVo.getContractCount() + 1);
    	    		userService.update(cjuserVo);
    	    	}
    		}else{
    			ccv.setRidingStatus(3);
    		}
    	}
    	
    	//ccv.setOnCarTimestamp(null);
    	ccv.setConfirmArriveTimestamp(System.currentTimeMillis());
    	contractService.update(ccv);
    	
    	return toResponsObject(200, "确认到达成功", ccv);
    }
    
    /**
     * 设置合约提醒
     */
    @ApiOperation(value = "设置合约提醒")
    @PostMapping("v1/contractWarn")
    public Object contractWarnV1(@LoginUser UserVo loginUser, @RequestBody ContractWarnCarpoolVo contractWarnCarpoolVo) {
    	Map<String, Object> mapWarn = new HashMap<String, Object>();
		mapWarn.put("contractId", contractWarnCarpoolVo.getContractId());
		mapWarn.put("userId", contractWarnCarpoolVo.getUserId());
		List<ContractWarnCarpoolVo> contractWarnList = contractWarnService.queryList(mapWarn);
		if(null != contractWarnList && contractWarnList.size() > 0) {
			ContractWarnCarpoolVo contractWarnCarpoolVoi = contractWarnList.get(0);
			contractWarnService.delete(contractWarnCarpoolVoi.getId());
		}
    	contractWarnService.save(contractWarnCarpoolVo);
    	return toResponsObject(200, "设置合约提醒成功", "");
    }
    
    public static void main(String[] args) {
    	long userId = Long.valueOf("");
    	
    	System.out.println(userId);
    }
}