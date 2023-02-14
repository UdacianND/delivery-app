package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.dtos.address.AddressAddDTO;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.address.AddressEditDTO;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<Address, AddressDTO> {

    @Override
    @Mapping(target = "userId" , source = "user.id")
    AddressDTO toDTO(Address entity);

    Address fromAddDTO(AddressAddDTO dto);

    Address fromEditDTO(AddressEditDTO editDTO);
}
