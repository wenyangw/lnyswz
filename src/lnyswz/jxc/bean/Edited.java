package lnyswz.jxc.bean;

import java.util.Date;

/**
 * Created by Wenyang on 2018/7/26.
 */
public class Edited {
    private int id;
    private int createId;
    private String createName;
    private Date createTime;
    private String lsh;
    private String fieldName;
    private String oldValue;
    private String newValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
