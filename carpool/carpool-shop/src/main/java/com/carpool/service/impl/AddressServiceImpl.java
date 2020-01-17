package com.carpool.service.impl;

import com.carpool.dao.AddressDao;
import com.carpool.entity.AddressEntity;
import com.carpool.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 17:22:46
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public AddressEntity queryObject(Long id) {
        return addressDao.queryObject(id);
    }

    @Override
    public List<AddressEntity> queryList(Map<String, Object> map) {
        List<AddressEntity> list = addressDao.queryList(map);
        if (null != list && list.size() > 0) {
            for (AddressEntity addressEntity : list) {
                //翻译地址国家码

            }
        }
        return list;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return addressDao.queryTotal(map);
    }

    @Override
    public int save(AddressEntity address) {
        return addressDao.save(address);
    }

    @Override
    public int update(AddressEntity address) {
        return addressDao.update(address);
    }

    @Override
    public int delete(Integer id) {
        return addressDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return addressDao.deleteBatch(ids);
    }
}
