package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import uz.md.shopapp.domain.template.AbsLongEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@Builder
@Where(clause = "deleted = false")
public class OrderProduct extends AbsLongEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    @OneToOne
    @JoinColumn(nullable = false)
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderProduct)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderProduct) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}