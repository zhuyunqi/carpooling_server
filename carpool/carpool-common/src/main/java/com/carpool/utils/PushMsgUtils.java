package com.carpool.utils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

public class PushMsgUtils {
	/**
	 * 
	 * @param message
	 * @param sound
	 * @param sendCount 单发还是群发  true：单发  false：群发
	 * @param deviceTokens
	 * @throws Exception
	 */
    public static void pushMsgNotification(String message, String sound,boolean sendCount, List<String> deviceTokens) throws Exception{
            System.out.println("zsl==========开始推送消息");
            int badge = 1; // 图标小红圈的数值
            String certificatePath = ResourceUtil.getConfigByName("certificatePath");
            String msgCertificatePassword = ResourceUtil.getConfigByName("msgCertificatePassword");//导出证书时设置的密码

//          String certificatePath = requestRealPath
//                  + "/WEB-INF/classes/certificate/msg.p12";
            //java必须要用导出p12文件  php的话是pem文件
            
            PushNotificationPayload payload = new PushNotificationPayload();
            payload.addAlert(message); // 消息内容
            payload.addBadge(badge);


            //payload.addCustomAlertBody(msgEX);
            if (null == sound || "".equals(sound)) {
                payload.addSound(sound);
            }

            PushNotificationManager pushManager = new PushNotificationManager();
            // true：表示的是产品测试推送服务 false：表示的是产品发布推送服务
            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(
                    certificatePath, msgCertificatePassword, false));
            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
            // 开始推送消息
            if (sendCount) {
                Device device = new BasicDevice();
                device.setToken(deviceTokens.get(0));
                PushedNotification notification = pushManager.sendNotification(
                        device, payload, true);
                notifications.add(notification);
            } else {
                List<Device> devices = new ArrayList<Device>();
                for (String token : deviceTokens) {
                    devices.add(new BasicDevice(token));
                }
                notifications = pushManager.sendNotifications(payload, devices);
            }

            List<PushedNotification> failedNotification = PushedNotification
                    .findFailedNotifications(notifications);
            List<PushedNotification> successfulNotification = PushedNotification
                    .findSuccessfulNotifications(notifications);
            int failed = failedNotification.size();
            int successful = successfulNotification.size();
            System.out.println("zsl==========成功数：" + successful);
            System.out.println("zsl==========失败数：" + failed);
            pushManager.stopConnection();
            System.out.println("zsl==========消息推送完毕");
    }
    
    public static void main(String[] args) {
    	List<String> deviceTokenlist = new ArrayList<String>();
    	deviceTokenlist.add("77dce91572512a12732892a6cd823f3dd3a1b6eb9b84398337fffb767fd0e4e7");
    	try {
			pushMsgNotification("你有一条新的推广消息", "default",false, deviceTokenlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}