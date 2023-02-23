package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.domain.template.AbsLongEntity;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "update role SET deleted = true where id = ?")
public class Role extends AbsLongEntity {

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @CollectionTable(name = "role_permission",
            joinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private Set<PermissionEnum> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
