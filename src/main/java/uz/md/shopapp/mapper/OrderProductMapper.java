package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import uz.md.shopapp.domain.OrderProduct;
import uz.md.shopapp.dtos.order.OrderProductAddDTO;
import uz.md.shopapp.dtos.orderProduct.OrderProductDTO;

@Mapper(componentModel = "spring")
public interface OrderProductMapper extends EntityMapper<OrderProduct, OrderProductDTO> {

    OrderProduct fromAddDTO(OrderProductAddDTO addDTO);
}
