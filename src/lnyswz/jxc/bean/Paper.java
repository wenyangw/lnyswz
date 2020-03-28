package lnyswz.jxc.bean;

/**
 * Created by Wenyang on 2017/6/29.
 */
public class Paper {
    private int id;
    private String filename;
    private String filepath;
    private String type;
    private int messageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
