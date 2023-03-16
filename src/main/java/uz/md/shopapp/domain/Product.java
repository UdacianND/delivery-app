package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
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
@SQLDelete(sql = "UPDATE product SET deleted = true where id = ?")
public class Product extends AbsLongEntity {

    @Column(nullable = false, unique = true)
    private String nameUz;

    @Column(nullable = false, unique = true)
    private String nameRu;
    private String imageUrl;
    private String descriptionUz;
    private String descriptionRu;

    @Column(nullable = false)
    private Long price;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
