package com.carpool.api;

import com.github.pagehelper.PageHelper;
import com.alibaba.fastjson.JSONObject;
import com.carpool.annotation.IgnoreAuth;
import com.carpool.annotation.LoginUser;
import com.carpool.entity.*;
import com.carpool.entitycarpool.ActivityCarpoolVo;
import com.carpool.entitycarpool.AdCarpoolVo;
import com.carpool.entitycarpool.ContractCarpoolVo;
import com.carpool.entitycarpool.SchedulingCarpoolVo;
import com.carpool.entitycarpool.UserActivityCarpoolVo;
import com.carpool.service.*;
import com.carpool.util.AdPropertiesUtils;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiPageUtils;
import com.carpool.utils.ApiRRException;
import com.carpool.utils.Query;
import com.carpool.utils.ResourceUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "首页接口文档")
@RestController
@RequestMapping("/api/index")
public class ApiIndexController extends ApiBaseAction {
    @Autowired
    private ApiAdService adService;
    @Autowired
    private ApiChannelService channelService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiBrandService brandService;
    @Autowired
    private ApiTopicService topicService;
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiCartService cartService;
    @Autowired
    private ApiActivityService activityService;
    @Autowired
    private ApiUserActivityService userActivityService;
    @Autowired
    private ApiContractService contractService;
    @Autowired
    private ApiSchedulingService schedulingService;
    @Autowired
    private ApiAddressService addressService;

    /**
     * 测试
     */
    @IgnoreAuth
    @PostMapping(value = "test")
    public Object test() {
        return toResponsMsgSuccess("请求成功yyy");
    }

    /**
     * app首页
     */
    @ApiOperation(value = "首页")
    @IgnoreAuth
    @PostMapping(value = "index")
    public Object index() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        resultObj.put("banner", banner);
        //
        param = new HashMap<String, Object>();
        param.put("sidx", "sort_order ");
        param.put("order", "asc ");
        List<ChannelVo> channel = channelService.queryList(param);
        resultObj.put("channel", channel);
        //
        param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("is_delete", 0);
        param.put("fields", "id, name, list_pic_url, retail_price");
        PageHelper.startPage(0, 4, false);
        List<GoodsVo> newGoods = goodsService.queryList(param);
        resultObj.put("newGoodsList", newGoods);
        //
        param = new HashMap<String, Object>();
        param.put("is_hot", "1");
        param.put("is_delete", 0);
        PageHelper.startPage(0, 3, false);
        List<GoodsVo> hotGoods = goodsService.queryHotGoodsList(param);
        resultObj.put("hotGoodsList", hotGoods);
        // 当前购物车中
        List<CartVo> cartList = new ArrayList<CartVo>();
        if (null != getUserId()) {
            //查询列表数据
            Map<String, Object> cartParam = new HashMap<String, Object>();
            cartParam.put("user_id", getUserId());
            cartList = cartService.queryList(cartParam);
        }
        if (null != cartList && cartList.size() > 0 && null != hotGoods && hotGoods.size() > 0) {
            for (GoodsVo goodsVo : hotGoods) {
                for (CartVo cartVo : cartList) {
                    if (goodsVo.getId().equals(cartVo.getGoods_id())) {
                        goodsVo.setCart_num(cartVo.getNumber());
                    }
                }
            }
        }
        //
        param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("sidx", "new_sort_order ");
        param.put("order", "asc ");
        param.put("offset", 0);
        param.put("limit", 4);
        List<BrandVo> brandList = brandService.queryList(param);
        resultObj.put("brandList", brandList);

        param = new HashMap<String, Object>();
        param.put("offset", 0);
        param.put("limit", 3);
        List<TopicVo> topicList = topicService.queryList(param);
        resultObj.put("topicList", topicList);

        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
        param.put("notName", "推荐");//<>
        List<CategoryVo> categoryList = categoryService.queryList(param);
        List<Map<String, Object>> newCategoryList = new ArrayList<>();

        for (CategoryVo categoryItem : categoryList) {
            param.remove("fields");
            param.put("parent_id", categoryItem.getId());
            List<CategoryVo> categoryEntityList = categoryService.queryList(param);
            List<Integer> childCategoryIds = new ArrayList<>();
            for (CategoryVo categoryEntity : categoryEntityList) {
                childCategoryIds.add(categoryEntity.getId());
            }
            //
            param = new HashMap<String, Object>();
            param.put("categoryIds", childCategoryIds);
            param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price");
            PageHelper.startPage(0, 7, false);
            List<GoodsVo> categoryGoods = goodsService.queryList(param);
            Map<String, Object> newCategory = new HashMap<String, Object>();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("name", categoryItem.getName());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultObj.put("categoryList", newCategoryList);
        return toResponsSuccess(resultObj);
    }
    
    /**
     * app首页
     */
    @ApiOperation(value = "首页")
    @IgnoreAuth
    @PostMapping(value = "v1/homePage")
    public Object homePage(@LoginUser UserVo loginUser, @RequestBody Map<String, Object> jsonMap) {
    	Object userId = jsonMap.get("userId");
    	if(null != userId) {
    		userId = Long.valueOf(jsonMap.get("userId") +"");
    	}
    	Integer page = 1;
//    	if(null != jsonMap.get("page")) {
//    		page = Integer.valueOf(jsonMap.get("page") +"");
//    	}
    	Integer pageSize = 5;
//    	if(null != jsonMap.get("pageSize")) {
//    		pageSize = Integer.valueOf(jsonMap.get("pageSize") +"");
//    	}
    	//从header中获取token
        String token = request.getHeader("X-Nideshop-Token");
        logger.info("============homePage token=============" +token);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("X-Nideshop-Token");
            logger.info("============homePage token=============" +token);
        }
    	//查询token信息
        TokenEntity tokenEntity = tokenService.queryByToken(token);
        if (StringUtils.isBlank(token) || tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            //userId = null;
        }
        
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        List<AdCarpoolVo> bannerList = AdPropertiesUtils.setAdVoListToAdCarpoolVoList(banner);
        resultObj.put("banner", bannerList);
        
        resultObj.put("describe", ResourceUtil.getConfigByName("indexDescribe"));
        
//        if(userId == null) {
//        	resultObj.put("activity", new ArrayList<ActivityCarpoolVo>());
//        	resultObj.put("contract", new ArrayList<ContractCarpoolVo>());
//        	resultObj.put("scheduling", new ArrayList<SchedulingCarpoolVo>());
//        }else {
        List<ActivityCarpoolVo> aclistn = new ArrayList<ActivityCarpoolVo>();
        logger.info("============homePage userId=============" +userId);
        if(null != userId) {
        	userId = Long.valueOf(jsonMap.get("userId") + "");
    		Map<String, Object> mapuacv = new HashMap<String, Object>();
        	mapuacv.put("userId", userId);
        	mapuacv.put("page", page);
        	mapuacv.put("limit", pageSize);
        	mapuacv.put("order", "desc");
        	mapuacv.put("sidx", "activity_id");
        	Query queryuacv = new Query(mapuacv);
        	PageHelper.startPage(queryuacv.getPage(), queryuacv.getLimit());
        	List<UserActivityCarpoolVo> uacvlist = userActivityService.queryList(queryuacv);
        	if(null != uacvlist && uacvlist.size() > 0) {
        		for(UserActivityCarpoolVo uacv : uacvlist) {
        			ActivityCarpoolVo acVo = activityService.queryObject(uacv.getActivityId());
            		if(null != acVo) {
            			acVo.setIsEnrollCurUser(true);
                		acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
                		
                		if(Long.valueOf(userId + "").longValue() == acVo.getUserId().longValue()) {
                			acVo.setIsEdit(true);
                		}else {
                			acVo.setIsEdit(false);
                		}
                		
                		aclistn.add(acVo);
            		}
        		}
        	}
        }
        resultObj.put("activity", aclistn);
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("userId", userId);
        	logger.info("============homePage userId=============" +userId);
        	map.put("order", "desc");
        	map.put("sidx", "id");
        	List<ContractCarpoolVo> cclist = contractService.queryListByUserIdORqyUserId(map);
        	List<ContractCarpoolVo> cclistn = new ArrayList<ContractCarpoolVo>();
            if(null != cclist && cclist.size() > 0) {
            	for(int i=0;i<cclist.size();i++) {
            		ContractCarpoolVo ccVo = cclist.get(i);
            		if(ccVo.getStatus() != 0 && ccVo.getStatus() != 2) {//不需要展示新建合约和取消的合约
            			ccVo.setFromAddressVo(addressService.queryObject(ccVo.getFromAddressId()));
                		ccVo.setToAddressVo(addressService.queryObject(ccVo.getToAddressId()));
                		cclistn.add(ccVo);
            		}
            		
            		if(i==4) {
            			break;
            		}
            	}
            	resultObj.put("contract", cclistn);
            }else {
            	resultObj.put("contract", cclistn);
            }
            
            List<SchedulingCarpoolVo> sclist = schedulingService.queryList(map);
            List<SchedulingCarpoolVo> sclistn = new ArrayList<SchedulingCarpoolVo>();
            if(null != sclist && sclist.size() > 0) {
            	for(int i=0;i<sclist.size();i++) {
            		SchedulingCarpoolVo scVo = sclist.get(i);
            		scVo.setFromAddressVo(addressService.queryObject(scVo.getFromAddressId()));
            		scVo.setToAddressVo(addressService.queryObject(scVo.getToAddressId()));
            		sclistn.add(scVo);
            		if(i==4) {
            			break;
            		}
            	}
            	resultObj.put("scheduling", sclistn);
            }else {
            	resultObj.put("scheduling", sclistn);
            }
//        }
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
        if(null != hotaclist && hotaclist.size() > 0) {
        	for(ActivityCarpoolVo acVo : hotaclist) {
            	acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
            }
        }
        
        resultObj.put("hotactivityList", hotaclist);
        
        Map<String, Object> map2 = new HashMap<String, Object>();
        //map1.put("hotCollect", 1);
        map2.put("page", page);
        map2.put("limit", pageSize);
        map2.put("order", "desc");
        map2.put("sidx", "id");
    	Query query2 = new Query(map2);
    	PageHelper.startPage(query2.getPage(), query2.getLimit());
        List<ActivityCarpoolVo> allActivity = activityService.queryList(query2);
        if(null != allActivity && allActivity.size() > 0) {
        	for(ActivityCarpoolVo acVo : allActivity) {
            	acVo.setAddressVo(addressService.queryObject(acVo.getAddressId()));
            }
        }
        resultObj.put("allActivity", allActivity);
        
        
        return toResponsObject(200,"success",resultObj);
    }


    /**
     * 新商品信息
     */
    @ApiOperation(value = "新商品信息")
    @IgnoreAuth
    @PostMapping(value = "newGoods")
    public Object newGoods() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("is_delete", 0);
        param.put("fields", "id, name, list_pic_url, retail_price");
        PageHelper.startPage(0, 4, false);
        List<GoodsVo> newGoods = goodsService.queryList(param);
        resultObj.put("newGoodsList", newGoods);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "新热门商品信息")
    @IgnoreAuth
    @PostMapping(value = "hotGoods")
    public Object hotGoods() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("is_hot", "1");
        param.put("is_delete", 0);
        PageHelper.startPage(0, 3, false);
        List<GoodsVo> hotGoods = goodsService.queryHotGoodsList(param);
        resultObj.put("hotGoodsList", hotGoods);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "topic")
    @IgnoreAuth
    @PostMapping(value = "topic")
    public Object topic() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("offset", 0);
        param.put("limit", 3);
        List<TopicVo> topicList = topicService.queryList(param);
        resultObj.put("topicList", topicList);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "brand")
    @IgnoreAuth
    @PostMapping(value = "brand")
    public Object brand() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("sidx", "new_sort_order ");
        param.put("order", "asc ");
        param.put("offset", 0);
        param.put("limit", 4);
        List<BrandVo> brandList = brandService.queryList(param);
        resultObj.put("brandList", brandList);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "category")
    @IgnoreAuth
    @PostMapping(value = "category")
    public Object category() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
        param.put("notName", "推荐");//<>
        List<CategoryVo> categoryList = categoryService.queryList(param);
        List<Map<String, Object>> newCategoryList = new ArrayList<>();

        for (CategoryVo categoryItem : categoryList) {
            param.remove("fields");
            param.put("parent_id", categoryItem.getId());
            List<CategoryVo> categoryEntityList = categoryService.queryList(param);
            List<Integer> childCategoryIds = null;
            if (categoryEntityList != null && categoryEntityList.size() > 0) {
                childCategoryIds = new ArrayList<>();
                for (CategoryVo categoryEntity : categoryEntityList) {
                    childCategoryIds.add(categoryEntity.getId());
                }
            }
            //
            param = new HashMap<String, Object>();
            param.put("categoryIds", childCategoryIds);
            param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price");
            param.put("is_delete", "0");
            PageHelper.startPage(0, 7, false);
            List<GoodsVo> categoryGoods = goodsService.queryList(param);
            Map<String, Object> newCategory = new HashMap<String, Object>();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("name", categoryItem.getName());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultObj.put("categoryList", newCategoryList);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "banner")
    @IgnoreAuth
    @PostMapping(value = "banner")
    public Object banner() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        resultObj.put("banner", banner);
        //

        return toResponsSuccess(resultObj);
    }

    @ApiOperation(value = "channel")
    @IgnoreAuth
    @PostMapping(value = "channel")
    public Object channel() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        Map<String, Object> param = new HashMap<String, Object>();
        param = new HashMap<String, Object>();
        param.put("sidx", "sort_order ");
        param.put("order", "asc ");
        List<ChannelVo> channel = channelService.queryList(param);
        resultObj.put("channel", channel);
        //

        return toResponsSuccess(resultObj);
    }
    
    @ApiOperation(value = "popularizeMessage")
    @IgnoreAuth
    @PostMapping(value = "v1/popularizeMessage")
    public Object popularizeMessageV1() {
    	JSONObject jsonParams = getJsonRequest();
    	Integer page = jsonParams.getInteger("page");
    	if(null == page) {
    		page = 1;
    	}
    	Integer pageSize = jsonParams.getInteger("pageSize");
    	if(null == pageSize) {
    		pageSize = 10;
    	}
    	
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("ad_position_id", 2);
	    param.put("page", page);
	    param.put("limit", pageSize);
	    param.put("order", "desc");
	    param.put("sidx", "id");
    	Query query = new Query(param);
    	PageHelper.startPage(query.getPage(), query.getLimit());
	    List<AdVo> banner = adService.queryList(query);
	    List<AdCarpoolVo> bannerList = AdPropertiesUtils.setAdVoListToAdCarpoolVoList(banner);
	    
	    int total = adService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(bannerList, total, query.getLimit(), query.getPage());
	    return toResponsObject(200,"success",pageUtil);
    }
    
    @ApiOperation(value = "popularizeMessage")
    @IgnoreAuth
    @PostMapping(value = "v1/systemMessage")
    public Object systemMessageV1() {
    	JSONObject jsonParams = getJsonRequest();
    	Integer page = jsonParams.getInteger("page");
    	if(null == page) {
    		page = 1;
    	}
    	Integer pageSize = jsonParams.getInteger("pageSize");
    	if(null == pageSize) {
    		pageSize = 10;
    	}
    	
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("ad_position_id", 3);
	    param.put("page", page);
	    param.put("limit", pageSize);
	    param.put("order", "desc");
	    param.put("sidx", "id");
    	Query query = new Query(param);
    	PageHelper.startPage(query.getPage(), query.getLimit());
	    List<AdVo> banner = adService.queryList(query);
	    List<AdCarpoolVo> bannerList = AdPropertiesUtils.setAdVoListToAdCarpoolVoList(banner);
	    
	    int total = adService.queryTotal(query);
		ApiPageUtils pageUtil = new ApiPageUtils(bannerList, total, query.getLimit(), query.getPage());
	    return toResponsObject(200,"success",pageUtil);
    }
}