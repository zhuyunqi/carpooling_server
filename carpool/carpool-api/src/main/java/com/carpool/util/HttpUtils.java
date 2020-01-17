package com.carpool.util;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.carpool.utils.ResourceUtil;

public class HttpUtils {
	
	public static void main(String[] args) {
//		String url = "http://13.251.222.195:8888/login";
//		
//		
//		//Post方式提交Json字符串，按照指定币种，指定时间点查询
//		String json1 = "{\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"imtype\":\"LTCUS\"}},{\"match_phrase\":{\"rtdatetime\":1521164922000}}]}}}";
//		//Post方式提交json字符串，按照指定的币种和时间正序的方式获取前N条数据
//		String json2 = "{\"query\":{\"match\":{\"imtype\":\"LTCUS\"}},\"sort\":[{\"rtdatetime\":{\"order\":\"desc\"}}],\"size\":3}";
//		//Post方式提交Json字符串，按照指定币种，指定时间段查询	
//		String json3 = "{\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"imtype\":\"LTCUS\"}},{\"range\":{\"rtdatetime\":{\"gte\":1521164922000,\"lte\":1521164932000}}}]}}}";
//		
//		String json4 = "{\"mobile\":15008083910,\"code\":1234,\"clientId\":2312313123}";
//		String json = json4;
//		
//		System.out.println(HttpUtils.HttpPostWithJson(url, json));
		
//		String url = "http://13.251.222.195:8888/login";
//        String json = "{\"mobile\":"+"\"15008083910\""+",\"code\":\"66666\",\"clientId\":"+"\"D96AB8F8-117F-42FC-9293-7168DFB6AED3\""+"}";
//        String imJsonString = HttpUtils.HttpPostWithJson(url, json);
//        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
//        Map<String, Object> imResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
//        System.out.println("======== loginV1 imString ==========" +imResult.get("userId"));
		
//		String userId = "swsEsErr";
//    	int status = 0;
//    	
//    	String url = "http://13.251.222.195:18080/admin/friend/list";
//        String json = "{\"userId\":\"swsEsErr\",\"status\":\"0\"}";
//        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
//        System.out.println("======== loginV1 imJsonString ==========" +imJsonString);
//        Map<String, Object> im = (Map<String, Object>) JSONUtils.parse(imJsonString);
//        List<String> imResult = (List<String>) JSONUtils.parse(JSONUtils.toJSONString(im.get("result")));
//        System.out.println("======== loginV1 imString ==========");
        
        String url = "http://13.251.222.195:18080/admin/user/get_info";
        String json = "{\"userId\":\""+"CFCKCKkk"+"\"}";
        System.out.println("======== getIMUserFriendsV1 json ==========" +json);
        String imJsonString = HttpUtils.HttpPostIMServerApiWithJson(url, json);
        System.out.println("======== getIMUserFriendsV1 imJsonString ==========" +imJsonString);
        Map<String, Object> imUserInfojsonMap = (Map<String, Object>) JSONUtils.parse(imJsonString);
        Map<String, Object> userInfoResult = (Map<String, Object>) JSONUtils.parse(JSONUtils.toJSONString(imUserInfojsonMap.get("result")));
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
        inputOutputUserInfo.setDisplayName("大强");
        String userCreatejson = JSONObject.toJSONString(inputOutputUserInfo);
        String userCreateUrl = "http://13.251.222.195:18080/admin/user/create";
        //String userCreatejson = "{\"userId\":\""+"CFCKCKkk"+"\",\"name\":\""+imResult.get("name")+"\",\"displayName\":\""+imResult.get("displayName")+"\",\"portrait\":\""+"http://cdn2.wildfirechat.cn/robot.png"+"\"}";
        System.out.println("======== getIMUserFriendsV1 userCreatejson ==========" +userCreatejson);
        String imUserCreatejson = HttpUtils.HttpPostIMServerApiWithJson(userCreateUrl, userCreatejson);
        System.out.println("======== getIMUserFriendsV1 imUserCreatejson ==========" +imUserCreatejson);
		
	}
	
	public static String HttpPostWithJson(String url, String json) {
		//String returnValue = "这是默认返回值，接口调用失败";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try{
			//第一步：创建HttpClient对象
		 httpClient = HttpClients.createDefault();
		 	
		 	//第二步：创建httpPost对象
	        HttpPost httpPost = new HttpPost(url);
	        
	        //第三步：给httpPost设置JSON格式的参数
	        StringEntity requestEntity = new StringEntity(json,"utf-8");
	        requestEntity.setContentEncoding("UTF-8");    	        
	        httpPost.setHeader("Content-type", "application/json");
	        httpPost.setEntity(requestEntity);
	       
	       //第四步：发送HttpPost请求，获取返回值
	       String returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
	       return returnValue;
		}
		 catch(Exception e)
		{
			 e.printStackTrace();
		}
		
		finally {
	       try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		 //第五步：处理返回值
	     return null;
	}
	
	public static String HttpPostIMServerApiWithJson(String url, String json) {
		//String returnValue = "这是默认返回值，接口调用失败";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try{
			//第一步：创建HttpClient对象
		 httpClient = HttpClients.createDefault();
		 	
		 	//第二步：创建httpPost对象
	        HttpPost httpPost = new HttpPost(url);
	        
	        //第三步：给httpPost设置JSON格式的参数
	        StringEntity requestEntity = new StringEntity(json,"utf-8");
	        requestEntity.setContentEncoding("UTF-8");    	        
	        httpPost.setHeader("Content-type", "application/json");
	        httpPost.setHeader("nonce", "76616");
	        httpPost.setHeader("timestamp", "1558350862502");
	        httpPost.addHeader("sign", "b98f9b0717f59febccf1440067a7f50d9b31bdde");
	        httpPost.setEntity(requestEntity);
	       
	       //第四步：发送HttpPost请求，获取返回值
	       String returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
	       return returnValue;
		}
		 catch(Exception e)
		{
			 e.printStackTrace();
		}
		
		finally {
	       try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		 //第五步：处理返回值
	     return null;
	}
 
}
	
