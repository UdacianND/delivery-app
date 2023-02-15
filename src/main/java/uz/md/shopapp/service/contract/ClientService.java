package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientDto;

import java.util.List;

public interface ClientService {
    ApiResult<ClientDto> getMe();

    ApiResult<List<OrderDTO>> getMyOrders();

    ApiResult<List<AddressDTO>> getMyAddresses();
}
