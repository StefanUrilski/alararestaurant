package alararestaurant.domain.entities;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

    private Integer id;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
