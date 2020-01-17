package com.carpool.entitycarpool;

import java.io.Serializable;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class UserFriendDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    
    private String userName;
    private String fuserName;
    
    private Long userId;
    private Long fuserId;
    private String fremarkName;

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

	public Long getFuserId() {
		return fuserId;
	}

	public void setFuserId(Long fuserId) {
		this.fuserId = fuserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFuserName() {
		return fuserName;
	}

	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}

	public String getFremarkName() {
		return fremarkName;
	}

	public void setFremarkName(String fremarkName) {
		this.fremarkName = fremarkName;
	}
}
