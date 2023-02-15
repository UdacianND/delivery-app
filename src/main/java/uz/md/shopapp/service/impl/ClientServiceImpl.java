package uz.md.shopapp.service.impl;

import org.springframework.stereotype.Service;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientDto;
import uz.md.shopapp.service.contract.ClientService;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public ApiResult<ClientDto> getMe() {
        return null;
    }

    @Override
    public ApiResult<List<OrderDTO>> getMyOrders() {
        return null;
    }

    @Override
    public ApiResult<List<AddressDTO>> getMyAddresses() {
        return null;
    }
}
