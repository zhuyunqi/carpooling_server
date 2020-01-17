package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiActivityMapper;
import com.carpool.entitycarpool.ActivityCarpoolVo;

@Service
public class ApiActivityService {
    @Autowired
    private ApiActivityMapper activityDao;


    public ActivityCarpoolVo queryObject(Long id) {
        return activityDao.queryObject(id);
    }

    public List<ActivityCarpoolVo> queryList(Map<String, Object> map) {
        return activityDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return activityDao.queryTotal(map);
    }

    public List<ActivityCarpoolVo> queryEnrollList(Map<String, Object> map) {
        return activityDao.queryEnrollList(map);
    }
    
    public int queryEnrollTotal(Map<String, Object> map) {
        return activityDao.queryEnrollTotal(map);
    }

    public void save(ActivityCarpoolVo activityCarpoolVo) {
    	activityDao.save(activityCarpoolVo);
    }


    public int update(ActivityCarpoolVo activityCarpoolVo) {
        return activityDao.update(activityCarpoolVo);
    }


    public void delete(Integer id) {
    	activityDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	activityDao.deleteBatch(ids);
    }

}
