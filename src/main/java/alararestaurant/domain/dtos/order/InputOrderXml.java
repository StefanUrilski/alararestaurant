package alararestaurant.domain.dtos.order;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class InputOrderXml {

    @XmlElement
    private String customer;

    @XmlElement
    private String employee;

    @XmlElement(name = "date-time")
    private String dateTime;

    @XmlElement
    private String type;

    @XmlElement(name = "items")
    private ImportOrderNestedItemRootXml items;

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @NotNull
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImportOrderNestedItemRootXml getItems() {
        return items;
    }

    public void setItems(ImportOrderNestedItemRootXml items) {
        this.items = items;
    }
}
