package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiUserAddressMapper;
import com.carpool.entitycarpool.UserAddressCarpoolVo;

@Service
public class ApiUserAddressService {
    @Autowired
    private ApiUserAddressMapper userAddressDao;


    public UserAddressCarpoolVo queryObject(Integer id) {
        return userAddressDao.queryObject(id);
    }

    public List<UserAddressCarpoolVo> queryList(Map<String, Object> map) {
        return userAddressDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return userAddressDao.queryTotal(map);
    }


    public void save(UserAddressCarpoolVo userAddressCarpoolVo) {
    	userAddressDao.save(userAddressCarpoolVo);
    }


    public int update(UserAddressCarpoolVo userAddressCarpoolVo) {
        return userAddressDao.update(userAddressCarpoolVo);
    }


    public void delete(Long id) {
    	userAddressDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	userAddressDao.deleteBatch(ids);
    }
    
    public void deleteByUidAid(Long userId, Long addressId) {
    	userAddressDao.deleteByUidAid(userId, addressId);
    }

}
