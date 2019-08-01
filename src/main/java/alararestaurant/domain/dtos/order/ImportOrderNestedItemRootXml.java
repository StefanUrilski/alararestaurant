package alararestaurant.domain.dtos.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOrderNestedItemRootXml {

    @XmlElement(name = "item")
    private ImportOrderNestedItemXml[] items;

    public ImportOrderNestedItemXml[] getItems() {
        return items;
    }

    public void setItems(ImportOrderNestedItemXml[] items) {
        this.items = items;
    }
}
