package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiContractWarnMapper;
import com.carpool.entitycarpool.ContractWarnCarpoolVo;

@Service
public class ApiContractWarnService {
    @Autowired
    private ApiContractWarnMapper contractWarnDao;


    public ContractWarnCarpoolVo queryObject(Integer id) {
        return contractWarnDao.queryObject(id);
    }

    public List<ContractWarnCarpoolVo> queryList(Map<String, Object> map) {
        return contractWarnDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return contractWarnDao.queryTotal(map);
    }


    public void save(ContractWarnCarpoolVo contractWarnCarpoolVo) {
    	contractWarnDao.save(contractWarnCarpoolVo);
    }


    public int update(ContractWarnCarpoolVo contractWarnCarpoolVo) {
        return contractWarnDao.update(contractWarnCarpoolVo);
    }


    public void delete(Long id) {
    	contractWarnDao.delete(id);
    }


    public void deleteBatch(Long[] ids) {
    	contractWarnDao.deleteBatch(ids);
    }

}
