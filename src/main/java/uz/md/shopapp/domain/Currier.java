package uz.md.shopapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
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
@SQLDelete(sql = "UPDATE currier SET deleted = true where id = ?")
public class Currier extends AbsLongEntity {

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;
    private String carNumber;
    private boolean online;

}
