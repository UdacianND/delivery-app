package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressAddDTO;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientMeDto;
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
    public ApiResult<ClientMeDto> getMe() {
        return clientService.getMe();
    }

    @GetMapping("/my-orders")
    @Operation(description = "Getting client orders")
    public ApiResult<List<OrderDTO>> getClientOrders() {
        return clientService.getMyOrders();
    }

    @GetMapping("/my-orders/{page}")
    @Operation(description = "Getting client orders")
    public ApiResult<List<OrderDTO>> getClientOrdersByPage(@PathVariable String page) {
        return clientService.getMyOrders(page);
    }

    @GetMapping("/my-orders/clear")
    @Operation(description = "Getting client orders")
    public ApiResult<Void> clearClientOrders() {
        return clientService.deleteMyOrders();
    }

    @GetMapping("/my-addresses")
    @Operation(description = "Getting client addresses")
    public ApiResult<List<AddressDTO>> getClientAddresses() {
        return clientService.getMyAddresses();
    }

    @PostMapping("/address/add")
    @Operation(description = "Adds a new address")
    public ApiResult<AddressDTO> addNewAddress(@RequestBody @Valid AddressAddDTO addressAddDTO) {
        return clientService.addAddress(addressAddDTO);
    }

    @PostMapping("/address/delete/{id}")
    @Operation(description = "deleted an address")
    public ApiResult<Void> deleteAddress(@PathVariable Long id) {
        return clientService.delete(id);
    }


}
