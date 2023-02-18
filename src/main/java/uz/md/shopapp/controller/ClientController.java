package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientDto;
import uz.md.shopapp.service.contract.ClientService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(ClientController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Client", description = "Endpoints for Client")
@Slf4j
public class ClientController {

    public static final String BASE_URL = AppConstants.BASE_URL + "client";
    private final ClientService clientService;

    @GetMapping("/me")
    @Operation(description = "Getting client")
    public ApiResult<ClientDto> getMe(){
        return clientService.getMe();
    }

    @GetMapping("/my-orders")
    @Operation(description = "Getting client orders")
    public ApiResult<List<OrderDTO>> getClientOrders(){
        return clientService.getMyOrders();
    }

    @GetMapping("/my-addresses")
    @Operation(description = "Getting client addresses")
    public ApiResult<List<AddressDTO>> getClientAddresses(){
        return clientService.getMyAddresses();
    }

}
