package com.carpool.service.impl;

import com.carpool.dao.ActivityDao;
import com.carpool.dao.UserDao;
import com.carpool.entity.ActivityEntity;
import com.carpool.entity.UserEntity;
import com.carpool.service.ActivityService;
import com.carpool.utils.R;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-19 12:53:26
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;

    @Override
    public ActivityEntity queryObject(Integer id) {
        return activityDao.queryObject(id);
    }

    @Override
    public List<ActivityEntity> queryList(Map<String, Object> map) {
        return activityDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return activityDao.queryTotal(map);
    }

    @Override
    public int save(ActivityEntity activity) {
        return activityDao.save(activity);
    }

    @Override
    public int update(ActivityEntity activity) {
        return activityDao.update(activity);
    }

    @Override
    public int delete(Integer id) {
        return activityDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return activityDao.deleteBatch(ids);
    }

}
