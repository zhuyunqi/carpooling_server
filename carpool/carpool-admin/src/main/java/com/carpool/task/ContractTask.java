package com.carpool.task;

import com.carpool.dao.SysContractCall;
import com.carpool.entity.SysUserEntity;
import com.carpool.service.SysUserService;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * contractTask为spring bean的名称
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年11月30日 下午1:34:24
 */
@Component("contractTask")
public class ContractTask {
    private Logger logger = LoggerFactory.getLogger(ContractTask.class);

    @Autowired
    private SysContractCall sysContractCall;
    
    public void test(String params) {
        logger.info("我是带参数的test方法，正在被执行，参数为：" + params);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //SysUserEntity user = sysUserService.queryObject(1L);
        //System.out.println(ToStringBuilder.reflectionToString(user));

    }

    public void test2() {
    	logger.info("=============== 设置短期合约过期开始 ==================");
    	sysContractCall.condqgq();
    	logger.info("=============== 设置短期合约过期结束 ==================");
    	logger.info("=============== 设置长期合约过期开始 ==================");
    	sysContractCall.concqgq();
    	logger.info("=============== 设置长期合约过期结束 ==================");
    }
}
