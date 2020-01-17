package com.carpool.entitycarpool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.carpool.entity.AddressVo;
import com.carpool.entity.UserVo;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class ActivityCarpoolVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    private Long userId;//发布活动的人
    private String imUserId;
    private String name;
    private Long date;//活动举办时间
    private Integer collect;//点赞数
    private String imgUrl;
//    private String address;
//    private String addressName;
//    //经
//    private BigDecimal longitude;
//    
//    //纬
//    private BigDecimal latitude;
    private Date createTime;
    private Date updateTime;
    
    private Long addressId;
    
    private String describe;
 
    private AddressVo addressVo;
    
    private List<UserVo> enrollList;
    
    private int userCount;
    
    private boolean isEnrollCurUser;//当前用户是否已报名
    
    private boolean isEdit;//是否是当前用户发布
	
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
	
	public List<UserVo> getEnrollList() {
		return enrollList;
	}
	public void setEnrollList(List<UserVo> enrollList) {
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
	public AddressVo getAddressVo() {
		return addressVo;
	}
	public void setAddressVo(AddressVo addressVo) {
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
	public boolean getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
