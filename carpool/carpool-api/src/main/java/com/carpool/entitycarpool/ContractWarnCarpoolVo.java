package com.carpool.entitycarpool;

import java.io.Serializable;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class ContractWarnCarpoolVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    private Long userId;
    private Long contractId;
    private String noticeTag;
//    private boolean isWarn;
//    private String warnString;
//    private Long warnTime;
//    
//    private String canSelect;//app用，后台不管
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

//	public Long getWarnTime() {
//		return warnTime;
//	}
//
//	public void setWarnTime(Long warnTime) {
//		this.warnTime = warnTime;
//	}
//
//	public boolean getIsWarn() {
//		return isWarn;
//	}
//
//	public void setIsWarn(boolean isWarn) {
//		this.isWarn = isWarn;
//	}
//
//	public String getWarnString() {
//		return warnString;
//	}
//
//	public void setWarnString(String warnString) {
//		this.warnString = warnString;
//	}
//	
//	public String getCanSelect() {
//		return canSelect;
//	}
//
//	public void setCanSelect(String canSelect) {
//		this.canSelect = canSelect;
//	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNoticeTag() {
		return noticeTag;
	}

	public void setNoticeTag(String noticeTag) {
		this.noticeTag = noticeTag;
	}
	
}
