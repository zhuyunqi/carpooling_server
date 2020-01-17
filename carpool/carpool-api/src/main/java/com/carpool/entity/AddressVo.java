package com.carpool.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:39
 */
public class AddressVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //会员ID
    private Long userId;
    //手机
    private String telNumber;
    //邮编
    private String postalCode;
    //地址国家码
    private String nationalCode;
    //省
    private String provinceName;
    //市
    private String cityName;
    //区
    private String countyName;
    
    //街道
    private String thoroughfare;
    
    //门牌号
    private String subThoroughfare;
    
    //城市
    private String locality;
    
    //标志性建筑
    private String subLocality;
    
    //直辖市
    private String administrativeArea;
    
    //行政区域
    private String subAdministrativeArea;
    
    
    
    //详细地址信息
    private String detailInfo;
    
    private String address;
    
    private String addressName;
    
    //经
    private BigDecimal longitude;
    //纬
    private BigDecimal latitude;

    //默认
    private Integer is_default = 0;

    private String full_region;
    
    private String subject;
    private String userName;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：会员ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：会员ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置：收货人姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：收货人姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置：手机
     */
    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    /**
     * 获取：手机
     */
    public String getTelNumber() {
        return telNumber;
    }

    /**
     * 设置：邮编
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 获取：邮编
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 设置：地址国家码
     */
    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    /**
     * 获取：地址国家码
     */
    public String getNationalCode() {
        return nationalCode;
    }

    /**
     * 设置：省
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取：省
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * 设置：市
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 获取：市
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * 设置：区
     */
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    /**
     * 获取：区
     */
    public String getCountyName() {
        return countyName;
    }

    /**
     * 设置：详细地址信息
     */
    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    /**
     * 获取：详细地址信息
     */
    public String getDetailInfo() {
        return detailInfo;
    }

    public Integer getIs_default() {
        return is_default;
    }

    public void setIs_default(Integer is_default) {
        this.is_default = is_default;
    }

    public String getFull_region() {
        if (StringUtils.isEmpty(full_region)) {
            full_region = getProvinceName() + getCityName() + getCountyName();
        }
        return full_region;
    }

    public void setFull_region(String full_region) {

        this.full_region = full_region;
    }

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude.setScale(7,BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude.setScale(7,BigDecimal.ROUND_HALF_UP);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getThoroughfare() {
		return thoroughfare;
	}

	public void setThoroughfare(String thoroughfare) {
		this.thoroughfare = thoroughfare;
	}

	public String getSubThoroughfare() {
		return subThoroughfare;
	}

	public void setSubThoroughfare(String subThoroughfare) {
		this.subThoroughfare = subThoroughfare;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getSubLocality() {
		return subLocality;
	}

	public void setSubLocality(String subLocality) {
		this.subLocality = subLocality;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getSubAdministrativeArea() {
		return subAdministrativeArea;
	}

	public void setSubAdministrativeArea(String subAdministrativeArea) {
		this.subAdministrativeArea = subAdministrativeArea;
	}
	
}
