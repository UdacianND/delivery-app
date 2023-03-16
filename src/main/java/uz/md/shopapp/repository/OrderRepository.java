package uz.md.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.ApiResult;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByStatus(OrderStatus status,
                                Pageable pageable);

    Page<Order> findAllByUserId(Long user_id, Pageable pageable);

    List<Order> findAllByUserId(Long user_id);

    Long countAllByUser_IdAndDeletedIsFalse(Long user_id);

    void deleteAllByUserId(Long id);
}
