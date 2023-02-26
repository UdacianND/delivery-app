package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.shopapp.domain.OrderProduct;
import uz.md.shopapp.dtos.order.OrderProductAddDTO;
import uz.md.shopapp.dtos.orderProduct.OrderProductDTO;

@Mapper(componentModel = "spring")
public interface OrderProductMapper extends EntityMapper<OrderProduct, OrderProductDTO> {

    @Override
    @Mapping(target = "orderId", source = "order.id")
    OrderProductDTO toDTO(OrderProduct entity);

    OrderProduct fromAddDTO(OrderProductAddDTO addDTO);
}
