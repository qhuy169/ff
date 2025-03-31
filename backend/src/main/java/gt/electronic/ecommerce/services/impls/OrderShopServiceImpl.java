package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.OrderUpdateStatusDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.mapper.OrderMapper;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.repositories.OrderRepository;
import gt.electronic.ecommerce.repositories.OrderShopRepository;
import gt.electronic.ecommerce.repositories.ProductRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.OrderShopService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author quang huy
 * @created 23/12/2025 - 7:42 PM
 */
@Service
@Transactional
public class OrderShopServiceImpl implements OrderShopService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = OrderShop.class.getSimpleName();
    private OrderMapper orderMapper;

    @Autowired
    public void OrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    private OrderRepository orderRepo;

    @Autowired
    public void OrderRepository(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    private OrderShopRepository orderShopRepo;

    @Autowired
    public void OrderShopRepository(OrderShopRepository orderShopRepo) {
        this.orderShopRepo = orderShopRepo;
    }

    private ProductRepository productRepo;

    @Autowired
    public void ProductRepository(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    private ShopRepository shopRepo;

    @Autowired
    public void ShopRepository(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Page<OrderResponseDTO> getAllOrderShops(Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
        Page<OrderShop> orderPage = this.orderShopRepo.findAll(pageable);
        return orderPage.map(order -> this.orderMapper.orderShopToOrderResponseDTO(order, null));
    }

    @Override
    public Page<OrderResponseDTO> getAllOrderShopsByUser(String loginKey, Long userId, Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD + Utils.ADD_LOG_FOR_USER,
                branchName,
                "User",
                userId,
                "LoginKey",
                loginKey));
        User userFound = this.userService.getUserByLoginKey(loginKey);
        Page<OrderShop> orderPage = this.orderShopRepo.findAllByUser(userFound, pageable);
        return orderPage.map(order -> this.orderMapper.orderShopToOrderResponseDTO(order, null, true));
    }

    @Override
    public Page<OrderResponseDTO> getAllOrderShopsByShop(
            String loginKey,
            Long shopId,
            Pageable pageable,
            boolean... isAdmin) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT + Utils.ADD_LOG_FOR_USER,
                branchName,
                "ShopId",
                shopId,
                "LoginKey",
                loginKey));
        Shop shopFound;
        if (isAdmin.length > 0 && isAdmin[0]) {
            shopFound = this.shopRepo.findById(shopId)
                    .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "ID",
                            shopId)));
        } else {
            User userFound = this.userService.getUserByLoginKey(loginKey);
            shopFound = this.shopRepo.findByUser(userFound)
                    .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "User",
                            userFound.getUsername())));
            if (!Objects.equals(shopFound.getId(), shopId)) {
                throw new UserNotPermissionException();
            }
        }
        Page<OrderShop> orderPage = this.orderShopRepo.findAllByShop(shopFound, pageable);
        return orderPage.map(order -> this.orderMapper.orderShopToOrderResponseDTO(order, shopFound.getId(), true));
    }

    @Override
    public OrderResponseDTO getOrderShopByOrderId(String loginKey, Long orderId) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT + Utils.ADD_LOG_FOR_USER,
                branchName,
                "ID",
                orderId,
                "LoginKey",
                loginKey));
        User userFound = this.userService.getUserByLoginKey(loginKey);
        OrderShop orderFound = this.orderShopRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "ID",
                        orderId)));
        return this.orderMapper.orderShopToOrderResponseDTO(orderFound, null, true);
    }

    @Override
    public OrderResponseDTO updateStatusOrderShop(String loginKey, Long id, OrderUpdateStatusDTO updateStatusDTO) {
        this.LOGGER.info(String.format(Utils.LOG_UPDATE_OBJECT_BY_TWO_FIELD + Utils.ADD_LOG_FOR_USER,
                branchName,
                "Id",
                id,
                "Status",
                updateStatusDTO.getStatus(),
                "LoginKey",
                loginKey));
        User userFound = this.userService.getUserByLoginKey(loginKey);
        OrderShop entityFound = this.orderShopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "ID",
                        id)));
        if (entityFound.getOrder() != null && (entityFound.getOrder().getUser() == null || Objects.equals(
                userFound.getId(),
                entityFound.getShop().getUser().getId())
                || (Objects.equals(
                        userFound.getId(),
                        entityFound.getOrder().getUser()
                                .getId())
                        && entityFound.getStatus() == EOrderStatus.ORDER_SHIPPING &&
                        updateStatusDTO.getStatus() == EOrderStatus.ORDER_CANCELLED)
                || userFound.getRole() == ERole.ROLE_ADMIN)) {
            entityFound.setStatus(updateStatusDTO.getStatus());
            if (updateStatusDTO.getLog() != null && !Objects.equals(updateStatusDTO.getLog().trim(), "")) {
                entityFound.setLog(updateStatusDTO.getLog());
            }
            if (updateStatusDTO.getShipOrderCode() != null && !Objects.equals(updateStatusDTO.getShipOrderCode().trim(),
                    "")) {
                entityFound.setShipOrderCode(updateStatusDTO.getShipOrderCode());
            }
            if (updateStatusDTO.getExpectedDeliveryTime() != null) {
                entityFound.setExpectedDeliveryTime(updateStatusDTO.getExpectedDeliveryTime());
            }
            if (updateStatusDTO.getTransportFee() != null) {
                entityFound.setTotalFee(updateStatusDTO.getTransportFee());
                entityFound.setTotalPrice(entityFound.getTotalPriceProduct().add(entityFound.getTotalFee())
                        .subtract(entityFound.getTotalPriceDiscount()));
            }
            return this.orderMapper.orderShopToOrderResponseDTO(this.orderShopRepo.save(entityFound), null, true);
        } else {
            throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
        }
    }
}
