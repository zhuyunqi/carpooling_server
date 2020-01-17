package com.carpool.entitycarpool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.carpool.entity.AddressVo;
import com.carpool.entity.UserVo;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
public class SchedulingCarpoolVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    //主键
    private Long id;
    private Long userId;
    private String imUserId;
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
    
    private String schedulingCycle;//["***","***","***"]//行程多个日期
    private String weekNum;
    
    private Long arriveTime;
    //主题
    private String subject;
    
    private Long contractId;//对应的合约id
    
    //0:新建    1：已匹配    2: 使用中     3:结束
    private int status;
    
    private UserVo userVo;//当前行程userId对应的用户，如果产生了合约，也就是合约的接收者
    
    private UserVo cjContractUserVo;//如果产生合约，就是合约的创建者
    
    private boolean isFriend;//标志以上两个用户是不是好友
    
    private String estTime;//两个地点间的，预估到达时间 app用
    
    private int userType;//0:乘客  1：司机  标记当前该创建用户在合约里的类型
    
    private Date createTime;

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

	public Long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public UserVo getCjContractUserVo() {
		return cjContractUserVo;
	}

	public void setCjContractUserVo(UserVo cjContractUserVo) {
		this.cjContractUserVo = cjContractUserVo;
	}

	public boolean getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public String getEstTime() {
		return estTime;
	}

	public void setEstTime(String estTime) {
		this.estTime = estTime;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getSchedulingCycle() {
		return schedulingCycle;
	}

	public void setSchedulingCycle(String schedulingCycle) {
		this.schedulingCycle = schedulingCycle;
	}

	public String getImUserId() {
		return imUserId;
	}

	public void setImUserId(String imUserId) {
		this.imUserId = imUserId;
	}

	public String getWeekNum() {
		return weekNum;
	}

	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
