package lnyswz.jxc.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_khdd")
@DynamicUpdate(true)
public class TKhdd {
    private String khddlsh;
    private Date createTime;
    private int createId;
    private String createName;
    private String bmbh;
    private String bmmc;
    private String khbh;
    private String khmc;
    private int ywyId;
    private String ywymc;
    private String bz;
    private String isCancel;
    private Integer cancelId;
    private Date cancelTime;
    private String cancelName;
    private String isHandle;
    private Integer handleId;
    private Date handleTime;
    private String handleName;
    private String isRefuse;
    private Integer refuseId;
    private Date refuseTime;
    private String refuseName;
    private Date xsthTime;

    private Set<TKhddDet> TKhddDets = new HashSet<TKhddDet>(0);

    public TKhdd() {
    }

    public TKhdd(String khddlsh, Date createTime, int createId, String createName, String bmbh, String bmmc,
                 String khbh, String khmc, int ywyId, String ywymc, String bz, String isCancel, Integer cancelId, Date cancelTime, String cancelName,
                 String isHandle, Integer handleId, Date handleTime, String handleName, String isRefuse, Integer refuseId, Date refuseTime, String refuseName, Date xsthTime,
                 Set<TKhddDet> TKhddDets) {
        this.khddlsh = khddlsh;
        this.createTime = createTime;
        this.createId = createId;
        this.createName = createName;
        this.bmbh = bmbh;
        this.bmmc = bmmc;
        this.khbh = khbh;
        this.khmc = khmc;
        this.ywyId = ywyId;
        this.ywymc = ywymc;
        this.bz = bz;
        this.isCancel = isCancel;
        this.cancelId = cancelId;
        this.cancelTime = cancelTime;
        this.cancelName = cancelName;
        this.isHandle = isHandle;
        this.handleId = handleId;
        this.handleTime = handleTime;
        this.handleName = handleName;
        this.isRefuse = isRefuse;
        this.refuseId = refuseId;
        this.refuseTime = refuseTime;
        this.refuseName = refuseName;
        this.xsthTime = xsthTime;
        this.TKhddDets = TKhddDets;
    }

    @Id
    @Column(name = "khddlsh", unique = true, nullable = false, length = 12)
    public String getKhddlsh() {
        return this.khddlsh;
    }

    public void setKhddlsh(String khddlsh) {
        this.khddlsh = khddlsh;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", nullable = false, length = 23)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "createId", nullable = false)
    public int getCreateId() {
        return this.createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    @Column(name = "createName", nullable = false, length = 20)
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Column(name = "bmbh", nullable = false, length = 2)
    public String getBmbh() {
        return this.bmbh;
    }

    public void setBmbh(String bmbh) {
        this.bmbh = bmbh;
    }

    @Column(name = "bmmc", nullable = false, length = 20)
    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }


    @Column(name = "khbh", length = 8)
    public String getKhbh() {
        return khbh;
    }

    public void setKhbh(String khbh) {
        this.khbh = khbh;
    }

    @Column(name = "khmc", length = 100)
    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    @Column(name = "ywyId")
    public int getYwyId() {
        return ywyId;
    }

    public void setYwyId(int ywyId) {
        this.ywyId = ywyId;
    }

    @Column(name = "ywymc", nullable = false, length = 20)
    public String getYwymc() {
        return ywymc;
    }

    public void setYwymc(String ywymc) {
        this.ywymc = ywymc;
    }

    @Column(name = "bz", length = 100)
    public String getBz() {
        return this.bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Column(name = "isCancel", nullable = false, length = 1)
    public String getIsCancel() {
        return this.isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    @Column(name = "cancelId")
    public Integer getCancelId() {
        return this.cancelId;
    }

    public void setCancelId(Integer cancelId) {
        this.cancelId = cancelId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancelTime", length = 23)
    public Date getCancelTime() {
        return this.cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Column(name = "cancelName", length = 20)
    public String getCancelName() {
        return cancelName;
    }

    public void setCancelName(String cancelName) {
        this.cancelName = cancelName;
    }

    @Column(name = "isHandle", nullable = false, length = 1)
    public String getIsHandle() {
        return this.isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    @Column(name = "handleId")
    public Integer getHandleId() {
        return this.handleId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "handleTime", length = 23)
    public Date getHandleTime() {
        return this.handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    @Column(name = "handleName", length = 20)
    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    @Column(name = "isRefuse", nullable = false, length = 1)
    public String getIsRefuse() {
        return this.isRefuse;
    }

    public void setIsRefuse(String isRefuse) {
        this.isRefuse = isRefuse;
    }

    @Column(name = "refuseId")
    public Integer getRefuseId() {
        return this.refuseId;
    }

    public void setRefuseId(Integer refuseId) {
        this.refuseId = refuseId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "refuseTime", length = 23)
    public Date getRefuseTime() {
        return this.refuseTime;
    }

    public void setRefuseTime(Date refuseTime) {
        this.refuseTime = refuseTime;
    }

    @Column(name = "refuseName", length = 20)
    public String getRefuseName() {
        return refuseName;
    }

    public void setRefuseName(String refuseName) {
        this.refuseName = refuseName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "xsthTime", length = 23)
    public Date getXsthTime() {
        return this.xsthTime;
    }

    public void setXsthTime(Date xsthTime) {
        this.xsthTime = xsthTime;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "TKhdd", cascade=CascadeType.ALL)
    public Set<TKhddDet> getTKhddDets() {
        return TKhddDets;
    }

    public void setTKhddDets(Set<TKhddDet> tKhddDets) {
        TKhddDets = tKhddDets;
    }
}
