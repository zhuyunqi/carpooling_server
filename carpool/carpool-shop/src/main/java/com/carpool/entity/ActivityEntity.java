package com.carpool.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 实体
 * 表名 nideshop_coupon
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-19 12:53:26
 */
public class ActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

  //主键
    private Long id;
    private Long userId;//发布活动的人
    private String userName;
    private String imUserId;
    private String name;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日 HH:mm", timezone = "GMT+8")
    private Long date;
    private String dateS;
    private Integer collect;//点赞数
    private String imgUrl;
//    private String address;
//    private String addressName;
//    //经
//    private BigDecimal longitude;
//    
//    //纬
//    private BigDecimal latitude;
    
    private AddressEntity addressVo;
    
    private Long addressId;
    
    private String describe;
    
    private List<UserEntity> enrollList;
    
    private int userCount;
    
    private boolean isEnrollCurUser;//当前用户是否已报名
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	
	public String getDateS() {
		return dateS;
	}
	public void setDateS(String dateS) {
		this.dateS = dateS;
	}
	public Integer getCollect() {
		return collect;
	}
	public void setCollect(Integer collect) {
		this.collect = collect;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public List<UserEntity> getEnrollList() {
		return enrollList;
	}
	public void setEnrollList(List<UserEntity> enrollList) {
		this.enrollList = enrollList;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public boolean getIsEnrollCurUser() {
		return isEnrollCurUser;
	}
	public void setIsEnrollCurUser(boolean isEnrollCurUser) {
		this.isEnrollCurUser = isEnrollCurUser;
	}
	public AddressEntity getAddressVo() {
		return addressVo;
	}
	public void setAddressVo(AddressEntity addressVo) {
		this.addressVo = addressVo;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getImUserId() {
		return imUserId;
	}
	public void setImUserId(String imUserId) {
		this.imUserId = imUserId;
	}
}
