package lnyswz.jxc.model;

import javax.persistence.*;
import java.util.Date;

/**
 * TCk generated by hbm2java
 */
@Entity
@Table(name = "t_conference")
public class TConference implements java.io.Serializable {

	private int id;
	private String text;
	private String valid;
	private Date createTime;

	public TConference() {
	}

	public TConference(int id, String text, String valid, Date createTime) {
		this.id = id;
		this.text = text;
		this.valid = valid;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(name = "text", nullable = true, length = 2000)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	@Column(name = "valid", nullable = true, length = 1)
	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
