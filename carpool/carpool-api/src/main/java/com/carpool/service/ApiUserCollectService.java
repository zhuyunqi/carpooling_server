package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiUserCollectMapper;
import com.carpool.entitycarpool.UserCollectCarpoolVo;

@Service
public class ApiUserCollectService {
    @Autowired
    private ApiUserCollectMapper userCollectDao;


    public UserCollectCarpoolVo queryObject(Integer id) {
        return userCollectDao.queryObject(id);
    }

    public List<UserCollectCarpoolVo> queryList(Map<String, Object> map) {
        return userCollectDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return userCollectDao.queryTotal(map);
    }


    public void save(UserCollectCarpoolVo userCollectCarpoolVo) {
    	userCollectDao.save(userCollectCarpoolVo);
    }


    public int update(UserCollectCarpoolVo userCollectCarpoolVo) {
        return userCollectDao.update(userCollectCarpoolVo);
    }


    public void delete(Integer id) {
    	userCollectDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	userCollectDao.deleteBatch(ids);
    }

}
