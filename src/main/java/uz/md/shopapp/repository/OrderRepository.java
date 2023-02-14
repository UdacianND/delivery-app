package uz.md.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.domain.enums.OrderStatus;

import java.net.ContentHandler;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByStatus(OrderStatus status,
                                Pageable pageable);

    Page<Order> findAllByUserId(UUID userid, Pageable pageable);
}
