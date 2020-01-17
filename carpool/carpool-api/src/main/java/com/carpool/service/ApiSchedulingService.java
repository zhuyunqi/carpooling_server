package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiSchedulingMapper;
import com.carpool.entitycarpool.SchedulingCarpoolVo;

@Service
public class ApiSchedulingService {
    @Autowired
    private ApiSchedulingMapper schedulingDao;


    public SchedulingCarpoolVo queryObject(Long id) {
        return schedulingDao.queryObject(id);
    }

    public List<SchedulingCarpoolVo> queryList(Map<String, Object> map) {
        return schedulingDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return schedulingDao.queryTotal(map);
    }


    public void save(SchedulingCarpoolVo schedulingCarpoolVo) {
    	schedulingDao.save(schedulingCarpoolVo);
    }


    public int update(SchedulingCarpoolVo schedulingCarpoolVo) {
        return schedulingDao.update(schedulingCarpoolVo);
    }


    public void delete(Long id) {
    	schedulingDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	schedulingDao.deleteBatch(ids);
    }

}
