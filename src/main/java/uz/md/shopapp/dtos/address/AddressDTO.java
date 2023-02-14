package uz.md.shopapp.dtos.address;

import lombok.*;

@AllArgsConstructor

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {
    private Long id;
    private Integer houseNumber;
    private String street;
    private String city;
    private Long userId;
}
