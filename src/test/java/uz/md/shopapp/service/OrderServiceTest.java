//package uz.md.shopapp.service;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.domain.*;
//import uz.md.shopapp.domain.enums.OrderStatus;
//import uz.md.shopapp.domain.enums.PermissionEnum;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.address.AddressAddDTO;
//import uz.md.shopapp.dtos.order.OrderAddDTO;
//import uz.md.shopapp.dtos.order.OrderDTO;
//import uz.md.shopapp.dtos.order.OrderProductAddDTO;
//import uz.md.shopapp.dtos.orderProduct.OrderProductDTO;
//import uz.md.shopapp.dtos.request.SimpleSearchRequest;
//import uz.md.shopapp.dtos.request.SimpleSortRequest;
//import uz.md.shopapp.exceptions.IllegalRequestException;
//import uz.md.shopapp.exceptions.NotFoundException;
//import uz.md.shopapp.repository.*;
//import uz.md.shopapp.service.contract.OrderService;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@IntegrationTest
//@Transactional
//@ActiveProfiles("test")
//public class OrderServiceTest {
//
//    private static final OrderStatus ORDER_STATUS = OrderStatus.PREPARING;
//    private static final Double OVERALL_PRICE = 800.0;
//
//    private static final String USER_FIRST_NAME = "Ali";
//    private static final String USER_LAST_NAME = "Ali";
//    private static final String USER_PHONE_NUMBER = "+998931664455";
//    private static final String USER_PASSWORD = "123";
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderService orderService;
//
//    private Order order;
//    private User user;
//    private Address address;
//    private Category category;
//
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private AddressRepository addressRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void init() {
//        order = new Order();
//        order.setStatus(ORDER_STATUS);
//        order.setActive(true);
//        order.setDeleted(false);
//        order.setOverallPrice(OVERALL_PRICE);
//        user = userRepository.saveAndFlush(new User(
//                USER_FIRST_NAME,
//                USER_LAST_NAME,
//                USER_PHONE_NUMBER,
//                USER_PASSWORD,
//                roleRepository
//                        .save(new Role("ADMIN",
//                                "description",
//                                Set.of(PermissionEnum.values()))),
//                true));
//        order.setUser(user);
//        address = addressRepository
//                .save(new Address(user,
//                        45,
//                        "street",
//                        "Tashkent"));
//        order.setAddress(addressRepository
//                .saveAndFlush(address));
//    }
//
//    @Test
//    @Transactional
//    void shouldFindById() {
//        orderRepository.saveAndFlush(order);
//        ApiResult<OrderDTO> byId = orderService.findById(order.getId());
//        assertTrue(byId.isSuccess());
//        OrderDTO data = byId.getData();
//
//        assertEquals(data.getId(), order.getId());
//        assertEquals(data.getOverallPrice(), order.getOverallPrice());
//        assertEquals(data.getStatus(), order.getStatus());
//        assertEquals(data.getUserId(), order.getUser().getId());
//        assertEquals(data.getAddress().getId(), order.getAddress().getId());
//    }
//
//    @Test
//    @Transactional
//    void shouldNotFindById() {
//        assertThrows(NotFoundException.class, () -> orderService.findById(15L));
//    }
//
//    @Test
//    @Transactional
//    void shouldAddWithUserAddedAddress() {
//        Category category = categoryRepository.saveAndFlush(new Category("category", "description"));
//        Address address1 = address;
//        List<Product> products = productRepository.saveAllAndFlush(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product1", "description", 500.0, category)
//        ));
//
//        List<OrderProductAddDTO> orderProductAddDTOs = new ArrayList<>(List.of(
//                new OrderProductAddDTO(products.get(0).getId(), 2, 2 * products.get(0).getPrice()),
//                new OrderProductAddDTO(products.get(1).getId(), 2, 2 * products.get(1).getPrice())
//        ));
//
//        OrderAddDTO addDTO = new OrderAddDTO(user.getId(),
//                null,
//                address1.getId(),
//                500.0,
//                orderProductAddDTOs);
//
//        ApiResult<OrderDTO> add = orderService.add(addDTO);
//
//        assertTrue(add.isSuccess());
//        OrderDTO data = add.getData();
//        assertNotNull(data.getId());
//        assertEquals(data.getOverallPrice(), addDTO.getOverallPrice());
//        assertEquals(data.getStatus(), OrderStatus.PREPARING);
//        assertEquals(data.getUserId(), addDTO.getUserId());
//        checkOrderProductAndAddDTOEquals(data.getOrderProducts(), orderProductAddDTOs);
//        assertEquals(data.getAddress().getId(), addDTO.getAddressId());
//    }
//
//    @Test
//    @Transactional
//    void shouldAddWithAddressAddAndOrderAdd() {
//        Category category = categoryRepository.saveAndFlush(new Category("category", "description"));
//
//        List<Product> products = productRepository.saveAllAndFlush(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product1", "description", 500.0, category)
//        ));
//
//        List<OrderProductAddDTO> orderProductAddDTOs = new ArrayList<>(List.of(
//                new OrderProductAddDTO(products.get(0).getId(), 2, 2 * products.get(0).getPrice()),
//                new OrderProductAddDTO(products.get(1).getId(), 2, 2 * products.get(1).getPrice())
//        ));
//        AddressAddDTO addressAddDTO = new AddressAddDTO(45, "street", "Andijan", user.getId());
//        OrderAddDTO addDTO = new OrderAddDTO(user.getId(),
//                addressAddDTO,
//                null,
//                500.0,
//                orderProductAddDTOs);
//
//        ApiResult<OrderDTO> add = orderService.add(addDTO);
//
//        assertTrue(add.isSuccess());
//        OrderDTO data = add.getData();
//        assertNotNull(data.getId());
//        assertEquals(data.getOverallPrice(), addDTO.getOverallPrice());
//        assertEquals(data.getStatus(), OrderStatus.PREPARING);
//        assertEquals(data.getUserId(), addDTO.getUserId());
//        checkOrderProductAndAddDTOEquals(data.getOrderProducts(), orderProductAddDTOs);
//        assertNotNull(data.getAddress().getId());
//        assertEquals(data.getAddress().getUserId(), addressAddDTO.getUserId());
//        assertEquals(data.getAddress().getHouseNumber(), addressAddDTO.getHouseNumber());
//        assertEquals(data.getAddress().getCity(), addressAddDTO.getCity());
//        assertEquals(data.getAddress().getStreet(), addressAddDTO.getStreet());
//    }
//
//    private void checkOrderProductAndAddDTOEquals(List<OrderProductDTO> actual, List<OrderProductAddDTO> expected) {
//        assertEquals(actual.size(), expected.size());
//        for (int i = 0; i < actual.size(); i++) {
//            OrderProductDTO dto = actual.get(i);
//            OrderProductAddDTO addDTO = expected.get(i);
//            assertEquals(addDTO.getQuantity(), dto.getQuantity());
//            assertEquals(addDTO.getProductId(), dto.getProduct().getId());
//            assertEquals(addDTO.getPrice(), dto.getPrice());
//        }
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddOrderWithOutUserId() {
//
//        Category category = categoryRepository.saveAndFlush(new Category("category", "description"));
//
//        List<Product> products = productRepository.saveAllAndFlush(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product1", "description", 500.0, category)
//        ));
//
//        List<OrderProductAddDTO> orderProductAddDTOs = new ArrayList<>(List.of(
//                new OrderProductAddDTO(products.get(0).getId(), 2, 2 * products.get(0).getPrice()),
//                new OrderProductAddDTO(products.get(1).getId(), 2, 2 * products.get(1).getPrice())
//        ));
//        AddressAddDTO addressAddDTO = new AddressAddDTO(45, "street", "Andijan", user.getId());
//        OrderAddDTO addDTO = new OrderAddDTO(
//                UUID.randomUUID(),
//                addressAddDTO,
//                null,
//                500.0,
//                orderProductAddDTOs);
//
//        assertThrows(NotFoundException.class, () -> orderService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddOrderWithOutAddress() {
//
//        Category category = categoryRepository.saveAndFlush(new Category("category", "description"));
//
//        List<Product> products = productRepository.saveAllAndFlush(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product1", "description", 500.0, category)
//        ));
//
//        List<OrderProductAddDTO> orderProductAddDTOs = new ArrayList<>(List.of(
//                new OrderProductAddDTO(products.get(0).getId(), 2, 2 * products.get(0).getPrice()),
//                new OrderProductAddDTO(products.get(1).getId(), 2, 2 * products.get(1).getPrice())
//        ));
//        OrderAddDTO addDTO = new OrderAddDTO(
//                user.getId(),
//                null,
//                45L,
//                500.0,
//                orderProductAddDTOs);
//
//        assertThrows(NotFoundException.class, () -> orderService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddOrderWithOutAddress2() {
//
//        Category category = categoryRepository.saveAndFlush(new Category("category", "description"));
//
//        List<Product> products = productRepository.saveAllAndFlush(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product1", "description", 500.0, category)
//        ));
//
//        List<OrderProductAddDTO> orderProductAddDTOs = new ArrayList<>(List.of(
//                new OrderProductAddDTO(products.get(0).getId(), 2, 2 * products.get(0).getPrice()),
//                new OrderProductAddDTO(products.get(1).getId(), 2, 2 * products.get(1).getPrice())
//        ));
//        OrderAddDTO addDTO = new OrderAddDTO(
//                user.getId(),
//                null,
//                null,
//                500.0,
//                orderProductAddDTOs);
//
//        assertThrows(IllegalRequestException.class, () -> orderService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldDelete() {
//        orderRepository.saveAndFlush(order);
//        ApiResult<Void> delete = orderService.delete(order.getId());
//        assertTrue(delete.isSuccess());
//    }
//
//    @Test
//    void shouldNotDeleteWithNotExistedId() {
//        assertThrows(NotFoundException.class, () -> orderService.delete(10L));
//    }
//
//    @Test
//    void shouldGetAllByPage() {
//        List<Order> orders = generateOrders(10);
//        orderRepository.saveAllAndFlush(orders);
////        generateOrderProductsForOrders(orders, 3);
//        ApiResult<List<OrderDTO>> allByPage = orderService.getAllByPage("0-5");
//        assertTrue(allByPage.isSuccess());
//        List<OrderDTO> data = allByPage.getData();
//        assertEquals(5, data.size());
//    }
//
//    @Test
//    void shouldGetBySort(){
//        List<Order> orders = generateOrders(10);
//        orderRepository.saveAllAndFlush(orders);
//        SimpleSortRequest sortRequest = SimpleSortRequest.builder()
//                .sortBy("user")
//                .direction(Sort.Direction.ASC)
//                .page(0)
//                .pageCount(4)
//                .build();
//
//        ApiResult<List<OrderDTO>> allBySort = orderService.findAllBySort(sortRequest);
//
//        assertTrue(allBySort.isSuccess());
//        List<OrderDTO> data = allBySort.getData();
//        assertEquals(4, data.size());
//    }
//
//    private void generateOrderProductsForOrders(List<Order> orders, int count) {
//        List<Product> products = generateProduct(orders.size() * count);
//        int k = 0;
//        for (Order o : orders) {
//            List<OrderProduct> orderProducts = new ArrayList<>();
//            for (int i = 0; i < count; i++){
//                orderProducts.add(OrderProduct
//                        .builder()
//                        .order(o)
//                        .product(products.get(k))
//                        .price(products.get(k).getPrice() * 2)
//                        .quantity(2)
//                        .deleted(false)
//                        .build());
//                k++;
//            }
//            o.setOrderProducts(orderProducts);
//        }
//    }
//
//    private List<Product> generateProduct(int count) {
//
//        List<Product> products = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < count; i++) {
//            double d = 500.0 * random.nextDouble() + 100.0;
//            products.add(Product.builder()
//                    .name("product" + i)
//                    .description("product description")
//                    .price(d)
//                    .category(category)
//                    .build());
//        }
//        return products;
//    }
//
//
//    private List<Order> generateOrders(int count) {
//        List<Order> orders = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            orders.add(Order.builder()
//                    .user(user)
//                    .address(address)
//                    .status(OrderStatus.PREPARING)
//                    .build());
//        }
//        return orders;
//    }
//
//}
