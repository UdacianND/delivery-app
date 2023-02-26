package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.LastModifiedBy;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.domain.template.AbsLongEntity;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE orders SET deleted = true where id = ?")
public class Order extends AbsLongEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.PREPARING;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Location location;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderProduct> orderProducts;

    @Column(nullable = false)
    private LocalDateTime deliveryTime;

    @Column(nullable = false)
    private Double deliveryPrice;

    @Column(nullable = false)
    private Double overallPrice;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
