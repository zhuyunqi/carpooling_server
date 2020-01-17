package com.carpool.entitycarpool;

import java.io.Serializable;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class UserCollectCarpoolVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    
    private Long userId;
    
    private Long activityId;
    
    private boolean isCollect;

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

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
    
}
