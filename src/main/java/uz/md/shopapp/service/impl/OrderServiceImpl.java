package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.order.OrderAddDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.order.OrderProductAddDTO;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.IllegalRequestException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.AddressMapper;
import uz.md.shopapp.mapper.OrderMapper;
import uz.md.shopapp.mapper.OrderProductMapper;
import uz.md.shopapp.repository.*;
import uz.md.shopapp.service.QueryService;
import uz.md.shopapp.service.contract.OrderService;
import uz.md.shopapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final QueryService queryService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    /**
     * getting by id
     *
     * @param id order's id
     * @return the order
     */
    private Order getById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw NotFoundException.builder()
                            .messageUz("ORDER_NOT_FOUND_WITH_ID" + id)
                            .messageRu("")
                            .build();
                });
    }

    @Override
    public ApiResult<OrderDTO> findById(Long id) {
        return ApiResult.successResponse(
                orderMapper.toDTO(getById(id)));
    }


    @Override
    public ApiResult<OrderDTO> add(OrderAddDTO dto) {

        Order order = orderMapper.fromAddDTO(dto);
        String currentUserPhoneNumber = CommonUtils.getCurrentUserPhoneNumber();

        User user = userRepository
                .findByPhoneNumber(currentUserPhoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("USER_NOT_FOUND")
                        .messageRu("")
                        .build());
        order.setUser(user);
        order.setActive(true);
        order.setDeleted(false);
        orderRepository.save(order);
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductAddDTO addDTO : dto.getOrderProducts()) {
            OrderProduct orderProduct = orderProductMapper.fromAddDTO(addDTO);
            orderProduct.setOrder(order);
            Product product = productRepository
                    .findById(addDTO.getProductId())
                    .orElseThrow(() -> NotFoundException.builder()
                            .messageUz("ORDER_PRODUCT_NOT_FOUND")
                            .messageRu("")
                            .build());
            orderProduct.setProduct(product);
            orderProduct.setPrice(product.getPrice() * addDTO.getQuantity());
            orderProductRepository.save(orderProduct);
            orderProducts.add(orderProduct);
        }

        double overallPrice = sumOrderOverallPrice(orderProducts);
        order.setOverallPrice(overallPrice);
        order.setOrderProducts(orderProducts);

        return ApiResult
                .successResponse(orderMapper
                        .toDTO(order));
    }

    private double sumOrderOverallPrice(List<OrderProduct> orderProducts) {
        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getPrice();
        }
        return totalPrice;
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (!orderRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageUz("ORDER_DOES_NOT_EXIST")
                    .messageRu("")
                    .build();
        orderRepository.deleteById(id);
        return ApiResult.successResponse();
    }


    @Override
    public ApiResult<List<OrderDTO>> getAllByPage(String pagination) {
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult.successResponse(
                orderMapper.toDTOList(orderRepository
                        .findAll(PageRequest.of(page[0], page[1])).getContent()));
    }


    @Override
    public ApiResult<List<OrderDTO>> findAllBySort(SimpleSortRequest request) {
        TypedQuery<Order> typedQuery = queryService.generateSimpleSortQuery(Order.class, request);
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(typedQuery.getResultList()));
    }


    @Override
    public ApiResult<List<OrderDTO>> getOrdersByStatus(String status, String pagination) {
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(orderRepository
                                .findAllByStatus(OrderStatus.valueOf(status),
                                        PageRequest.of(page[0], page[1])).getContent()));
    }

    @Override
    public ApiResult<List<OrderDTO>> getOrdersByUserId(UUID userid, String pagination) {
        String currentUserPhoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        User user = userRepository
                .findByPhoneNumber(currentUserPhoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("User not found")
                        .messageRu("")
                        .build());
        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(orderRepository
                                .findAllByUser_IdAndDeletedIsFalse(user.getId(),
                                        PageRequest.of(page[0], page[1]))
                                .getContent()));
    }
}
