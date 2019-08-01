package alararestaurant.domain.dtos.item;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ImportItemJson {

    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private String category;


    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Positive
    @Column(precision = 2, nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
