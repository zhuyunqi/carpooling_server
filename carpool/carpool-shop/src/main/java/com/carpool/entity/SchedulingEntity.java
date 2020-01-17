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
public class SchedulingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

  //主键
    private Long id;
    private Long userId;
    private String userName;
    private String imUserId;
//    private BigDecimal fromLng;
//    private BigDecimal fromLat;
//    private String fromAddress;
//    private String fromAddressName;
    private AddressEntity fromAddressVo;
    private Long fromAddressId;
//    private BigDecimal toLng;
//    private BigDecimal toLat;
//    private String toAddress;
//    private String toAddressName;
    private AddressEntity toAddressVo;
    private Long toAddressId;
    
    private String schedulingCycle;//["***","***","***"]//行程多个日期
    private String weekNum;
    
    private Long arriveTime;
    private String arriveTimeS;
    //主题
    private String subject;
    
    private Long contractId;//对应的合约id
    
    //0:新建    1：已匹配    2: 使用中     3:结束
    private int status;
    
    private UserEntity userVo;//当前行程userId对应的用户，就是该行程创建者，如果产生了合约，也就是合约的接收者
    
    private UserEntity cjContractUserVo;//如果产生合约，就是合约的创建者，行程的匹配者
    private String cjContractUsername;
    private String cjContractNickname;
    private String cjContractMobile;
    private String cjContractEmail;
    
    private boolean isFriend;//标志以上两个用户是不是好友
    
    private String estTime;
    
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
	public String getImUserId() {
		return imUserId;
	}
	public void setImUserId(String imUserId) {
		this.imUserId = imUserId;
	}
	public AddressEntity getFromAddressVo() {
		return fromAddressVo;
	}
	public void setFromAddressVo(AddressEntity fromAddressVo) {
		this.fromAddressVo = fromAddressVo;
	}
	public Long getFromAddressId() {
		return fromAddressId;
	}
	public void setFromAddressId(Long fromAddressId) {
		this.fromAddressId = fromAddressId;
	}
	public AddressEntity getToAddressVo() {
		return toAddressVo;
	}
	public void setToAddressVo(AddressEntity toAddressVo) {
		this.toAddressVo = toAddressVo;
	}
	public Long getToAddressId() {
		return toAddressId;
	}
	public void setToAddressId(Long toAddressId) {
		this.toAddressId = toAddressId;
	}
	public String getSchedulingCycle() {
		return schedulingCycle;
	}
	public void setSchedulingCycle(String schedulingCycle) {
		this.schedulingCycle = schedulingCycle;
	}
	public String getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}
	
	public Long getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(Long arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getArriveTimeS() {
		return arriveTimeS;
	}
	public void setArriveTimeS(String arriveTimeS) {
		this.arriveTimeS = arriveTimeS;
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
	public UserEntity getUserVo() {
		return userVo;
	}
	public void setUserVo(UserEntity userVo) {
		this.userVo = userVo;
	}
	public UserEntity getCjContractUserVo() {
		return cjContractUserVo;
	}
	public void setCjContractUserVo(UserEntity cjContractUserVo) {
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCjContractUsername() {
		return cjContractUsername;
	}
	public void setCjContractUsername(String cjContractUsername) {
		this.cjContractUsername = cjContractUsername;
	}
	public String getCjContractNickname() {
		return cjContractNickname;
	}
	public void setCjContractNickname(String cjContractNickname) {
		this.cjContractNickname = cjContractNickname;
	}
	public String getCjContractMobile() {
		return cjContractMobile;
	}
	public void setCjContractMobile(String cjContractMobile) {
		this.cjContractMobile = cjContractMobile;
	}
	public String getCjContractEmail() {
		return cjContractEmail;
	}
	public void setCjContractEmail(String cjContractEmail) {
		this.cjContractEmail = cjContractEmail;
	}
}
