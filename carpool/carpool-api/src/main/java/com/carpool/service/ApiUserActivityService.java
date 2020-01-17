package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiUserActivityMapper;
import com.carpool.entitycarpool.UserActivityCarpoolVo;

@Service
public class ApiUserActivityService {
    @Autowired
    private ApiUserActivityMapper userActivityDao;


    public UserActivityCarpoolVo queryObject(Integer id) {
        return userActivityDao.queryObject(id);
    }

    public List<UserActivityCarpoolVo> queryList(Map<String, Object> map) {
        return userActivityDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return userActivityDao.queryTotal(map);
    }


    public void save(UserActivityCarpoolVo userActivityCarpoolVo) {
    	userActivityDao.save(userActivityCarpoolVo);
    }


    public int update(UserActivityCarpoolVo userActivityCarpoolVo) {
        return userActivityDao.update(userActivityCarpoolVo);
    }


    public void delete(Integer id) {
    	userActivityDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	userActivityDao.deleteBatch(ids);
    }

}
