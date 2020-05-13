package lnyswz.jxc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * THw generated by hbm2java
 */
@Entity
@Table(name = "t_kh_user")
public class TKhUser implements java.io.Serializable {

	private int id;
	private String userName;
	private String realName;
	private String phone;
	private String openId;
	private String khbh;
	private String dwmc;
	private int ywyId;
	private Date createTime;
	private Date lastTime;
	private String isValid;

	public TKhUser() {
	}

	public TKhUser(int id, String userName, String realName, String phone, String openId, String khbh, String dwmc, int ywyId, Date createTime, Date lastTime, String isValid ) {
		this.id = id;
		this.userName = userName;
		this.realName = realName;
		this.phone = phone;
		this.openId = openId;
		this.khbh = khbh;
		this.dwmc = dwmc;
		this.ywyId = ywyId;
		this.createTime = createTime;
		this.lastTime = lastTime;
		this.isValid = isValid;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "userName", unique = true, length = 20)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "realName", unique = false, nullable = false, length = 20)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Column(name = "phone", unique = true, nullable = false, length = 20)
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	@Column(name = "openId", unique = true, nullable = false, length = 20)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "khbh", unique = false, length = 8)
	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	@Column(name = "dwmc", unique = false, nullable = false, length = 50)
	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	
	@Column(name = "ywyId", unique = false)
	public int getYwyId() {
		return ywyId;
	}

	public void setYwyId(int ywyId) {
		this.ywyId = ywyId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastTime", length = 23)
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "isValid", unique = false, nullable = false, length = 1)
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	

}
