package com.carpool.dao;

import com.carpool.entitycarpool.UserAddressCarpoolVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiUserAddressMapper extends BaseDao<UserAddressCarpoolVo> {
	
	int deleteByUidAid(Long userId, Long addressId);
}
