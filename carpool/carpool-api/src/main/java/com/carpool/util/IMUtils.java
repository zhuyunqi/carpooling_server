package com.carpool.util;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.carpool.entity.InputOutputUserInfo;
import com.carpool.entity.UserVo;
import com.carpool.utils.ResourceUtil;

public class IMUtils {
	
	public static void main(String[] args) {
		
	}
	
	public static boolean isFriend(String imUserId, String fimUserId) {
		String url = ResourceUtil.getConfigByName("imfriendlist");
        String json = "{\"userId\":\""+imUserId+"\",\"status\":\""+0+"\"}";
        System.out.println("======== getIMUserFriendsV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
        System.out.println("======== getIMUserFriendsV1 imJsonString ==========" +imJsonString);
        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
        List<String> imResult = (List<String>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
        System.out.println("======== getIMUserFriendsV1 imString ==========" +imResult);
        if(null != imResult && imResult.size() > 0) {
        	for(String imUserIdi : imResult) {
        		if(fimUserId.equals(imUserIdi)) {
        			return true;
        		}
        	}
        }
        return false;
	}
 
}
	
