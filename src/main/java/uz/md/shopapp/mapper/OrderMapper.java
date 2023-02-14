package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.Order;
import uz.md.shopapp.dtos.order.OrderAddDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.service.contract.UserService;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class,
                OrderMapper.class,
                OrderProductMapper.class})
public interface OrderMapper extends EntityMapper<Order, OrderDTO> {

    Order fromAddDTO(OrderAddDTO dto);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "address", expression = " java( addressMapper.toDTO(entity.getAddress()) ) ")
    @Mapping(target = "orderProducts", expression = " java( orderProductMapper.toDTOList(entity.getOrderProducts()) )")
    OrderDTO toDTO(Order entity);
}
