package com.carpool.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.carpool.dao.GoodsAttributeDao;
import com.carpool.entity.GoodsAttributeEntity;
import com.carpool.service.GoodsAttributeService;


@Service("goodsAttributeService")
public class GoodsAttributeServiceImpl implements GoodsAttributeService {
	@Autowired
	private GoodsAttributeDao goodsAttributeDao;
	
	@Override
	public GoodsAttributeEntity queryObject(Integer id){
		return goodsAttributeDao.queryObject(id);
	}
	
	@Override
	public List<GoodsAttributeEntity> queryList(Map<String, Object> map){
		return goodsAttributeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return goodsAttributeDao.queryTotal(map);
	}
	
	@Override
	public void save(GoodsAttributeEntity goodsAttribute){
		goodsAttributeDao.save(goodsAttribute);
	}
	
	@Override
	public void update(GoodsAttributeEntity goodsAttribute){
		goodsAttributeDao.update(goodsAttribute);
	}
	
	@Override
	public void delete(Integer id){
		goodsAttributeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		goodsAttributeDao.deleteBatch(ids);
	}
	
}
