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
@ToString
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
    private Address address;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @ToString.Include
    private List<OrderProduct> orderProducts;

    private LocalDateTime deliveryTime;

    private Double deliveryPrice;

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
