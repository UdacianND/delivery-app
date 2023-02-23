package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@Builder
@Where(clause = "deleted = false")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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