
package lnyswz.jxc.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "lg2pr", namespace = "http://service.jxc.lnyswz/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lg2pr", namespace = "http://service.jxc.lnyswz/")
public class Lg2pr {

    @XmlElement(name = "xml", namespace = "")
    private String xml;

    /**
     * 
     * @return
     *     returns String
     */
    public String getXml() {
        return this.xml;
    }

    /**
     * 
     * @param xml
     *     the value for the xml property
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

}
