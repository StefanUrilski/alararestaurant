package alararestaurant.domain.dtos.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOrderRootXml {

    @XmlElement(name = "order")
    private InputOrderXml[] order;

    public InputOrderXml[] getOrder() {
        return order;
    }

    public void setOrder(InputOrderXml[] order) {
        this.order = order;
    }
}
