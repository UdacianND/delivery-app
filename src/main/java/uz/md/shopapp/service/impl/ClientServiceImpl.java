package uz.md.shopapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.Address;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.address.AddressAddDTO;
import uz.md.shopapp.dtos.address.AddressDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.user.ClientMeDto;
import uz.md.shopapp.exceptions.BadRequestException;
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

import java.util.List;

import static uz.md.shopapp.utils.MessageConstants.*;

@Service
@Transactional
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

        User user = getCurrentUser();

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

        User user = getCurrentUser();

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();
        return ApiResult
                .successResponse(orderMapper
                        .toDTOList(orderRepository
                                .findAllByUserId(user.getId())));
    }

    @Override
    public ApiResult<Void> deleteMyOrders() {

        User user = getCurrentUser();

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();
        orderRepository.deleteAllByUserId(user.getId());
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<List<OrderDTO>> getMyOrders(String page) {

        if (page == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        User user = getCurrentUser();

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotAllowedException.builder()
                    .messageUz("Siz client emassiz")
                    .messageRu("")
                    .build();

        int[] pagination = CommonUtils.getPagination(page);
        return ApiResult.successResponse(orderMapper
                .toDTOList(orderRepository
                        .findAllByUserId(
                                user.getId(),
                                PageRequest.of(pagination[0], pagination[1]))
                        .getContent()));
    }

    @Override
    public ApiResult<List<AddressDTO>> getMyAddresses() {

        User user = getCurrentUser();

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

        if (addressAddDTO == null
                || addressAddDTO.getLocation() == null)
            throw BadRequestException.builder()
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .build();

        User user = userRepository
                .findById(addressAddDTO.getUserId())
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz(USER_NOT_FOUND_UZ)
                        .messageRu(USER_NOT_FOUND_RU)
                        .build());
        Address address = addressMapper.fromAddDTO(addressAddDTO);
        address.setUser(user);
        addressRepository.save(address);
        return ApiResult.successResponse(addressMapper
                .toDTO(address));
    }

    private User getCurrentUser() {

        String phoneNumber = CommonUtils.getCurrentUserPhoneNumber();

        if (phoneNumber == null)
            throw NotFoundException.builder()
                    .messageUz(USER_NOT_FOUND_UZ)
                    .messageRu(USER_NOT_ACTIVE_RU)
                    .build();

        return userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz(USER_NOT_FOUND_UZ)
                        .messageRu("")
                        .build());
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        if (id == null)
            throw BadRequestException.builder()
                    .messageRu(ERROR_IN_REQUEST_RU)
                    .messageUz(ERROR_IN_REQUEST_UZ)
                    .build();

        addressRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
