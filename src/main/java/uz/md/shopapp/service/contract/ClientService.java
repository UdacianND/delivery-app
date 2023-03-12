package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressAddDTO;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientDto;
import uz.md.shopapp.dtos.user.ClientMeDto;

import java.util.List;

public interface ClientService {
    ApiResult<ClientMeDto> getMe();

    ApiResult<List<OrderDTO>> getMyOrders();
    ApiResult<Void> deleteMyOrders();

    ApiResult<List<OrderDTO>> getMyOrders(String page);

    ApiResult<List<AddressDTO>> getMyAddresses();

    ApiResult<AddressDTO> addAddress(AddressAddDTO addressAddDTO);

    ApiResult<Void> delete(Long id);
}
