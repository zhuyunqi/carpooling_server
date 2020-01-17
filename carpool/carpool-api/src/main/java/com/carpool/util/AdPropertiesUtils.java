package com.carpool.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.carpool.entity.AdVo;
import com.carpool.entitycarpool.AdCarpoolVo;

public class AdPropertiesUtils  {
	private static Logger log = Logger.getLogger(AdPropertiesUtils.class);
    
	public static AdCarpoolVo setAdVoToAdCarpoolVo(AdVo adVo) {
		AdCarpoolVo adCarpoolVo = new AdCarpoolVo();
		adCarpoolVo.setId(adVo.getId());
		adCarpoolVo.setImgUrl(adVo.getImage_url());
		adCarpoolVo.setUrl(adVo.getLink());
		adCarpoolVo.setContent(adVo.getContent());
		adCarpoolVo.setCreateTime(adVo.getCreate_time());
		return adCarpoolVo;
		
	}
	
	public static List<AdCarpoolVo> setAdVoListToAdCarpoolVoList(List<AdVo> adVoList) {
		if(null == adVoList || adVoList.size() <=0) {
			return null;
		}
		List<AdCarpoolVo> adCarpoolVoList = new ArrayList<AdCarpoolVo>();
		for(AdVo adVo : adVoList) {
			AdCarpoolVo adCarpoolVo = new AdCarpoolVo();
			adCarpoolVo.setId(adVo.getId());
			adCarpoolVo.setImgUrl(adVo.getImage_url());
			adCarpoolVo.setUrl(adVo.getLink());
			adCarpoolVo.setContent(adVo.getContent());
			adCarpoolVo.setCreateTime(adVo.getCreate_time());
			adCarpoolVoList.add(adCarpoolVo);
		}
		return adCarpoolVoList;
	}
	
}