package uz.md.shopapp.dtos.institution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationDto {
    private Double latitude;
    private Double longitude;
}
