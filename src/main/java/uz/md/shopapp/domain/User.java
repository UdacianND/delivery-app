package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.md.shopapp.domain.template.AbsLongEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Builder
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true where id = ?")
public class User extends AbsLongEntity implements UserDetails {

    private String firstName;

    private String lastName;

    private Long chatId;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    // smsCode if user is client
    private String password;

    // we check if user is client we don't check if user is not client
    private LocalDateTime codeValidTill;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    @ToString.Include
    private List<Address> addresses;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public User(String firstName, String lastName, String phoneNumber, String password, Role role) {
        super.setActive(true);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    public User(String firstName, String lastName, String phoneNumber, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return super.getId() != null && super.getId().equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(this.role.getPermissions());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
