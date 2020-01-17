package com.carpool.entitycarpool;

import java.io.Serializable;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class UserAddressCarpoolVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    
    private Long userId;
    
    private Long addressId;

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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

}
