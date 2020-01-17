package com.carpool.entitycarpool;

import java.io.Serializable;
import java.math.BigDecimal;

import com.carpool.entity.AddressVo;
import com.carpool.entity.UserVo;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class ContractCarpoolDTO implements Serializable {
    private Long id;
    private Long userId;
    private String noticeTag;//app用
    private int contractType;//0:短期 1:长期
    private int userType;//0:乘客  1：司机
    private String subject;
//    private BigDecimal fromLng;
//    private BigDecimal fromLat;
//    private String fromAddress;
//    private String fromAddressName;
    private AddressVo fromAddressVo;
    private Long fromAddressId;
//    private BigDecimal toLng;
//    private BigDecimal toLat;
//    private String toAddress;
//    private String toAddressName;
    private AddressVo toAddressVo;
    private Long toAddressId;
    
    
    private String beginTime;
    private String endDate;
    private String endTime;
    
    private String contractCycle;//["周一","周二","周五"]//长期合约才填，短期合约为空
    private String[] contractCyclesz;
    
    private String weekNum;
    
    private Long qyUserId;//签约用户id
    
    private String remark;
    
    private Long schedulingId;//关联的行程id
    
    //本人
    private int markValue;//0:未评价 1:五星好评 2:不太愉快
    private String evaluateRemark;//本人评价备注
    
    //对方
    private int qyMarkValue;//0:未评价 1:五星好评 2:不太愉快
    private String qyEvaluateRemark;//对方评价备注
    
    
    private int status;//0:新建状态  1:接受合约，开始进行  2:合约取消 3:合约结束(完成)
    private int ridingStatus;//乘车状态 0:未上车  1:上车   2:创建者确认已到达  3:签约者确认已到达  4:下车，到达   
    private Long onCarTimestamp; //上车时间
    
    private Long confirmArriveTimestamp; // 确认到达的时间戳
    
    private UserVo cjUserVo;
    
    private UserVo qyUserVo;
    
    private boolean isFriend;
    
    private int curUserType;//当前用户是创建者还是签约者，算出当前用户的类型

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

	public int getContractType() {
		return contractType;
	}

	public void setContractType(int contractType) {
		this.contractType = contractType;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public AddressVo getFromAddressVo() {
		return fromAddressVo;
	}

	public void setFromAddressVo(AddressVo fromAddressVo) {
		this.fromAddressVo = fromAddressVo;
	}

	public Long getFromAddressId() {
		return fromAddressId;
	}

	public void setFromAddressId(Long fromAddressId) {
		this.fromAddressId = fromAddressId;
	}

	public AddressVo getToAddressVo() {
		return toAddressVo;
	}

	public void setToAddressVo(AddressVo toAddressVo) {
		this.toAddressVo = toAddressVo;
	}

	public Long getToAddressId() {
		return toAddressId;
	}

	public void setToAddressId(Long toAddressId) {
		this.toAddressId = toAddressId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getContractCycle() {
		return contractCycle;
	}

	public void setContractCycle(String contractCycle) {
		this.contractCycle = contractCycle;
	}

	public String[] getContractCyclesz() {
		return contractCyclesz;
	}

	public void setContractCyclesz(String[] contractCyclesz) {
		this.contractCyclesz = contractCyclesz;
	}

	public Long getQyUserId() {
		return qyUserId;
	}

	public void setQyUserId(Long qyUserId) {
		this.qyUserId = qyUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getSchedulingId() {
		return schedulingId;
	}

	public void setSchedulingId(Long schedulingId) {
		this.schedulingId = schedulingId;
	}

	public int getMarkValue() {
		return markValue;
	}

	public void setMarkValue(int markValue) {
		this.markValue = markValue;
	}

	public String getEvaluateRemark() {
		return evaluateRemark;
	}

	public void setEvaluateRemark(String evaluateRemark) {
		this.evaluateRemark = evaluateRemark;
	}

	public int getQyMarkValue() {
		return qyMarkValue;
	}

	public void setQyMarkValue(int qyMarkValue) {
		this.qyMarkValue = qyMarkValue;
	}

	public String getQyEvaluateRemark() {
		return qyEvaluateRemark;
	}

	public void setQyEvaluateRemark(String qyEvaluateRemark) {
		this.qyEvaluateRemark = qyEvaluateRemark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRidingStatus() {
		return ridingStatus;
	}

	public void setRidingStatus(int ridingStatus) {
		this.ridingStatus = ridingStatus;
	}

	public Long getOnCarTimestamp() {
		return onCarTimestamp;
	}

	public void setOnCarTimestamp(Long onCarTimestamp) {
		this.onCarTimestamp = onCarTimestamp;
	}

	public UserVo getCjUserVo() {
		return cjUserVo;
	}

	public void setCjUserVo(UserVo cjUserVo) {
		this.cjUserVo = cjUserVo;
	}

	public UserVo getQyUserVo() {
		return qyUserVo;
	}

	public void setQyUserVo(UserVo qyUserVo) {
		this.qyUserVo = qyUserVo;
	}

	public boolean getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public int getCurUserType() {
		return curUserType;
	}

	public void setCurUserType(int curUserType) {
		this.curUserType = curUserType;
	}

	public String getWeekNum() {
		return weekNum;
	}

	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}

	public String getNoticeTag() {
		return noticeTag;
	}

	public void setNoticeTag(String noticeTag) {
		this.noticeTag = noticeTag;
	}

	public Long getConfirmArriveTimestamp() {
		return confirmArriveTimestamp;
	}

	public void setConfirmArriveTimestamp(Long confirmArriveTimestamp) {
		this.confirmArriveTimestamp = confirmArriveTimestamp;
	}
    
}
