package com.carpool.service.impl;

import com.carpool.dao.SchedulingDao;
import com.carpool.dao.UserDao;
import com.carpool.entity.SchedulingEntity;
import com.carpool.entity.UserEntity;
import com.carpool.service.SchedulingService;
import com.carpool.service.SchedulingService;
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
@Service("schedulingService")
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    private SchedulingDao schedulingDao;
    @Autowired
    private UserDao userDao;

    @Override
    public SchedulingEntity queryObject(Long id) {
        return schedulingDao.queryObject(id);
    }

    @Override
    public List<SchedulingEntity> queryList(Map<String, Object> map) {
        return schedulingDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return schedulingDao.queryTotal(map);
    }

    @Override
    public int save(SchedulingEntity scheduling) {
        return schedulingDao.save(scheduling);
    }

    @Override
    public int update(SchedulingEntity scheduling) {
        return schedulingDao.update(scheduling);
    }

    @Override
    public int delete(Integer id) {
        return schedulingDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return schedulingDao.deleteBatch(ids);
    }

}
