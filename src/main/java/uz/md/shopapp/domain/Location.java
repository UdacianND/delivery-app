package uz.md.shopapp.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
