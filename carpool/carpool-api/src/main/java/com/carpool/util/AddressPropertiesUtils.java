package com.carpool.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.carpool.entity.AdVo;
import com.carpool.entity.AddressVo;
import com.carpool.entitycarpool.AdCarpoolVo;
import com.carpool.entitycarpool.AddressCarpoolVo;

public class AddressPropertiesUtils  {
	private static Logger log = Logger.getLogger(AddressPropertiesUtils.class);
    
	public static AddressCarpoolVo setAddVoToAddCarpoolVo(AddressVo addVo) {
		AddressCarpoolVo addressCarpoolVo = new AddressCarpoolVo();
		addressCarpoolVo.setId(addVo.getId());
		addressCarpoolVo.setAddress(addVo.getDetailInfo());
		addressCarpoolVo.setAddressName(addVo.getAddressName());
		addressCarpoolVo.setLongitude(addVo.getLongitude());
		addressCarpoolVo.setLatitude(addVo.getLatitude());
		addressCarpoolVo.setSubject(addVo.getSubject());
		addressCarpoolVo.setUserName(addVo.getUserName());
		addressCarpoolVo.setThoroughfare(addVo.getThoroughfare());
		addressCarpoolVo.setSubThoroughfare(addVo.getSubThoroughfare());
		addressCarpoolVo.setLocality(addVo.getLocality());
		addressCarpoolVo.setSubLocality(addVo.getSubLocality());
		addressCarpoolVo.setAdministrativeArea(addVo.getAdministrativeArea());
		addressCarpoolVo.setSubAdministrativeArea(addVo.getSubAdministrativeArea());
		return addressCarpoolVo;
		
	}
	
	public static List<AddressCarpoolVo> setAddVoListToAddCarpoolVoList(List<AddressVo> addVoList) {
		if(null == addVoList || addVoList.size() <=0) {
			return null;
		}
		List<AddressCarpoolVo> addCarpoolVoList = new ArrayList<AddressCarpoolVo>();
		for(AddressVo addVo : addVoList) {
			AddressCarpoolVo addressCarpoolVo = new AddressCarpoolVo();
			addressCarpoolVo.setId(addVo.getId());
			addressCarpoolVo.setAddress(addVo.getDetailInfo());
			addressCarpoolVo.setAddressName(addVo.getAddressName());
			addressCarpoolVo.setLongitude(addVo.getLongitude());
			addressCarpoolVo.setLatitude(addVo.getLatitude());
			addressCarpoolVo.setSubject(addVo.getSubject());
			addressCarpoolVo.setUserName(addVo.getUserName());
			addressCarpoolVo.setThoroughfare(addVo.getThoroughfare());
			addressCarpoolVo.setSubThoroughfare(addVo.getSubThoroughfare());
			addressCarpoolVo.setLocality(addVo.getLocality());
			addressCarpoolVo.setSubLocality(addVo.getSubLocality());
			addressCarpoolVo.setAdministrativeArea(addVo.getAdministrativeArea());
			addressCarpoolVo.setSubAdministrativeArea(addVo.getSubAdministrativeArea());
			addCarpoolVoList.add(addressCarpoolVo);
		}
		return addCarpoolVoList;
	}
	
}