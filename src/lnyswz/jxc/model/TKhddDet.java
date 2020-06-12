package lnyswz.jxc.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_khdd_det")
@DynamicUpdate(true)
public class TKhddDet {
    private int id;
    private TKhdd TKhdd;
    private String spbh;
    private String spmc;
    private String spcd;
    private String sppp;
    private String spbz;
    private String zjldwId;
    private String zjldwmc;
    private BigDecimal zdwsl;
    private String xsthlsh;

    public TKhddDet() {
    }

    public TKhddDet(int id, TKhdd TKhdd, String spbh, String spmc, String spcd, String sppp, String spbz, String zjldwId,
                    String zjldwmc, BigDecimal zdwsl, String xsthlsh) {
        this.id = id;
        this.TKhdd = TKhdd;
        this.spbh = spbh;
        this.spmc = spmc;
        this.spcd = spcd;
        this.sppp = sppp;
        this.spbz = spbz;
        this.zjldwId = zjldwId;
        this.zjldwmc = zjldwmc;
        this.zdwsl = zdwsl;
        this.xsthlsh = xsthlsh;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khddlsh")
    public TKhdd getTKhdd() {
        return TKhdd;
    }

    public void setTKhdd(TKhdd tKhdd) {
        TKhdd = tKhdd;
    }

    @Column(name = "spbh", nullable = false, length = 7)
    public String getSpbh() {
        return spbh;
    }

    public void setSpbh(String spbh) {
        this.spbh = spbh;
    }

    @Column(name = "spmc", nullable = false, length = 100)
    public String getSpmc() {
        return spmc;
    }

    public void setSpmc(String spmc) {
        this.spmc = spmc;
    }

    @Column(name = "spcd", nullable = false, length = 20)
    public String getSpcd() {
        return spcd;
    }

    public void setSpcd(String spcd) {
        this.spcd = spcd;
    }

    @Column(name = "sppp", length = 20)
    public String getSppp() {
        return sppp;
    }

    public void setSppp(String sppp) {
        this.sppp = sppp;
    }

    @Column(name = "spbz", length = 20)
    public String getSpbz() {
        return spbz;
    }

    public void setSpbz(String spbz) {
        this.spbz = spbz;
    }

    @Column(name = "zjldwId", nullable = false, length = 2)
    public String getZjldwId() {
        return zjldwId;
    }

    public void setZjldwId(String zjldwId) {
        this.zjldwId = zjldwId;
    }

    @Column(name = "zjldwmc", nullable = false, length = 20)
    public String getZjldwmc() {
        return zjldwmc;
    }

    public void setZjldwmc(String zjldwmc) {
        this.zjldwmc = zjldwmc;
    }

    @Column(name = "zdwsl", nullable = false, precision = 18, scale = 3)
    public BigDecimal getZdwsl() {
        return this.zdwsl;
    }

    public void setZdwsl(BigDecimal zdwsl) {
        this.zdwsl = zdwsl;
    }

    @Column(name = "xsthlsh", nullable = true, length = 12)
    public String getXsthlsh() {
        return xsthlsh;
    }

    public void setXsthlsh(String xsthlsh) {
        this.xsthlsh = xsthlsh;
    }
}
