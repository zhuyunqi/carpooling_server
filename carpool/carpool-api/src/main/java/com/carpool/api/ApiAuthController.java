package com.carpool.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.carpool.annotation.IgnoreAuth;
import com.carpool.entity.FullUserInfo;
import com.carpool.entity.InputOutputUserInfo;
import com.carpool.entity.UserInfo;
import com.carpool.entity.UserVo;
import com.carpool.service.ApiUserService;
import com.carpool.service.TokenService;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.ApiUserUtils;
import com.carpool.util.BeanMapConvert;
import com.carpool.util.CommonUtil;
import com.carpool.util.HttpUtils;
import com.carpool.utils.CharUtil;
import com.carpool.utils.R;
import com.carpool.utils.RRException;
import com.carpool.utils.ResourceUtil;
import com.carpool.validator.Assert;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * API登录授权
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:31
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public R login(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        //用户登录
        UserVo userVo = userService.login(mobile, password);

        //生成token
        Map<String, Object> map = tokenService.createToken(userVo.getUserId());

        return R.ok(map);
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin() {
        JSONObject jsonParam = this.getJsonRequest();
        FullUserInfo fullUserInfo = null;
        String code = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("code"))) {
            code = jsonParam.getString("code");
        }
        if (null != jsonParam.get("userInfo")) {
            fullUserInfo = jsonParam.getObject("userInfo", FullUserInfo.class);
        }
        if (null == fullUserInfo) {
            return toResponsFail("登录失败");
        }

        Map<String, Object> resultObj = new HashMap<String, Object>();
        //
        UserInfo userInfo = fullUserInfo.getUserInfo();

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
        logger.info("》》》组合token为：" + requestUrl);
        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null == sessionData || StringUtils.isNullOrEmpty(sessionData.getString("openid"))) {
            return toResponsFail("登录失败");
        }
        //验证用户信息完整性
        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
        if (!fullUserInfo.getSignature().equals(sha1)) {
            return toResponsFail("登录失败");
        }
        Date nowTime = new Date();
        UserVo userVo = userService.queryByOpenId(sessionData.getString("openid"));
        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUsername("微信用户" + CharUtil.getRandomString(12));
            userVo.setPassword(sessionData.getString("openid"));
            userVo.setRegister_time(nowTime);
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            userVo.setGender(userInfo.getGender()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(userInfo.getNickName());
            userService.save(userVo);
        } else {
            userVo.setLast_login_ip(this.getClientIp());
            userVo.setLast_login_time(nowTime);
            userService.update(userVo);
        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (null == userInfo || StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        resultObj.put("token", token);
        resultObj.put("userInfo", userInfo);
        resultObj.put("userId", userVo.getUserId());
        return toResponsSuccess(resultObj);
    }
    
    
    
    /**
     * 退出登录
     */
    @IgnoreAuth
    @PostMapping("v1/logout")
    @ApiOperation(value = "退出登录接口")
    public R logoutV1(@RequestBody UserVo userVo) {
    	Assert.isNull(userVo, "不能为空");
        //用户退出
        tokenService.delete(userVo.getUserId());
        return R.ok(200,"success");
    }
    
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("v1/login")
    @ApiOperation(value = "登录接口")
    public R loginV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");

        //用户登录
        UserVo userVo1 = userService.login(userVo.getPhone(), userVo.getPassword());
        if(null == userVo1) {
        	return R.error(407, "用户不存在");
        }
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String oldDeviceToken = userVo1.getDeviceToken();
        String newDeviceToken = userVo.getDeviceToken();
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getPhone()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        if(null == oldDeviceToken || !oldDeviceToken.equals(newDeviceToken)) {
        	userVo1.setDeviceToken(newDeviceToken);
        }
        userVo1.setImUserId(imResult.get("userId") + "");
        userVo1.setLoginNum(userVo1.getLoginNum() +1);
        userVo1.setLast_login_ip(this.getClientIp());
        userVo1.setLast_login_time(new Date());
    	userService.update(userVo1);
    	
    	map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * im匿名登录
     */
    @IgnoreAuth
    @PostMapping("v1/anonymousimlogin")
    @ApiOperation(value = "登录接口")
    public R anonymousimloginV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");
        String phone = userVo.getPhone();
        if(null == phone || "".equals(phone)) {
        	return R.error(500, "phone不能为空");
        }
        String clientId = userVo.getClientId();
        if(null == clientId || "".equals(clientId)) {
        	return R.error(500, "clientId不能为空");
        }
        //生成token
        Map<String, Object> map = new HashMap<String, Object>();
        //String data = "mobile="+userVo.getPhone()+"&code=1234&clientId="+userVo.getClientId();
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+phone+"\",\"code\":\""+66666+"\",\"clientId\":\""+clientId+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        String userInfoUrl = ResourceUtil.getConfigByName("imusergetinfo");
        String userInfojson = "{\"userId\":\""+imResult.get("userId")+"\"}";
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
        String ndStr = "stranger" + phone.substring(0, 6);
        inputOutputUserInfo.setName(ndStr);
        inputOutputUserInfo.setDisplayName(ndStr);
        String userCreateUrl = ResourceUtil.getConfigByName("imusercreate");
        String userCreatejson = JSONObject.toJSONString(inputOutputUserInfo);
        //String userCreatejson = "{\"userId\":\""+userVo.getImUserId()+"\",\"name\":\""+userInfoResult.get("name")+"\",\"displayName\":\""+remarkName+"\",\"extra\":\""+remarkName+"\"}";
        logger.info("======== getIMUserFriendsV1 userCreatejson ==========" +userCreatejson);
        String imUserCreatejson = HttpUtils.HttpPostIMServerApiWithJson(userCreateUrl, userCreatejson);
        logger.info("======== getIMUserFriendsV1 imUserCreatejson ==========" +imUserCreatejson);
        
        return R.ok(200,"success",map);
    }
    
    
    
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("v1/loginByEmail")
    @ApiOperation(value = "登录接口")
    public R loginByEmailV1(@RequestBody UserVo userVo) {
    	Assert.isNull(userVo, "不能为空");

        //用户登录
        UserVo userVo1 = userService.loginByEmail(userVo.getEmail(), userVo.getPassword());
        if(null == userVo1) {
        	R.error(407, "该邮箱账号用户不存在");
        }
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String oldDeviceToken = userVo1.getDeviceToken();
        String newDeviceToken = userVo.getDeviceToken();

        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getEmail()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        if(null == oldDeviceToken || !oldDeviceToken.equals(newDeviceToken)) {
        	userVo1.setDeviceToken(newDeviceToken);
        }
        userVo1.setImUserId(imResult.get("userId") +"");
        userVo1.setLoginNum(userVo1.getLoginNum() +1);
        userVo1.setLast_login_ip(this.getClientIp());
        userVo1.setLast_login_time(new Date());
    	userService.update(userVo1);
    	
    	map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * 账号登录
     */
    @IgnoreAuth
    @PostMapping("v1/loginByUserName")
    @ApiOperation(value = "登录接口")
    public R loginByUserNameV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");

        //用户登录
        UserVo userVo1 = userService.queryByUserName(userVo.getUsername());
        if(null == userVo1) {
        	return R.error(407,"该用户不存在");
        }
        //密码错误
        if (!userVo1.getPassword().equals(DigestUtils.sha256Hex(userVo.getPassword()))) {
            throw new RRException("手机号或密码错误");
        }
        
        String oldDeviceToken = userVo1.getDeviceToken();
        String newDeviceToken = userVo.getDeviceToken();
        
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getUsername()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        if(null == oldDeviceToken || !oldDeviceToken.equals(newDeviceToken)) {
        	userVo1.setDeviceToken(newDeviceToken);
        }
        userVo1.setImUserId(imResult.get("userId") +"");
        userVo1.setLoginNum(userVo1.getLoginNum() +1);
        userVo1.setLast_login_ip(this.getClientIp());
        userVo1.setLast_login_time(new Date());
    	userService.update(userVo1);
    	
    	map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * 账号登录
     */
    @IgnoreAuth
    @PostMapping("v1/loginByFb")
    @ApiOperation(value = "登录接口")
    public R loginByFbV1(@RequestBody UserVo userVoFB) {
        Assert.isNull(userVoFB, "不能为空");
        Assert.isBlank(userVoFB.getUsername(), "不能为空");

        UserVo userVo = userService.queryByUserName(userVoFB.getUsername());
        if (null == userVo) {
            userVo = new UserVo();
            userVo.setUsername(userVoFB.getUsername());
            userVo.setEmail(userVoFB.getEmail());
            userVo.setPassword(userVoFB.getUsername() + "");
            userVo.setRegister_time(new Date());
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVoFB.getRegister_ip());
            userVo.setLast_login_time(userVoFB.getRegister_time());
            userVo.setAvatar(userVoFB.getAvatar());
            userVo.setGender(userVoFB.getGender()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(userVoFB.getNickname());
            userService.save(userVo);
        }

      //生成token
        Map<String, Object> map = tokenService.createToken(userVo.getUserId());
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getUsername()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        userVo.setImUserId(imResult.get("userId") +"");
        userVo.setLoginNum(userVo.getLoginNum() + 1);
        userVo.setLast_login_ip(this.getClientIp());
        userVo.setLast_login_time(new Date());
        userService.update(userVo);
        
        map.put("user", userVo);
        return R.ok(200,"success",map);
    }
    
    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("v1/loginByUserId")
    @ApiOperation(value = "登录接口")
    public R loginByUserIdV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");

        //用户登录
        UserVo userVo1 = userService.queryObject(userVo.getUserId());
        if(null == userVo1) {
        	return R.error(407, "该用户不存在");
        }
        //密码错误
        if (!userVo1.getPassword().equals(DigestUtils.sha256Hex(userVo.getPassword()))) {
            throw new RRException("手机号或密码错误");
        }
        
        userVo1.setLoginNum(userVo1.getLoginNum() +1);
        userVo1.setLast_login_ip(this.getClientIp());
        userVo1.setLast_login_time(new Date());
    	userService.update(userVo1);
        
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());

        return R.ok(200,"success",map);
    }
    
    /**
     * 登录
     */
    @PostMapping("v1/islogin")
    @ApiOperation(value = "登录接口")
    public R isloginV1() {
        return R.ok(200,"success","");
    }
    
}
