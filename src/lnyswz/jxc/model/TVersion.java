package lnyswz.jxc.model;

import javax.persistence.*;

@Entity
@Table(name = "t_version")
public class TVersion implements java.io.Serializable {

	private int id;
	private int versionCode;
	private String versionName;

	public TVersion() {
	}

	public TVersion(int id, int versionCode, String versionName) {
		this.versionCode = versionCode;
		this.versionName = versionName;
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


	@Column(name = "versionCode")
	public int getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	@Column(name = "versionName", nullable = false, length = 20)
	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
}
