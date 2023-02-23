package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private User user;

    private Integer houseNumber;
    private String street;
    private String city;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Location location;

    public Address(User user, Integer houseNumber, String street, String city) {
        this.user = user;
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
    }
}
