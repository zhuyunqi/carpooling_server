package com.carpool.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpool.dao.ApiContractMapper;
import com.carpool.entitycarpool.ContractCarpoolVo;

@Service
public class ApiContractService {
    @Autowired
    private ApiContractMapper contractDao;


    public ContractCarpoolVo queryObject(Long id) {
        return contractDao.queryObject(id);
    }

    public List<ContractCarpoolVo> queryList(Map<String, Object> map) {
        return contractDao.queryList(map);
    }

    public List<ContractCarpoolVo> queryListByUserIdORqyUserId(Map<String, Object> map) {
        return contractDao.queryListByUserIdORqyUserId(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return contractDao.queryTotal(map);
    }


    public int save(ContractCarpoolVo contractCarpoolVo) {
    	return contractDao.save(contractCarpoolVo);
    }


    public int update(ContractCarpoolVo contractCarpoolVo) {
        return contractDao.update(contractCarpoolVo);
    }


    public void delete(Integer id) {
    	contractDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
    	contractDao.deleteBatch(ids);
    }

}
