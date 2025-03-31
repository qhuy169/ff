package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.CartDetailCreationDTO;
import gt.electronic.ecommerce.dto.request.CartItemCreationDTO;
import gt.electronic.ecommerce.dto.response.OrderDetailResponseDTO;
import gt.electronic.ecommerce.entities.OrderItem;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.mapper.OrderItemMapper;
import gt.electronic.ecommerce.models.enums.EModelName;
import gt.electronic.ecommerce.models.enums.EOrdertemStatus;
import gt.electronic.ecommerce.models.enums.EPattern;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.repositories.OrderItemRepository;
import gt.electronic.ecommerce.repositories.ProductRepository;
import gt.electronic.ecommerce.repositories.UserRepository;
import gt.electronic.ecommerce.services.CartItemService;
import gt.electronic.ecommerce.services.SaleService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author quang huy
 * @created 19/09/2025 - 7:06 PM
 */
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {
        private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        public static final String branchName = "Cart Item";
        private OrderItemMapper orderItemMapper;

        @Autowired
        public void OrderItemMapper(OrderItemMapper orderItemMapper) {
                this.orderItemMapper = orderItemMapper;
        }

        private OrderItemRepository orderItemRepo;

        @Autowired
        public void OrderItemRepository(OrderItemRepository orderItemRepo) {
                this.orderItemRepo = orderItemRepo;
        }

        private ProductRepository productRepo;

        @Autowired
        public void ProductRepository(ProductRepository productRepo) {
                this.productRepo = productRepo;
        }

        private SaleService saleService;

        @Autowired
        public void SaleService(SaleService saleService) {
                this.saleService = saleService;
        }

        private UserRepository userRepo;

        @Autowired
        public void UserRepository(UserRepository userRepo) {
                this.userRepo = userRepo;
        }

        private UserService userService;

        @Autowired
        public void UserService(UserService userService) {
                this.userService = userService;
        }

        @Override
        public List<OrderDetailResponseDTO> getAllCartItemByUser(String loginKey, Long userId, Pageable pageable) {
                User userFound;
                if (Utils.isPattern(loginKey) == EPattern.PHONE) {
                        userFound = this.userRepo
                                        .findByPhone(loginKey)
                                        .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                        String.format(
                                                                                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                        User.class.getSimpleName(),
                                                                                        "Phone",
                                                                                        loginKey)));
                } else {
                        userFound = this.userRepo
                                        .findByEmail(loginKey)
                                        .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                        String.format(
                                                                                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                        User.class.getSimpleName(),
                                                                                        "Email",
                                                                                        loginKey)));
                }
                List<OrderItem> cartItemList = this.orderItemRepo.findByUserAndStatus(userFound,
                                EOrdertemStatus.UN_PAID,
                                pageable);
                return cartItemList.stream()
                                .map(orderItem -> this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem))
                                .collect(
                                                Collectors.toList());
        }

        @Override
        public OrderDetailResponseDTO getCartItemById(Long id) {
                this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, EModelName.Names.CART_ITEM, "ID", id));
                OrderItem cartItemFound = this.orderItemRepo
                                .findByIdAndStatus(id, EOrdertemStatus.UN_PAID)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                branchName,
                                                                                "ID",
                                                                                id)));
                return this.orderItemMapper.orderItemToOrderDetailResponseDTO(cartItemFound);
        }

        @Override
        public OrderDetailResponseDTO getCartItemByUserAndProduct(Long userId, Long productId) {
                Product productFound = this.productRepo
                                .findById(productId)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                Product.class.getSimpleName(),
                                                                                "ID",
                                                                                productId)));
                User userFound = this.userRepo
                                .findById(userId)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                User.class.getSimpleName(),
                                                                                "ID",
                                                                                userId)));
                OrderItem cartItemFound = this.orderItemRepo
                                .findByUserAndProductAndStatus(userFound, productFound, EOrdertemStatus.UN_PAID)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_TWO_FIELD,
                                                                                branchName,
                                                                                User.class.getSimpleName() + "Id",
                                                                                userId, Product.class.getSimpleName(),
                                                                                productId)));
                return this.orderItemMapper.orderItemToOrderDetailResponseDTO(cartItemFound);
        }

        @Override
        public OrderDetailResponseDTO createCartItem(String loginKey, CartItemCreationDTO creationDTO) {
                this.LOGGER.info(
                                String.format(
                                                Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
                                                EModelName.Names.CART_ITEM,
                                                User.class.getSimpleName(),
                                                creationDTO.getUserId(),
                                                Product.class.getSimpleName(),
                                                creationDTO.getProductId()));
                Product productFound = this.productRepo
                                .findById(creationDTO.getProductId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                Product.class.getSimpleName(),
                                                                                "ID",
                                                                                creationDTO.getProductId())));
                User userFound = this.userService.getUserByLoginKey(loginKey);
                OrderItem cartItemFound = this.orderItemRepo
                                .findByUserAndProductAndStatus(userFound, productFound, EOrdertemStatus.UN_PAID)
                                .orElse(null);
                if (cartItemFound == null) {
                        cartItemFound = new OrderItem();
                        cartItemFound.setUser(userFound);
                        cartItemFound.setProduct(productFound);
                        cartItemFound
                                        .setQuantity(creationDTO.getQuantity() < productFound.getQuantity()
                                                        ? cartItemFound.getQuantity()
                                                        : productFound.getQuantity());
                        cartItemFound.setSale(this.saleService.getMostOptimalSaleByProduct(productFound.getId()));
                        cartItemFound.setTotalPrice(
                                        Utils.getPriceProduct(cartItemFound.getProduct(), cartItemFound.getSale()));
                        cartItemFound.setStatus(EOrdertemStatus.UN_PAID);
                } else {
                        cartItemFound
                                        .setQuantity(cartItemFound.getQuantity()
                                                        + creationDTO.getQuantity() < productFound.getQuantity()
                                                                        ? cartItemFound.getQuantity()
                                                                                        + creationDTO.getQuantity()
                                                                        : productFound.getQuantity());
                }

                return this.orderItemMapper.orderItemToOrderDetailResponseDTO(this.orderItemRepo.save(cartItemFound));
        }

        @Override
        public OrderDetailResponseDTO updateCartItem(String loginKey, Long id, CartItemCreationDTO creationDTO) {
                this.LOGGER.info(
                                String.format(Utils.LOG_UPDATE_OBJECT, EModelName.Names.CART_ITEM, "ID", id));
                Product productFound = this.productRepo
                                .findById(creationDTO.getProductId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                Product.class.getSimpleName(),
                                                                                "ID",
                                                                                creationDTO.getProductId())));
                User userFound = this.userService.getUserByLoginKey(loginKey);
                OrderItem cartItemFound = this.orderItemRepo
                                .findByIdAndStatus(id, EOrdertemStatus.UN_PAID)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                EModelName.Names.CART_ITEM,
                                                                                "ID",
                                                                                id)));
                if (Objects.equals(userFound.getId(), cartItemFound.getUser().getId())
                                || userFound.getRole() == ERole.ROLE_ADMIN) {
                        cartItemFound
                                        .setQuantity(cartItemFound.getQuantity()
                                                        + creationDTO.getQuantity() < productFound.getQuantity()
                                                                        ? cartItemFound.getQuantity()
                                                                                        + creationDTO.getQuantity()
                                                                        : productFound.getQuantity());

                        return this.orderItemMapper
                                        .orderItemToOrderDetailResponseDTO(this.orderItemRepo.save(cartItemFound));
                } else {
                        throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
                }
        }

        @Override
        public OrderDetailResponseDTO deleteCartItemById(String loginKey, Long userId, Long id) {
                this.LOGGER.info(
                                String.format(Utils.LOG_DELETE_OBJECT, EModelName.Names.CART_ITEM, "ID", id));
                User userFound = this.userService.getUserByLoginKey(loginKey);
                OrderItem cartItemFound = this.orderItemRepo
                                .findByIdAndStatus(id, EOrdertemStatus.UN_PAID)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format(
                                                                                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                EModelName.Names.CART_ITEM,
                                                                                "ID",
                                                                                id)));

                if (Objects.equals(userFound.getId(), cartItemFound.getUser().getId())
                                || userFound.getRole() == ERole.ROLE_ADMIN) {
                        // delete Cart Item
                        this.orderItemRepo.deleteById(id);

                        return null;
                } else {
                        throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
                }
        }

        @Override
        public List<OrderDetailResponseDTO> updateCart(
                        String loginKey,
                        List<CartDetailCreationDTO> creationDTOList) {
                this.LOGGER.info(
                                String.format(Utils.LOG_UPDATE_OBJECT + Utils.ADD_LOG_FOR_USER, "List cart", "Length",
                                                creationDTOList.size(),
                                                "loginKey", loginKey));
                User userFound;
                if (Utils.isPattern(loginKey) == EPattern.PHONE) {
                        userFound = this.userRepo
                                        .findByPhone(loginKey)
                                        .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                        String.format(
                                                                                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                        User.class.getSimpleName(),
                                                                                        "Phone",
                                                                                        loginKey)));
                } else {
                        userFound = this.userRepo
                                        .findByEmail(loginKey)
                                        .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                        String.format(
                                                                                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                        User.class.getSimpleName(),
                                                                                        "Email",
                                                                                        loginKey)));
                }
                List<OrderItem> orderItemList;
                this.orderItemRepo.saveAll(
                                this.orderItemMapper.cartDetailCreationDTOsToOrderItems(userFound, creationDTOList));
                orderItemList = this.orderItemRepo.findByUserAndStatus(userFound, EOrdertemStatus.UN_PAID, null);
                return orderItemList.stream()
                                .map(orderItem -> this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem))
                                .collect(
                                                Collectors.toList());
        }
}
