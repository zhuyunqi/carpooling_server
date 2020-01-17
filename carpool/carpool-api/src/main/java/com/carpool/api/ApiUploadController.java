package com.carpool.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.carpool.annotation.IgnoreAuth;
import com.carpool.entity.InputOutputUserInfo;
import com.carpool.entity.UserVo;
import com.carpool.oss.OSSFactory;
import com.carpool.service.ApiUserService;
import com.carpool.util.ApiBaseAction;
import com.carpool.util.BeanMapConvert;
import com.carpool.util.HttpUtils;
import com.carpool.utils.RRException;
import com.carpool.utils.ResourceUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-09-08 13:20<br>
 * 描述: ApiUploadController <br>
 */
@Api(tags = "上传")
@RestController
@RequestMapping("/api/upload")
public class ApiUploadController extends ApiBaseAction {
	protected Logger log = Logger.getLogger(ApiUploadController.class);
	
	@Autowired
    private ApiUserService userService;
	
    /**
     * 上传文件
     */
	@ApiOperation(value = "上传文件")
    @IgnoreAuth
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = OSSFactory.build().upload(file);
        return toResponsSuccess(url);
    }
	
	/**
     * 上传文件
     */
	@ApiOperation(value = "上传文件")
    @IgnoreAuth
    @PostMapping("v1/upload")
    public Object uploadV1(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = OSSFactory.build().upload(file);
        return toResponsObject(200,"上传成功",url);
    }
	
	/**
     * 上传头像
     */
	@ApiOperation(value = "上传头像")
    @IgnoreAuth
    @PostMapping("/v1/upload/hdavatar")
    public Object hdavatarV1(byte[] bytes, Long userId) throws Exception {
        if (bytes.length <= 0) {
            throw new RRException("上传文件不能为空");
        }
        String fileName = OSSFactory.build().getPath("hdavatar") + userId + "_" + System.currentTimeMillis() + 0 + ".png";
		String url = OSSFactory.build().upload(bytes, fileName);
		log.info("======= hdavatarV1 url =======" + url);
		UserVo userVo = userService.queryObject(userId);
        userVo.setAvatar(url);
        userService.update(userVo);
        return toResponsObject(200,"上传成功",userVo);
    }
	
	/**
     * 上传头像
     */
	@ApiOperation(value = "上传头像")
    @IgnoreAuth
    @PostMapping("/v1/upload/hdavatarByFile")
    public Object hdavatarV1(@RequestParam("file") MultipartFile file, long userId) throws Exception {
		if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = OSSFactory.build().upload(file);
        UserVo userVo = userService.queryObject(userId);
        userVo.setAvatar(url);
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
        inputOutputUserInfo.setPortrait(url);
        
        String userCreateUrl = ResourceUtil.getConfigByName("imusercreate");
        String userCreatejson = JSONObject.toJSONString(inputOutputUserInfo);
        //String userCreatejson = "{\"userId\":\""+userVo.getImUserId()+"\",\"name\":\""+userInfoResult.get("name")+"\",\"displayName\":\""+userInfoResult.get("displayName")+"\",\"portrait\":\""+avatar+"\"}";
        logger.info("======== getIMUserFriendsV1 userCreatejson ==========" +userCreatejson);
        String imUserCreatejson = HttpUtils.HttpPostIMServerApiWithJson(userCreateUrl, userCreatejson);
        logger.info("======== getIMUserFriendsV1 imUserCreatejson ==========" +imUserCreatejson);
        return toResponsObject(200,"上传成功",userVo);
    }
}