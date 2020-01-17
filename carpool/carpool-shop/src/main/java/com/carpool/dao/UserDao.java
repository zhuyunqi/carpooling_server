package com.carpool.dao;

import java.util.List;

import com.carpool.entity.UserEntity;

/**
 * 会员Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 15:02:28
 */
public interface UserDao extends BaseDao<UserEntity> {

	List<UserEntity> queryListByActId(Long activityId);

}
