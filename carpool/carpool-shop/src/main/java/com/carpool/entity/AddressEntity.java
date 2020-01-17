package com.carpool.entity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 实体
 * 表名 nideshop_address
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-10-02 14:11:24
 */
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = 1L;

private Long id;
    
    //经
    private BigDecimal longitude;
    
    //纬
    private BigDecimal latitude;
    
    private String address;
    
    private String addressName;
    
    private String subject;
    
    private String userName;
    
  //街道
    private String thoroughfare;
    
    //门牌号
    private String subThoroughfare;
    
    //城市
    private String locality;
    
    //标志性建筑
    private String subLocality;
    
    //直辖市
    private String administrativeArea;
    
    //行政区域
    private String subAdministrativeArea;
    
    private boolean isCollect;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getThoroughfare() {
		return thoroughfare;
	}

	public void setThoroughfare(String thoroughfare) {
		this.thoroughfare = thoroughfare;
	}

	public String getSubThoroughfare() {
		return subThoroughfare;
	}

	public void setSubThoroughfare(String subThoroughfare) {
		this.subThoroughfare = subThoroughfare;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getSubLocality() {
		return subLocality;
	}

	public void setSubLocality(String subLocality) {
		this.subLocality = subLocality;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getSubAdministrativeArea() {
		return subAdministrativeArea;
	}

	public void setSubAdministrativeArea(String subAdministrativeArea) {
		this.subAdministrativeArea = subAdministrativeArea;
	}

	public boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
	
}
