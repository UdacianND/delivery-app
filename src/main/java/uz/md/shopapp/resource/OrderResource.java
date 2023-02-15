package uz.md.shopapp.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.order.OrderAddDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.service.contract.OrderService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(OrderResource.BASE_URL + "/")
@RequiredArgsConstructor
@Slf4j
public class OrderResource {

    public static final String BASE_URL = AppConstants.BASE_URL + "order";

    private final OrderService orderService;

    /**
     * Gets the order by its id
     *
     * @param id order's id
     * @return found order
     */
    @GetMapping("/by_id/{id}")
    public ApiResult<OrderDTO> getById(@PathVariable Long id) {
        log.info("Getting by id {}", id);
        return orderService.findById(id);
    }

    /**
     * Adds a new order
     *
     * @param dto for adding new order
     * @return newly added order
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<OrderDTO> add(@RequestBody @Valid OrderAddDTO dto) {
        log.info("adding order");
        log.info("Request body: {}", dto);
        return orderService.add(dto);
    }

    @Operation(description = "delete orders")
    @DeleteMapping("/delete/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("delete order by id {}", id);
        return orderService.delete(id);
    }

    @Operation(description = "get orders by page")
    @GetMapping("/by_page/{pagination}")
    public ApiResult<List<OrderDTO>> getAllByPage(@PathVariable String pagination) {
        log.info("getting all orders by pagination: {}", pagination);
        return orderService.getAllByPage(pagination);
    }


    @Operation(description = "get orders by status")
    @GetMapping("/status/{status}/{pagination}")
    @ApiResponse(description = "Getting orders by status")
    public ApiResult<List<OrderDTO>> getOrdersByStatus(@PathVariable String status, @PathVariable String pagination) {
        log.info("Getting orders by status: {}", status);
        return orderService.getOrdersByStatus(status, pagination);
    }

    @Operation(description = "Get orders by user id")
    @GetMapping("/user/{userId}/{pagination}")
    @ApiResponse(description = "Getting orders by userId")
    public ApiResult<List<OrderDTO>> getOrdersByUserId(@PathVariable UUID userId, @PathVariable String pagination) {
        log.info("getOrders by userId {} ", userId);
        return orderService.getOrdersByUserId(userId, pagination);
    }

    @Operation(description = "simple sorting request")
    @GetMapping("/sorting")
    @ApiResponse(description = "List of orders sorted")
    public ApiResult<List<OrderDTO>> getOrdersBySort(@RequestBody SimpleSortRequest request) {
        log.info("Get orders by sort request");
        log.info("Request body {}", request);
        return orderService.findAllBySort(request);
    }

}
