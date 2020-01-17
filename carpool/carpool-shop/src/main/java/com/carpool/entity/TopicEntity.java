package com.carpool.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实体
 * 表名 nideshop_topic
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-20 14:10:08
 */
public class TopicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //主题
    private String title;
    //子标题
    private String subtitle;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：活动主题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取：活动主题
     */
    public String getTitle() {
        return title;
    }

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

}
