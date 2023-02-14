package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.order.OrderAddDTO;
import uz.md.shopapp.dtos.order.OrderDTO;
import uz.md.shopapp.dtos.request.SimpleSearchRequest;
import uz.md.shopapp.dtos.request.SimpleSortRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    /**
     * finds order by id
     *
     * @param id order's id
     * @return the order
     */
    ApiResult<OrderDTO> findById(Long id);

    /**
     * Adds a new order
     * @param dto for adding the order
     * @return newly added order
     */
    ApiResult<OrderDTO> add(OrderAddDTO dto);

    /**
     * Deletes order by id
     * @param id order id
     * @return success if deleted successfuly or else otherwise
     */
    ApiResult<Void> delete(Long id);

    /**
     * gets orders by page
     * @param pagination pagination
     * @return orders by pagination
     */
    ApiResult<List<OrderDTO>> getAllByPage(String pagination);

    /**
     * gets orders by sort
     * @param request sort request
     * @return sorted list of orders
     */
    ApiResult<List<OrderDTO>> findAllBySort(SimpleSortRequest request);


    /**
     * gets orders by status
     * @param status
     * @param pagination
     * @return found list of products
     */
    ApiResult<List<OrderDTO>> getOrdersByStatus(String status, String pagination);

    /**
     * gets orders by userId
     * @param userid id of user
     * @param pagination
     * @return list of products
     */
    ApiResult<List<OrderDTO>> getOrdersByUserId(UUID userid, String pagination);
}
