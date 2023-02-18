package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import uz.md.shopapp.domain.template.AbsLongEntity;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE institution SET deleted = true where id = ?")
public class Institution extends AbsLongEntity {

    @Column(nullable = false, unique = true)
    private String nameUz;
    @Column(nullable = false, unique = true)
    private String nameRu;
    private String descriptionUz;
    private String descriptionRu;
    private String imageUrl;

    @OneToOne
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    private InstitutionType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institution", fetch = FetchType.EAGER)
    private List<Category> categories;

    @OneToOne
    private User manager;

    public Institution(String nameUz, String nameRu, String descriptionUz, String descriptionRu, InstitutionType type, User manager) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.descriptionUz = descriptionUz;
        this.descriptionRu = descriptionRu;
        this.type = type;
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Institution)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Institution) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
