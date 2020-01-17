package com.carpool.dao;

import java.util.List;
import java.util.Map;

import com.carpool.entitycarpool.ActivityCarpoolVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiActivityMapper extends BaseDao<ActivityCarpoolVo> {
	List<ActivityCarpoolVo> queryEnrollList(Map<String, Object> map);
	
	int queryEnrollTotal(Map<String, Object> map);
}
