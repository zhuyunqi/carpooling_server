package com.carpool.api;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.carpool.annotation.IgnoreAuth;
import com.carpool.annotation.LoginUser;
import com.carpool.entity.AddressVo;
import com.carpool.entity.InputOutputUserInfo;
import com.carpool.entity.SmsConfig;
import com.carpool.entity.SmsLogVo;
import com.carpool.entity.UserVo;
import com.carpool.entitycarpool.AddressCarpoolVo;
import com.carpool.entitycarpool.UserAddressCarpoolVo;
import com.carpool.entitycarpool.UserFriendDTO;
import com.carpool.service.ApiUserService;
import com.carpool.service.SysConfigService;
import com.carpool.util.AddressPropertiesUtils;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiPageUtils;
import com.carpool.util.BeanMapConvert;
import com.carpool.util.HttpUtils;
import com.carpool.utils.CharUtil;
import com.carpool.utils.Constant;
import com.carpool.utils.DateUtils;
import com.carpool.utils.Query;
import com.carpool.utils.R;
import com.carpool.utils.RRException;
import com.carpool.utils.ResourceUtil;
import com.carpool.utils.SmsUtil;
import com.carpool.utils.StringUtils;
import com.carpool.validator.Assert;
import com.github.pagehelper.PageHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "会员验证")
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends ApiBaseAction {
    @Autowired
    private ApiUserService userService;
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 发送短信
     */
    @ApiOperation(value = "发送短信")
    @PostMapping("smscode")
    public Object smscode(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        String phone = jsonParams.getString("phone");
        // 一分钟之内不能重复发送短信
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());
        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLog_date()) < 1 * 60) {
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "您的验证码是：" + sms_code + "，请在页面中提交验证码完成验证。";
        // 发送短信
        String result = "";
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            return toResponsFail("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getName())) {
            return toResponsFail("请先配置短信平台用户名");
        }
        if (StringUtils.isNullOrEmpty(config.getPwd())) {
            return toResponsFail("请先配置短信平台密钥");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
            return toResponsFail("请先配置短信平台签名");
        }
        try {
            /**
             * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
             */
            result = SmsUtil.crSendSms(config.getName(), config.getPwd(), phone, msgContent, config.getSign(),
                    DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), "");
        } catch (Exception e) {

        }
        String arr[] = result.split(",");

        if ("0".equals(arr[0])) {
            smsLogVo = new SmsLogVo();
            smsLogVo.setLog_date(System.currentTimeMillis() / 1000);
            smsLogVo.setUser_id(loginUser.getUserId());
            smsLogVo.setPhone(phone);
            smsLogVo.setSms_code(sms_code);
            smsLogVo.setSms_text(msgContent);
            userService.saveSmsCodeLog(smsLogVo);
            return toResponsSuccess("短信发送成功");
        } else {
            return toResponsFail("短信发送失败");
        }
    }
    
    /**
     * 发送短信
     */
    @ApiOperation(value = "发送短信")
    @PostMapping("v1/smscode")
    public Object smscodeV1(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        long userId = jsonParams.getLong("userId");
        logger.info("========== smscodeV1 userId ==========" + userId);
        String countryCode = jsonParams.getString("countryCode");
        logger.info("========== smscodeV1 countryCode ==========" + countryCode);
        String phone = jsonParams.getString("phone");
        logger.info("========== smscodeV1 phone ==========" + phone);
        // 一分钟之内不能重复发送短信
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(userId);
        if (null != smsLogVo && (System.currentTimeMillis() / 1000 - smsLogVo.getLog_date()) < 1 * 60) {
            return toResponsFail("短信已发送");
        }
        //生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "您的验证码是：" + sms_code + "，请在页面中提交验证码完成验证。";
        // 发送短信
        String result = "";
        
        try {
            /**
             * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
             */
            result = SmsUtil.sendSMSMessage(msgContent, countryCode, phone);
            
            smsLogVo = new SmsLogVo();
            smsLogVo.setLog_date(System.currentTimeMillis() / 1000);
            smsLogVo.setUser_id(loginUser.getUserId());
            smsLogVo.setPhone(phone);
            smsLogVo.setSms_code(sms_code);
            smsLogVo.setSms_text(msgContent);
            userService.saveSmsCodeLog(smsLogVo);
            return toResponsObject(200,"短信发送成功", "");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return toResponsFail(500,"短信发送失败");
    }

    /**
     * 获取当前会员等级
     *
     * @param loginUser
     * @return
     */
    @ApiOperation(value = "获取当前会员等级")
    @PostMapping("getUserLevel")
    public Object getUserLevel(@LoginUser UserVo loginUser) {
        String userLevel = userService.getUserLevel(loginUser);
        return toResponsSuccess(userLevel);
    }
    
    /**
     * 获取用户头像昵称
     *
     * @param userName
     * @return
     */
    @ApiOperation(value = "获取用户头像昵称")
    @IgnoreAuth
    @PostMapping("v1/getUserAvatarNickName")
    public Object getUserV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	String userName = jsonParams.getString("userName");
    	Map<String, Object> userM = new HashMap<String, Object>();
        UserVo userVo = userService.queryByUserNameMove(userName);
        if(null == userVo) {
        	return toResponsObject(500,"查不到用户信息", userM);
        }
        userM.put("avatar", userVo.getAvatar());
        userM.put("nickname", userVo.getNickname());
        userM.put("username", userName);
        return toResponsObject(200,"获取用户头像昵称", userM);
    }

    /**
     * 绑定手机
     */
    @ApiOperation(value = "绑定手机")
    @PostMapping("bindMobile")
    public Object bindMobile(@LoginUser UserVo loginUser) {
        JSONObject jsonParams = getJsonRequest();
        SmsLogVo smsLogVo = userService.querySmsCodeByUserId(loginUser.getUserId());

        String mobile_code = jsonParams.getString("mobile_code");
        String mobile = jsonParams.getString("mobile");

        if (!mobile_code.equals(smsLogVo.getSms_code())) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = userService.queryObject(loginUser.getUserId());
        userVo.setMobile(mobile);
        userService.update(userVo);
        return toResponsSuccess("手机绑定成功");
    }
    
    /**
     * 绑定手机
     */
    @ApiOperation(value = "绑定手机")
    @PostMapping("v1/bingPhone")
    public Object bingPhoneV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	long userId = jsonParams.getLongValue("userId");
    	String code = jsonParams.getString("code");
    	String phone = jsonParams.getString("phone");
    	SmsLogVo smsLogVo = userService.querySmsCodeByUserId(userId);
        if (!code.equals(smsLogVo.getSms_code())) {
            return toResponsFail("验证码错误");
        }
        UserVo userVo = userService.queryObject(userId);
        userVo.setMobile(phone);
        userService.update(userVo);
        return toResponsObject(200,"手机绑定成功", userVo);
    }
    
    /**
     * 更新用户昵称
     */
    @ApiOperation(value = "更新用户昵称")
    @PostMapping("v1/updateName")
    public Object updateNameV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	long userId = jsonParams.getLongValue("userId");
    	String nickName = jsonParams.getString("nickName");
        UserVo userVo = userService.queryObject(userId);
        userVo.setNickname(nickName);
        userService.update(userVo);
        
        String userInfoUrl = ResourceUtil.getConfigByName("imusergetinfo");
        String userInfojson = "{\"userId\":\""+userVo.getImUserId()+"\"}";
        logger.info("======== getIMUserFriendsV1 userInfojson ==========" +userInfojson);
        String imUserInfojson = HttpUtils.HttpPostIMServerApiWithJson(userInfoUrl, userInfojson);
        logger.info("======== getIMUserFriendsV1 imJsonString ==========" +imUserInfojson);
        Map<String, Object> imUserInfojsonMap = (Map<String, Object>) JSONUtils.parse(imUserInfojson);
        Map<String, Object> userInfoResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(imUserInfojsonMap.get("result")));
        logger.info("======== getIMUserFriendsV1 userInfoResult ==========" +userInfoResult);
        InputOutputUserInfo inputOutputUserInfo = new InputOutputUserInfo();
        try {
			inputOutputUserInfo = (InputOutputUserInfo) BeanMapConvert.convertMap(InputOutputUserInfo.class, userInfoResult);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        inputOutputUserInfo.setDisplayName(nickName);
        
        String userCreateUrl = ResourceUtil.getConfigByName("imusercreate");
        String userCreatejson = JSONObject.toJSONString(inputOutputUserInfo);
        //String userCreatejson = "{\"userId\":\""+userVo.getImUserId()+"\",\"name\":\""+userInfoResult.get("name")+"\",\"displayName\":\""+remarkName+"\",\"extra\":\""+remarkName+"\"}";
        logger.info("======== getIMUserFriendsV1 userCreatejson ==========" +userCreatejson);
        String imUserCreatejson = HttpUtils.HttpPostIMServerApiWithJson(userCreateUrl, userCreatejson);
        logger.info("======== getIMUserFriendsV1 imUserCreatejson ==========" +imUserCreatejson);
        
        return toResponsObject(200, "昵称修改成功", userVo);
    }
    
    /**
     * 设置好友备注名
     */
    @ApiOperation(value = "设置好友备注名")
    @PostMapping("v1/updateRemarkName")
    public Object updateRemarkNameV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	String userName = jsonParams.getString("userName");
    	String fuserName = jsonParams.getString("fuserName");
    	String remarkName = jsonParams.getString("remarkName");
    	
    	UserVo userVo = userService.queryByUserNameMove(userName);
    	//UserVo fuserVo = userService.queryByUserNameMove(fuserName);
    	String url = ResourceUtil.getConfigByName("imfriendlist");
        String json = "{\"userId\":\""+userVo.getImUserId()+"\",\"status\":\""+0+"\"}";
        logger.info("======== getIMUserFriendsV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
        logger.info("======== getIMUserFriendsV1 imJsonString ==========" +imJsonString);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        List<String> imResult = (List<String>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        logger.info("======== getIMUserFriendsV1 imString ==========" +imResult);
    	if(null == imResult || imResult.size() <=0) {
    		return toResponsFail(500, "这两位用户不是好友");
    	}
          
    	//??????
        return toResponsObject(200, "设置好友备注名", userVo);
    }
    
    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @PostMapping("v1/userInfo")
    public Object userInfoV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	long userId = jsonParams.getLongValue("userId");
        UserVo userVo = userService.queryObject(userId);
        return toResponsObject(200, "查询用户信息成功", userVo);
    }
    
    /**
     * 更新用户头像
     */
    @ApiOperation(value = "更新用户头像")
    @PostMapping("v1/updateAvatar")
    public Object updateAvatarV1(@LoginUser UserVo loginUser) {
    	JSONObject jsonParams = getJsonRequest();
    	long userId = jsonParams.getLongValue("userId");
    	String avatar = jsonParams.getString("avatar");
        UserVo userVo = userService.queryObject(userId);
        userVo.setAvatar(avatar);
        userService.update(userVo);
        
        String userInfoUrl = ResourceUtil.getConfigByName("imusergetinfo");
        String userInfojson = "{\"userId\":\""+userVo.getImUserId()+"\"}";
        logger.info("======== getIMUserFriendsV1 userInfojson ==========" +userInfojson);
        String imUserInfojson = HttpUtils.HttpPostIMServerApiWithJson(userInfoUrl, userInfojson);
        logger.info("======== getIMUserFriendsV1 imJsonString ==========" +imUserInfojson);
        Map<String, Object> imUserInfojsonMap = (Map<String, Object>) JSONUtils.parse(imUserInfojson);
        Map<String, Object> userInfoResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(imUserInfojsonMap.get("result")));
        logger.info("======== getIMUserFriendsV1 userInfoResult ==========" +userInfoResult);
        InputOutputUserInfo inputOutputUserInfo = new InputOutputUserInfo();
        try {
			inputOutputUserInfo = (InputOutputUserInfo) BeanMapConvert.convertMap(InputOutputUserInfo.class, userInfoResult);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        inputOutputUserInfo.setPortrait(avatar);
        
        String userCreateUrl = ResourceUtil.getConfigByName("imusercreate");
        String userCreatejson = JSONObject.toJSONString(inputOutputUserInfo);
        //String userCreatejson = "{\"userId\":\""+userVo.getImUserId()+"\",\"name\":\""+userInfoResult.get("name")+"\",\"displayName\":\""+userInfoResult.get("displayName")+"\",\"portrait\":\""+avatar+"\"}";
        logger.info("======== getIMUserFriendsV1 userCreatejson ==========" +userCreatejson);
        String imUserCreatejson = HttpUtils.HttpPostIMServerApiWithJson(userCreateUrl, userCreatejson);
        logger.info("======== getIMUserFriendsV1 imUserCreatejson ==========" +imUserCreatejson);
        return toResponsObject(200, "更新用户头像成功", userVo);
    }
    
    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码")
    @PostMapping("v1/updatePassword")
    public Object updatePasswordV1() {
    	JSONObject jsonParams = getJsonRequest();
    	long userId = jsonParams.getLongValue("userId");
    	String oldPassword = jsonParams.getString("oldPassword");
    	String newPassword = jsonParams.getString("newPassword");
        Assert.isBlank(oldPassword, "原密码不能为空");
        Assert.isBlank(newPassword, "新密码不能为空");

        UserVo user = userService.queryObject(userId);
        Assert.isNull(user, "用户不存在");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPassword))) {
            throw new RRException("手机号或密码错误");
        }
        
        user.setPassword(DigestUtils.sha256Hex(newPassword));
        userService.update(user);
        
        return toResponsObject(200, "更新用户密码成功", user);
    }
    
    /**
     * 保存用户好友关系
     */
    @ApiOperation(value = "保存用户好友关系")
    @PostMapping("v1/saveUserFriend")
    public Object saveUserFriendV1(@LoginUser UserVo loginUser, @RequestBody UserFriendDTO userFriendCarpoolVo) {
    	UserVo userVo = userService.queryByUserNameMove(userFriendCarpoolVo.getUserName());
    	userFriendCarpoolVo.setUserId(userVo.getUserId());
    	UserVo fuserVo = userService.queryByUserNameMove(userFriendCarpoolVo.getFuserName());
    	userFriendCarpoolVo.setFuserId(fuserVo.getUserId());
    	
    	String url = ResourceUtil.getConfigByName("imfriendlist");
        String json = "{\"userId\":\""+userVo.getImUserId()+"\",\"status\":\""+0+"\"}";
        logger.info("======== getIMUserFriendsV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
        logger.info("======== getIMUserFriendsV1 imJsonString ==========" +imJsonString);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        List<String> imResult = (List<String>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        logger.info("======== getIMUserFriendsV1 imString ==========" +imResult);
    	if(null != imResult && imResult.size() > 0) {
    		return toResponsObject(402, "保存用户好友关系失败，双方已是好友关系", "");
    	}
    	
    	
    	String fsurl = ResourceUtil.getConfigByName("imfriendstatus");
        String fsjson = "{\"userId\":\""+userVo.getImUserId()+"\"friendUid\":\""+fuserVo.getImUserId()+"\",\"status\":\""+0+"\"}";
        logger.info("======== getIMUserFriendsV1 fsjson ==========" +fsjson);
        String fsimJsonString = HttpUtils.HttpPostIMServerApiWithJson(fsurl, fsjson);
        logger.info("======== getIMUserFriendsV1 fsimJsonString ==========" +fsimJsonString);
        Map<String, Object> fsim = (Map<String, Object>) JSONUtils.parse(fsimJsonString);
        String fsimResult = (String) JSONUtils.parse(JSONUtils.toJSONString(fsim.get("msg")));
        logger.info("======== getIMUserFriendsV1 imString ==========" +fsimResult);
        return toResponsObject(200, "保存用户好友关系成功", "");
    }
    
    /**
     * 保存用户好友关系
     */
//    @ApiOperation(value = "获取用户好友")
//    @PostMapping("v1/getUserFriends")
//    public Object getUserFriendsV1(@LoginUser UserVo loginUser) {
//    	JSONObject jsonParams = getJsonRequest();
//    	long userId = jsonParams.getLongValue("userId");
//    	int page = jsonParams.getIntValue("page");
//    	int pageSize = jsonParams.getIntValue("pageSize");
//    	
//    	Map<String, Object> map1 = new HashMap<String, Object>();
//    	map1.put("userId", userId);
////    	map1.put("page", page);
////    	map1.put("limit", pageSize);
//    	map1.put("order", "desc");
//    	map1.put("sidx", "id");
////    	Query query = new Query(map1);
////    	PageHelper.startPage(query.getPage(), query.getLimit());
//    	List<UserFriendCarpoolVo> ufcvList = userFriendService.queryList(map1);
//    	List<UserVo> fuserList = new ArrayList<UserVo>();
//    	//int total = userFriendService.queryTotal(query);
//		for(UserFriendCarpoolVo ufcv : ufcvList) {
//			UserVo fuserVo = userService.queryObject(ufcv.getFuserId());
//			fuserList.add(fuserVo);
//		}
//		//ApiPageUtils pageUtil = new ApiPageUtils(fuserList, total, query.getLimit(), query.getPage());
//    	return toResponsObject(200, "success", fuserList);
//    }
    
    /**
     * 获取im用户好友关系
     */
    @ApiOperation(value = "获取im用户好友")
    @PostMapping("v1/getUserFriends")
    public Object getUserFriendsV1() {
    	JSONObject jsonParams = getJsonRequest();
    	Long userId = jsonParams.getLong("userId");
    	if(null == userId) {
    		return R.error(500, "参数userId不能为空");
    	}
    	UserVo userVosf = userService.queryObject(userId);
    	int status = jsonParams.getIntValue("status");
    	
    	String url = ResourceUtil.getConfigByName("imfriendlist");
        String json = "{\"userId\":\""+userVosf.getImUserId()+"\",\"status\":\""+status+"\"}";
        logger.info("======== getIMUserFriendsV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
        logger.info("======== getIMUserFriendsV1 imJsonString ==========" +imJsonString);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        List<String> imResult = (List<String>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        logger.info("======== getIMUserFriendsV1 imString ==========" +imResult);
        
        Map<String,List<UserVo>> imUserFriendsMap = new HashMap<String,List<UserVo>>();
        List<UserVo> imUserFriendsList = new ArrayList<UserVo>();
        if(null != imResult && imResult.size() > 0) {
        	for(String imUserId : imResult) {
        		UserVo userVo = userService.queryByIMUserId(imUserId);
        		if(null != userVo) {
        			imUserFriendsList.add(userVo);
        		}
        	}
        }
        
        imUserFriendsMap.put("imUserFriends", imUserFriendsList);
        
        return R.ok(200,"success",imUserFriendsMap);
    }
    
}