package com.carpool.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.carpool.annotation.LoginUser;
import com.carpool.entity.AddressVo;
import com.carpool.entity.UserVo;
import com.carpool.entitycarpool.AddressCarpoolVo;
import com.carpool.entitycarpool.UserAddressCarpoolVo;
import com.carpool.service.ApiAddressService;
import com.carpool.service.ApiUserAddressService;
import com.carpool.service.ApiUserService;
import com.carpool.util.AddressPropertiesUtils;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiPageUtils;
import com.carpool.utils.Query;
import com.github.pagehelper.PageHelper;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "地址")
@RestController
@RequestMapping("/api/address")
public class ApiAddressController extends ApiBaseAction {
    @Autowired
    private ApiAddressService addressService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiUserAddressService userAddressService;

    /**
     * 获取用户的地址
     */
    @ApiOperation(value = "获取用户的地址接口", response = Map.class)
    @PostMapping("list")
    public Object list(@LoginUser UserVo loginUser) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", loginUser.getUserId());
        List<AddressVo> addressEntities = addressService.queryList(param);
        return toResponsSuccess(addressEntities);
    }
    
    /**
     * 获取用户的地址
     */
    @ApiOperation(value = "获取用户的地址接口", response = Map.class)
    @PostMapping("v1/getAddress")
    public Object getAddressV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId 不能为空 ");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") + "");
    	
    	Integer page = 1;
    	if(jsonMap.get("page") != null) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(pageSize != null) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
    	
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	map1.put("userId", userId);
    	map1.put("page", page);
    	map1.put("limit", pageSize);
    	map1.put("order", "desc");
    	map1.put("sidx", "id");
    	Query query = new Query(map1);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<AddressVo> addressEntities = addressService.queryList(query);
        List<AddressCarpoolVo> addressCarpoolEntities = AddressPropertiesUtils.setAddVoListToAddCarpoolVoList(addressEntities);
        if(null != addressCarpoolEntities && addressCarpoolEntities.size() > 0) {
        	for(AddressCarpoolVo acVo : addressCarpoolEntities) {
            	Map<String, Object> map2 = new HashMap<String, Object>();
            	map2.put("userId", userId);
            	map2.put("addressId", acVo.getId());
            	List<UserAddressCarpoolVo>  uaclist = userAddressService.queryList(map2);
            	if(null != uaclist && uaclist.size() > 0) {
            		acVo.setIsCollect(true);
            	}else {
            		acVo.setIsCollect(false);
            	}
            	
            }
        }
        
    	int total = userAddressService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(addressCarpoolEntities, total, query.getLimit(), query.getPage());
		
        return toResponsObject(200, "success", pageUtil);
    }

    /**
     * 获取地址的详情
     */
    @ApiOperation(value = "获取地址的详情", response = Map.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "收获地址ID", required = true, dataType = "Long")})
    @PostMapping("detail")
    public Object detail(Long id, @LoginUser UserVo loginUser) {
        AddressVo entity = addressService.queryObject(id);
        //判断越权行为
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权查看", "");
        }
        return toResponsSuccess(entity);
    }

    /**
     * 添加或更新地址
     */
    @ApiOperation(value = "添加或更新地址", response = Map.class)
    @PostMapping("save")
    public Object save(@LoginUser UserVo loginUser) {
        JSONObject addressJson = this.getJsonRequest();
        AddressVo entity = new AddressVo();
        if (null != addressJson) {
            entity.setId(addressJson.getLong("id"));
            entity.setUserId(loginUser.getUserId());
            entity.setUserName(addressJson.getString("userName"));
            entity.setPostalCode(addressJson.getString("postalCode"));
            entity.setProvinceName(addressJson.getString("provinceName"));
            entity.setCityName(addressJson.getString("cityName"));
            entity.setCountyName(addressJson.getString("countyName"));
            entity.setDetailInfo(addressJson.getString("detailInfo"));
            entity.setNationalCode(addressJson.getString("nationalCode"));
            entity.setTelNumber(addressJson.getString("telNumber"));
            entity.setIs_default(addressJson.getInteger("is_default"));
        }
        if (null == entity.getId() || entity.getId() == 0) {
            entity.setId(null);
            addressService.save(entity);
        } else {
            addressService.update(entity);
        }
        return toResponsSuccess(entity);
    }
    
    /**
     * 添加地址
     */
    @ApiOperation(value = "添加地址", response = Map.class)
    @PostMapping("v1/save")
    public Object saveV1(@LoginUser UserVo loginUser, @RequestBody AddressVo address) {
    	address.setDetailInfo(address.getAddress());
    	
    	addressService.save(address);
        return toResponsObject(200, "success", address.getId());
    }
    
    /**
     * 更新地址
     */
    @ApiOperation(value = "更新地址", response = Map.class)
    @PostMapping("v1/update")
    public Object updateV1(@LoginUser UserVo loginUser, @RequestBody AddressVo address) {
    	address.setDetailInfo(address.getAddress());
    	addressService.update(address);
        return toResponsObject(200, "success", address.getId());
    }
    
    /**
     * 收藏地址
     */
    @ApiOperation(value = "收藏地址")
    @PostMapping("v1/collectAddress")
    public Object collectAddressV1(@LoginUser UserVo loginUser, @RequestBody AddressVo addressVo) {
    	Long userId = addressVo.getUserId();
    	if(null == userId) {
    		return toResponsFail(500, "userId不能为空！");
    	}
    	Long addressId = addressVo.getId();
    	if(null == addressId) {
    		Map<String, Object> addressMap = new HashMap<String, Object>();
        	addressMap.put("userId", userId);
        	addressMap.put("longitude", addressVo.getLongitude());
        	addressMap.put("latitude", addressVo.getLatitude());
        	List<AddressVo> addressList = addressService.queryList(addressMap);
        	if(null == addressList || addressList.size() <= 0) {
        		addressVo.setUserId(userId);
        		addressVo.setDetailInfo(addressVo.getAddress());
        		addressService.save(addressVo);
        		addressId = addressVo.getId();
        	}else {
        		addressId = addressList.get(0).getId();
        	}
    		
    	}
    	
    	
    	UserAddressCarpoolVo uacv = new UserAddressCarpoolVo();
        uacv.setUserId(userId);
        uacv.setAddressId(addressId);
        userAddressService.save(uacv);
    	return toResponsObject(200, "success", "收藏成功！");
    }
    
    /**
     * 取消收藏地址
     */
    @ApiOperation(value = "取消收藏地址")
    @PostMapping("v1/cancelCollectAddress")
    public Object cancelCollectAddressV1(@LoginUser UserVo loginUser, @RequestBody AddressVo addressVo) {
    	Long userId = addressVo.getUserId();
    	if(null == userId) {
    		return toResponsFail(500, "userId不能为空！");
    	}
    	Long addressId = addressVo.getId();
    	if(null == addressId) {
    		Map<String, Object> addressMap = new HashMap<String, Object>();
        	addressMap.put("userId", userId);
        	addressMap.put("longitude", addressVo.getLongitude());
        	addressMap.put("latitude", addressVo.getLatitude());
        	List<AddressVo> addressList = addressService.queryList(addressMap);
        	if(null != addressList && addressList.size() > 0) {
        		addressId = addressList.get(0).getId();
        	}
    	}
    	
    	userAddressService.deleteByUidAid(userId, addressId);
    	return toResponsObject(200, "success", "取消收藏成功！");
    }
    
    /**
     * 获取我的收藏地址
     */
    @ApiOperation(value = "获取我的收藏地址")
    @PostMapping("v1/getMyCollectAddress")
    public Object getMyCollectAddressV1(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	if(null == jsonMap.get("userId")) {
    		return toResponsFail(500, "userId 不能为空 ");
    	}
    	long userId = Long.valueOf(jsonMap.get("userId") +"");
    	Integer page = 1;
    	if(jsonMap.get("page") != null) {
    		page = Integer.valueOf(jsonMap.get("page") +"");
    	}
    	Integer pageSize = 10;
    	if(pageSize != null) {
    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
    	}
          
    	Map<String, Object> map1 = new HashMap<String, Object>();
    	map1.put("userId", userId);
    	map1.put("page", page);
    	map1.put("limit", pageSize);
    	map1.put("order", "desc");
    	map1.put("sidx", "id");
    	Query query = new Query(map1);
    	PageHelper.startPage(query.getPage(), query.getLimit());
    	List<UserAddressCarpoolVo> uacvList = userAddressService.queryList(query);
    	int total = userAddressService.queryTotal(query);
		List<AddressVo> addressList = new ArrayList<AddressVo>();
		for(UserAddressCarpoolVo uacv : uacvList) {
			AddressVo addressVo = addressService.queryObject(uacv.getAddressId());
			addressList.add(addressVo);
		}
		List<AddressCarpoolVo> addressCarpoolEntities = AddressPropertiesUtils.setAddVoListToAddCarpoolVoList(addressList);
		if(null == addressCarpoolEntities) {
			return toResponsObject(200, "success", "没有收藏地址");
		}
		for(AddressCarpoolVo acv : addressCarpoolEntities) {
			acv.setIsCollect(true);
		}
		ApiPageUtils pageUtil = new ApiPageUtils(addressCarpoolEntities, total, query.getLimit(), query.getPage());
    	return toResponsObject(200, "success", pageUtil);
    }
    

    /**
     * 删除指定的地址
     */
    @ApiOperation(value = "删除指定的地址", response = Map.class)
    @PostMapping("delete")
    public Object delete(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = this.getJsonRequest();
        Long id = jsonParam.getLong("id");

        AddressVo entity = addressService.queryObject(Long.valueOf(id));
        //判断越权行为
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权删除", "");
        }
        addressService.delete(id);
        return toResponsSuccess("");
    }
    
    /**
     * 删除指定的地址
     */
    @ApiOperation(value = "删除指定的地址", response = Map.class)
    @PostMapping("v1/delete")
    public Object deleteV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = this.getJsonRequest();
        Long id = jsonParam.getLong("id");
        
        AddressVo entity = addressService.queryObject(Long.valueOf(id));
        //判断越权行为
        if (!entity.getUserId().equals(loginUser.getUserId())) {
            return toResponsObject(403, "您无权删除", "");
        }
        
        Map<String, Object> map2 = new HashMap<String, Object>();
    	map2.put("userId", entity.getUserId());
    	map2.put("addressId", entity.getId());
    	List<UserAddressCarpoolVo>  uaclist = userAddressService.queryList(map2);
    	for(UserAddressCarpoolVo uac : uaclist){
    		userAddressService.delete(uac.getId());
    	}
        
//        entity.setUserId(null);
//        addressService.update(entity);
        addressService.delete(id);
        
        return toResponsObject(200, "success", "地址删除成功，且收藏地址栏该条地址也会一并消失");
    }
}