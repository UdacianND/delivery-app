package uz.md.shopapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.OrderStatus;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressAddDTO;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.orderProduct.OrderProductDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.dtos.user.ClientMeDto;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.AddressMapper;
import uz.md.shopapp.mapper.OrderMapper;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.AddressRepository;
import uz.md.shopapp.repository.OrderRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.ClientService;
import uz.md.shopapp.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    public ClientServiceImpl(UserRepository userRepository,
                             UserMapper userMapper,
                             OrderMapper orderMapper,
                             AddressMapper addressMapper,
                             OrderRepository orderRepository,
                             AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public ApiResult<ClientMeDto> getMe() {
        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Foydalanuvchi topilmadi")
                        .messageRu("")
                        .build());

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();

        ClientMeDto clientMeDto = userMapper.toClientMeDTO(user);
        Long aLong = orderRepository
                .countAllByUser_IdAndDeletedIsFalse(clientMeDto.getId());
        clientMeDto.setNumberOfOrders(aLong);
        return ApiResult.successResponse(clientMeDto);
    }

    @Override
    public ApiResult<List<OrderDTO>> getMyOrders() {
        List<OrderProductDTO> orderProductDTOS = List.of(
                new OrderProductDTO(1L, 1L,
                        new ProductDTO(1L, "Cola", "ColaRu", "", "", "", 2000D, 1L),
                        3, 9000D),
                new OrderProductDTO(2L, 1L,
                        new ProductDTO(2L, "Cola", "ColaRu", "", "", "", 2000D, 1L),
                        3, 9000D)
        );
        OrderDTO orderDTO = new OrderDTO(1L, 2L, OrderStatus.DELIVERED, 30000D,10000D, LocalDateTime.now(),null,orderProductDTOS);
//        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
//        User user = userRepository
//                .findByPhoneNumber(phoneNumber)
//                .orElseThrow(() -> NotFoundException.builder()
//                        .messageUz("Foydalanuvchi topilmadi")
//                        .messageRu("")
//                        .build());
//
//        if (!user.getRole().getName().equals("CLIENT"))
//            throw NotAllowedException.builder()
//                    .messageUz("Siz client emassiz")
//                    .messageRu("")
//                    .build();
//        return ApiResult
//                .successResponse(orderMapper
//                        .toDTOList(orderRepository
//                                .findAllByUser_IdAndDeletedIsFalse(user.getId())));
        return ApiResult.successResponse(List.of(orderDTO));
    }

    @Override
    public ApiResult<List<OrderDTO>> getMyOrders(String page) {
        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Foydalanuvchi topilmadi")
                        .messageRu("")
                        .build());

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();
        int[] pagination = CommonUtils.getPagination(page);
        return ApiResult.successResponse(orderMapper
                .toDTOList(orderRepository
                        .findAllByUser_IdAndDeletedIsFalse(
                                user.getId(),
                                PageRequest.of(pagination[0], pagination[1]))
                        .getContent()));
    }

    @Override
    public ApiResult<List<AddressDTO>> getMyAddresses() {
        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("Foydalanuvchi topilmadi")
                        .messageRu("")
                        .build());

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();

        return ApiResult.successResponse(addressMapper
                .toDTOList(addressRepository
                        .findAllByUser_Id(user.getId())));
    }

    @Override
    public ApiResult<AddressDTO> addAddress(AddressAddDTO addressAddDTO) {
        User user = userRepository
                .findById(addressAddDTO.getUserId())
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz("Foydalanuvchi topilmadi")
                        .messageRu("")
                        .build());
        Address address = addressMapper.fromAddDTO(addressAddDTO);
        address.setUser(user);
        addressRepository.save(address);
        return ApiResult.successResponse(addressMapper
                .toDTO(address));
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        addressRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
