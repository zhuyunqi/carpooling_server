package com.carpool.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.carpool.entitycarpool.ContractWarnCarpoolVo;
import com.carpool.service.ApiContractWarnService;
import com.carpool.service.ApiUserService;
import com.carpool.utils.PushMsgUtils;

/**
 * 测试定时任务(演示Demo，可删除)
 * <p>
 * testTask为spring bean的名称
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年11月30日 下午1:34:24
 */
//@Component("contractWarnTask")
public class ContractWarnTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiContractWarnService contractWarnService;
    @Autowired
    private ApiUserService userService;

    public void contractWarn() {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	   Date now = new Date();
 	   System.out.println("当前时间：" + sdf.format(now));
 	   Map<String, Object> map = new HashMap<String, Object>();
 	   map.put("isWarn", 1);
 	   System.out.println("nowTime：" +now.getTime());
 	   map.put("nowTime", now.getTime());
 	   map.put("afterTime", now.getTime() + 300000);
 	   System.out.println("afterTime：" + (now.getTime() + 300000));
 	   List<ContractWarnCarpoolVo> cwcVoList = contractWarnService.queryList(map);
 	   List<String> dtList = new ArrayList<String>();
 	   for(ContractWarnCarpoolVo cwcVo : cwcVoList) {
 		   String dt = userService.queryObject(cwcVo.getUserId()).getDeviceToken();
 		   if(null != dt && !"".equals(dt)) {
 			  dtList.add(dt);
 		   }
 		   
// 		   Timer timer = new Timer();
// 	 	   timer.schedule(new ContractWarnTimerTask(timer, dtList), cwcVo.getWarnTime());
 	   }
 	   
 	   if(null != dtList && dtList.size() > 0) {
 		   System.out.println("您有一个合约时间既将开始，请准备好行礼物品！");
 		   try {
 				PushMsgUtils.pushMsgNotification("您有一个合约时间既将开始，请准备好行礼物品！", "default", false, dtList);
 		   } catch (Exception e) {
 			    e.printStackTrace();
 		   }
 	   }
 	   
	 	   
    }
}
