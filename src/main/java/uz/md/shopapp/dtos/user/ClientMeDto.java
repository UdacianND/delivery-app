package uz.md.shopapp.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.shopapp.dtos.address.AddressDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientMeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<AddressDTO> addresses;
    private Long numberOfOrders;
}
