package alararestaurant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "categories")
public class Category extends BaseEntityWithName {

    private Set<Item> items;

    @OneToMany(mappedBy = "category", targetEntity = Item.class)
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
