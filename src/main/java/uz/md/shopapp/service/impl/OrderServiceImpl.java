package uz.md.shopapp.service.impl;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.order.OrderAddDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.order.OrderProductAddDTO;
import uz.md.shopapp.dtos.request.SimpleSortRequest;
import uz.md.shopapp.exceptions.BadRequestException;
import uz.md.shopapp.exceptions.NotFoundException;
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
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final QueryService queryService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final InstitutionRepository institutionRepository;

    /**
     * getting by id
     *
     * @param id order's id
     * @return the order
     */
    private Order getById(Long id) {
        log.info("getting by id " + id);
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        return orderRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw NotFoundException.builder()
                            .messageUz(id + " raqamli buyurtma topilmadi ")
                            .messageRu("заказ не найден с идентификатором " + id)
                            .build();
                });
    }

    @Override
    public ApiResult<OrderDTO> findById(Long id) {
        log.info("findById called with id " + id);
        if (id == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        return ApiResult.successResponse(
                orderMapper.toDTO(getById(id)));
    }


    @Override
    public ApiResult<OrderDTO> add(OrderAddDTO dto) {
        log.info("add order called with dto: " + dto);
        if (dto == null
                || dto.getLocation() == null
                || dto.getOrderProducts() == null
                || dto.getOrderProducts().size() == 0)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        Order order = orderMapper.fromAddDTO(dto);

        String currentUserPhoneNumber = CommonUtils.getCurrentUserPhoneNumber();

        if (currentUserPhoneNumber == null)
            throw NotFoundException.builder()
                    .messageUz("foydalanuvchi topilmadi")
                    .messageRu("Пользователь не найден")
                    .build();

        User user = userRepository
                .findByPhoneNumber(currentUserPhoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("foydalanuvchi topilmadi")
                        .messageRu("Пользователь не найден")
                        .build());

        order.setUser(user);
        order.setActive(true);
        order.setDeleted(false);

        Long productId = dto.getOrderProducts()
                .stream()
                .findFirst()
                .orElseThrow()
                .getProductId();

        Institution institution = productRepository
                .findInstitutionByProductId(productId)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Muassasa topilmadi")
                        .messageRu("Объект не найден")
                        .build());

        order.setInstitution(institution);
        orderRepository.save(order);
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductAddDTO addDTO : dto.getOrderProducts()) {
            OrderProduct orderProduct = orderProductMapper.fromAddDTO(addDTO);
            orderProduct.setOrder(order);
            Product product = productRepository
                    .findById(addDTO.getProductId())
                    .orElseThrow(() -> NotFoundException.builder()
                            .messageUz("Buyurtma mahsuloti topilmadi")
                            .messageRu("заказ товара не найден")
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

        log.info("Sum order overall price for order products {}", orderProducts);

        if (orderProducts == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getPrice();
        }
        return totalPrice;
    }

    @Override
    public ApiResult<Void> delete(Long id) {

        if (id == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        if (!orderRepository.existsById(id))
            throw NotFoundException.builder()
                    .messageUz("Buyurtma topilmadi")
                    .messageRu("Заказ не найден")
                    .build();
        orderRepository.deleteById(id);
        return ApiResult.successResponse();
    }


    @Override
    public ApiResult<List<OrderDTO>> getAllByPage(String pagination) {

        log.info("getAllByPage called with pagination " + pagination);

        if (pagination == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult.successResponse(
                orderMapper.toDTOList(orderRepository
                        .findAll(PageRequest.of(page[0], page[1])).getContent()));
    }


    @Override
    public ApiResult<List<OrderDTO>> findAllBySort(SimpleSortRequest request) {

        log.info("findAllBySort request {}", request);

        if (request == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        TypedQuery<Order> typedQuery = queryService.generateSimpleSortQuery(Order.class, request);
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(typedQuery.getResultList()));
    }


    @Override
    public ApiResult<List<OrderDTO>> getOrdersByStatus(String status, String pagination) {

        log.info("getting orders by status " + status + " " + pagination);

        if (status == null || pagination == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        int[] page = CommonUtils.getPagination(pagination);
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(orderRepository
                                .findAllByStatus(OrderStatus.valueOf(status),
                                        PageRequest.of(page[0], page[1])).getContent()));
    }

    @Override
    public ApiResult<List<OrderDTO>> getOrdersByUserId(UUID userid, String pagination) {

        log.info("getting orders by user id {}  with pagination {}", userid, pagination);
        if (userid == null || pagination == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xatolik")
                    .messageRu("Ошибка в запросе")
                    .build();

        String currentUserPhoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        User user = userRepository
                .findByPhoneNumber(currentUserPhoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Foydalanuvchi topilmadi")
                        .messageRu("Пользователь не найден")
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
