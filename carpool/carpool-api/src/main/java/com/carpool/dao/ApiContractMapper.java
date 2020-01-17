package com.carpool.dao;

import java.util.List;
import java.util.Map;

import com.carpool.entitycarpool.ContractCarpoolVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiContractMapper extends BaseDao<ContractCarpoolVo> {
	
	List<ContractCarpoolVo> queryListByUserIdORqyUserId(Map<String, Object> map);
	
}
