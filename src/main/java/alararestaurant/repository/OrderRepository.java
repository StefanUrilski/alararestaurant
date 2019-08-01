package alararestaurant.repository;

import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * " +
            "from orders as o " +
            "join employees as e " +
            "on o.employee_id = e.id " +
            "join positions as p " +
            "on e.position_id = p.id " +
            "where p.name like :position " +
            "group by e.name " +
            "order by e.name, o.id ", nativeQuery = true)
    List<Order> findAllByEmployeePosition(@Param("position") String position);
}
