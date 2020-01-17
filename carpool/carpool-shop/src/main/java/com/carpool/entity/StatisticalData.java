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
public class StatisticalData{
	private static final long serialVersionUID = 1L;
	
	private int userSum;
	private int actSum;
	private int userSumToday;
	private int actSumToday;
	public int getUserSum() {
		return userSum;
	}
	public void setUserSum(int userSum) {
		this.userSum = userSum;
	}
	public int getActSum() {
		return actSum;
	}
	public void setActSum(int actSum) {
		this.actSum = actSum;
	}
	public int getUserSumToday() {
		return userSumToday;
	}
	public void setUserSumToday(int userSumToday) {
		this.userSumToday = userSumToday;
	}
	public int getActSumToday() {
		return actSumToday;
	}
	public void setActSumToday(int actSumToday) {
		this.actSumToday = actSumToday;
	}
	
}
