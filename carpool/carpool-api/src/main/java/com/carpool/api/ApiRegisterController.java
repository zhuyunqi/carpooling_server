package com.carpool.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.carpool.annotation.IgnoreAuth;
import com.carpool.entity.MessageEntity;
import com.carpool.entity.UserVo;
import com.carpool.service.ApiUserService;
import com.carpool.service.SysConfigService;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.HttpUtils;
import com.carpool.utils.CharUtil;
import com.carpool.utils.R;
import com.carpool.utils.ResourceUtil;
import com.carpool.utils.SendEmail;
import com.carpool.utils.ShiroUtils;
import com.carpool.utils.SmsUtil;
import com.carpool.utils.StringUtils;
import com.carpool.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 注册
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-26 17:27
 */
@Api(tags = "注册")
@RestController
@RequestMapping("/api/register")
public class ApiRegisterController extends ApiBaseAction {
	private Logger log = Logger.getLogger(ApiRegisterController.class);
    @Autowired
    private ApiUserService userService;
    
    @Autowired
    private SysConfigService sysConfigService;
    
    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("register")
    public R register(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        userService.save(mobile, password);

        return R.ok();
    }
    
    /**
     * 账号注册
     */
    @ApiOperation(value = "账号注册")
    @IgnoreAuth
    @PostMapping("v1/registerByUserName")
    public R registerByUserNameV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");
        Assert.isBlank(userVo.getUsername(), "账号不能为空");
        Assert.isBlank(userVo.getPassword(), "密码不能为空");

        UserVo userVo1 = userService.saveByUserName(userVo);
        
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getPhone()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        userVo1.setImUserId(imResult.get("userId") + "");
    	userService.update(userVo1);
    	
    	map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * 手机注册
     */
    @ApiOperation(value = "手机注册")
    @IgnoreAuth
    @PostMapping("v1/register")
    public R registerV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");
        Assert.isBlank(userVo.getPhone(), "手机号不能为空");
        Assert.isBlank(userVo.getPassword(), "密码不能为空");
        
        userVo.setMobile(userVo.getPhone());
        UserVo userVo1 = userService.saveByMobile(userVo);
        
        //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getPhone()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        userVo1.setImUserId(imResult.get("userId") + "");
    	userService.update(userVo1);
        
        map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("v1/registerByEmail")
    public R registerByEmailV1(@RequestBody UserVo userVo) {
    	Assert.isNull(userVo, "不能为空");
        Assert.isBlank(userVo.getEmail(), "邮箱号不能为空");
        Assert.isBlank(userVo.getPassword(), "密码不能为空");

        UserVo userVo1 = userService.saveByEmail(userVo);

      //生成token
        Map<String, Object> map = tokenService.createToken(userVo1.getUserId());
        
        String url = ResourceUtil.getConfigByName("imlogin");
        String json = "{\"mobile\":\""+userVo.getPhone()+"\",\"code\":\""+66666+"\",\"clientId\":\""+userVo.getClientId()+"\"}";
        System.out.println("======== loginV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== loginV1 imString ==========" +imJsonString);
        map.put("imResult", imResult);
        
        userVo1.setImUserId(imResult.get("userId") + "");
    	userService.update(userVo1);
        
        map.put("user", userVo1);
        return R.ok(200,"success",map);
    }
    
    /**
     * 设置密码(忘记密码)
     */
    @ApiOperation(value = "设置密码(忘记密码)")
    @IgnoreAuth
    @PostMapping("v1/setPassword")
    public R setPasswordV1(@RequestBody UserVo userVo) {
        Assert.isNull(userVo, "不能为空");

        UserVo user1 = userService.queryByMobile(userVo.getPhone());
        Assert.isNull(user1, "该手机号用户不存在");

        user1.setPassword(DigestUtils.sha256Hex(userVo.getPassword()));
        userService.update(user1);
        
        return R.ok(200,"success");
    }
    
    /**
     * 获取验证码
     */
    @ApiOperation(value = "获取验证码")
    @IgnoreAuth
    @RequestMapping("v1/getCode")
    //@PostMapping("v1/getCode")
    public R getCodeV1(@RequestBody Map<String, Object> jsonMap) {
    	String countryCode = jsonMap.get("countryCode") +"";
    	String phone = jsonMap.get("phone") +"";
    	
    	if(StringUtils.isNullOrEmpty(phone)) {
    		return R.error("请输入手机号");
    	}
    	//生成验证码
        String sms_code = CharUtil.getRandomNum(4);
        String msgContent = "【顺风车】"+sms_code+"，手机号授权验证码。";
        log.info("================= getCodeV1 msgContent  ==============="  +msgContent);
        try {
            /**
             * 状态,发送编号,无效号码数,成功提交数,黑名单数和消息，无论发送的号码是多少，一个发送请求只返回一个sendid，如果响应的状态不是“0”，则只有状态和消息
             */
              //log.info("================= getCodeV1 request.getCookies()  ==============="  +request.getCookies().length);
              String result = SmsUtil.sendSMSMessage(msgContent, countryCode, phone);
              log.info("================= getCodeV1 result  ==============="  +result);
              Session ss = ShiroUtils.getSession();
              ss.setAttribute("getCodeV1_" +phone, sms_code);
            
        	  
              return R.ok(200,"success","短信发送成功");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return R.error("短信发送失败");
    }
    
    /**
     * 获取验证码
     */
    @ApiOperation(value = "获取验证码")
    @IgnoreAuth
    @PostMapping("v1/getCodeByEmail")
    public R getCodeByEmailV1(@RequestBody Map<String, Object> jsonMap) {
    	String email = jsonMap.get("email") +"";
    	logger.info("==========getCodeByEmailV1 email===========" +email);
    	if(StringUtils.isNullOrEmpty(email)) {
    		return R.error("请输入邮箱地址");
    	}
    	//生成验证码
        String sms_code = CharUtil.getRandomNum(6);
        String msgContent = "【顺风车】"+sms_code+"，邮箱号授权验证码。";
        logger.info("==========getCodeByEmailV1 msgContent===========" +msgContent);
        try {
        	
        	SendEmail sendEmail = new SendEmail();
            MessageEntity messageVo = new MessageEntity();
            messageVo.setSubject("【顺风车】邮箱号授权验证码");
            messageVo.setContent(msgContent);
            messageVo.setToMailAddress(email); // 接受邮件的帐号
            sendEmail.sendEmailBySmtp(messageVo);
            System.out.println("email send success");

            Session ss = ShiroUtils.getSession();
            ss.setAttribute("getCodeV1_" +email, sms_code);
            
            return R.ok(200,"success","邮件发送成功");

        } catch (Exception e) {
        	e.printStackTrace();
        }
        return R.error("邮件发送失败");

    }
    
    public static void main(String[] args){
        try {
            SendEmail sendEmail = new SendEmail();
            MessageEntity messageVo = new MessageEntity();
            messageVo.setSubject("error message");
            messageVo.setContent("product environment get something error");
            messageVo.setToMailAddress("125101391@qq.com"); // 接受邮件的帐号
            sendEmail.sendEmailBySmtp(messageVo);
            System.out.println("email send success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("email send end1");
    }

    
    /**
     * 验证验证码
     */
    @ApiOperation(value = "验证验证码")
    @IgnoreAuth
    @PostMapping("v1/verification")
    public R verificationV1(@RequestBody Map<String, Object> jsonMap) {
    	String phone = jsonMap.get("phone") + "";
    	String code = jsonMap.get("code") + "";
    	if(StringUtils.isNullOrEmpty(phone)) {
    		return R.error("请输入手机号");
    	}
    	if(StringUtils.isNullOrEmpty(code)) {
    		return R.error("请输入验证码");
    	}
    	String sms_code = (String) request.getSession().getAttribute("getCodeV1_" + phone);
    	if (!code.equals(sms_code)) {
            return R.error("验证码错误");
        }
    	
    	return R.ok(200,"success");
    }
    
    /**
     * 验证验证码
     */
    @ApiOperation(value = "验证验证码")
    @IgnoreAuth
    @PostMapping("v1/verificationByEmail")
    public R verificationByEmailV1(@RequestBody Map<String, Object> jsonMap) {
    	String email = jsonMap.get("email") + "";
    	String code = jsonMap.get("code") + "";
    	if(StringUtils.isNullOrEmpty(email)) {
    		return R.error("请输入邮箱地址");
    	}
    	if(StringUtils.isNullOrEmpty(code)) {
    		return R.error("请输入验证码");
    	}
    	String sms_code = (String) request.getSession().getAttribute("getCodeV1_" + email);
    	if (!code.equals(sms_code)) {
            return R.error("验证码错误");
        }
    	
    	return R.ok(200,"success");
    }
    
    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("v1/getCountryCode")
    public R getCountryCodeV1() {
        Map<String, Object> map = new HashMap<String, Object>();
        
        
        List<Map<String, Object>> countryCodeList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("country", "China PR");
        map1.put("code", "+86");
        map1.put("domainCode", "CN");
        countryCodeList.add(map1);
        
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("country", "Hong Kong China");
        map2.put("code", "+852");
        map2.put("domainCode", "HK");
        countryCodeList.add(map2);
        
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("country", "America");
        map3.put("code", "+1");
        map3.put("domainCode", "US");
        countryCodeList.add(map3);
        
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("country", "Canada");
        map4.put("code", "+1");
        map4.put("domainCode", "CA");
        countryCodeList.add(map4);
        
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("country", "England");
        map5.put("code", "+44");
        map5.put("domainCode", "GB");
        countryCodeList.add(map5);
        
        
        map.put("countryCode", countryCodeList);
        
        return R.ok(200,"success", map);
    }
}
