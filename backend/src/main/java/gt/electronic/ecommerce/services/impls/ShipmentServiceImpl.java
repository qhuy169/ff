package gt.electronic.ecommerce.services.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gt.electronic.ecommerce.dto.request.OrderShipmentUpdateDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.dto.response.ShipmentResponseDTO;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.mapper.OrderMapper;
import gt.electronic.ecommerce.mapper.ShipmentMapper;
import gt.electronic.ecommerce.models.clazzs.OrderLog;
import gt.electronic.ecommerce.models.enums.EOrderLog;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.EShipmentStatus;
import gt.electronic.ecommerce.repositories.OrderRepository;
import gt.electronic.ecommerce.repositories.OrderShopRepository;
import gt.electronic.ecommerce.repositories.ShipmentRepository;
import gt.electronic.ecommerce.services.ShipmentService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ShipmentServiceImpl implements ShipmentService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final Gson gson = new Gson();
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final String branchName = "Shipment";
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

    private ShipmentMapper shipmentMapper;

    @Autowired
    public void ShipmentMapper(ShipmentMapper shipmentMapper) {
        this.shipmentMapper = shipmentMapper;
    }

    private ShipmentRepository shipmentRepo;

    @Autowired
    public void ShipmentRepository(ShipmentRepository shipmentRepo) {
        this.shipmentRepo = shipmentRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Page<OrderResponseDTO> getAllOrdersSameAre(String loginKey, Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_USER,
                                       Order.class.getSimpleName(),
                                       branchName,
                                       loginKey));
        User author = userService.getUserByLoginKey(loginKey);
        if (author.getRole() == ERole.ROLE_SHIPPER && author.getAddresses().stream().findFirst().isPresent()) {
            Location location = author.getAddresses().stream().findFirst().get().getLocation();
            Page<OrderShop> page = this.orderRepo.getAllOrderByShipperArea(location.getCommune(),
                                                                           location.getDistrict(),
                                                                           location.getProvince(),
                                                                           pageable);
            return page.map(orderShop -> this.orderMapper.orderShopToOrderResponseDTO(orderShop, null, true));
        } else {
            throw new UserNotPermissionException();
        }
    }

    @Override
    public List<ShipmentResponseDTO> receiveOrderShipments(String loginKey, List<Long> orderShipmentIds) {
        this.LOGGER.info(String.format(Utils.LOG_RECEIVE_ORDER_SHIPMENTS_BY_SHIPPER,
                                       "[" + String.join(",",
                                                         orderShipmentIds.stream().map(String::valueOf).toList()) + "]",
//                                       orderShipmentIds.stream().collect(Collectors.joining(",")),
                                       loginKey));
        User author = userService.getUserByLoginKey(loginKey);
        if (author.getRole() == ERole.ROLE_SHIPPER && author.getAddresses().stream().findFirst().isPresent()) {
            List<OrderShop> orderShops = new ArrayList<>();
            List<Long> failedReceive = new ArrayList<>();
            for (Long orderShipmentId : orderShipmentIds) {
                OrderShop orderShop = this.orderShopRepo.findById(orderShipmentId)
                        .orElseThrow(() -> new NotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                               OrderShop.class.getSimpleName(),
                                                                               "Id",
                                                                               orderShipmentId)));
                if (orderShop.getStatus() == EOrderStatus.ORDER_SHIPPING) {
//                if (orderShop.getStatus() == EOrderStatus.ORDER_PENDING) {
                    orderShops.add(orderShop);
                } else {
                    failedReceive.add(orderShipmentId);
                }
            }
            List<Shipment> shipments = new ArrayList<>();
            for (OrderShop orderShop : orderShops) {
                Shipment shipment = new Shipment(author, orderShop);
                shipment = this.shipmentRepo.save(shipment);
                orderShop.setShipOrderCode(shipment.getId());
                orderShop.setStatus(EOrderStatus.ORDER_SHIPPING);
                OrderLog orderLog = new OrderLog(EOrderLog.LOG_PICKED.toString(),
                                                 new Date(),
                                                 author.getUsername(),
                                                 author.getRole());
                orderShop.setLog(Utils.addLogToOrderShop(orderLog, orderShop.getLog()));
                this.orderShopRepo.save(orderShop);
                shipments.add(shipment);
            }
            return shipments.stream().map(shipment -> this.shipmentMapper.shipmentToShipmentResponseDTO(shipment))
                    .toList();
        } else {
            throw new UserNotPermissionException();
        }
    }

    @Override
    public Page<ShipmentResponseDTO> getAllOrderShipmentsByShipper(String loginKey,
                                                                   EShipmentStatus status,
                                                                   Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_USER,
                                       branchName + (status != null ? " " + status.name() : ""),
                                       "Shipper",
                                       loginKey));
        User author = userService.getUserByLoginKey(loginKey);
        if (author.getRole() == ERole.ROLE_SHIPPER && author.getAddresses().stream().findFirst().isPresent()) {
            Page<Shipment> page =
                    this.shipmentRepo.findAllByUserAndAndStatus(author, status, pageable);
            return page.map(shipment -> this.shipmentMapper.shipmentToShipmentResponseDTO(shipment));
        } else {
            throw new UserNotPermissionException();
        }
    }

    @Override
    public ShipmentResponseDTO getOrderShipment(String loginKey, String id) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT_BY_USER,
                                       branchName,
                                       "ID",
                                       id,
                                       loginKey));
        User author = userService.getUserByLoginKey(loginKey);
        Shipment shipment = this.shipmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                               branchName,
                                                                               "Id",
                                                                               id
                )));
        if (author.getRole() == ERole.ROLE_SHIPPER && author.getAddresses().stream().findFirst().isPresent() &&
                author.equals(shipment.getUser())) {
            return this.shipmentMapper.shipmentToShipmentResponseDTO(shipment);
        } else {
            throw new UserNotPermissionException();
        }
    }

    @Override
    public ShipmentResponseDTO updateOrderShipment(String loginKey, String id, OrderShipmentUpdateDTO updateDTO) {
        this.LOGGER.info(String.format(Utils.LOG_UPDATE_ORDER_SHIPMENT,
                                       id,
                                       updateDTO.getStatus(),
                                       updateDTO.getLog(),
                                       loginKey));
        User author = userService.getUserByLoginKey(loginKey);
        Shipment shipment = this.shipmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                               branchName,
                                                                               "Id",
                                                                               id
                )));
        if (author.getRole() == ERole.ROLE_SHIPPER && author.getAddresses().stream().findFirst().isPresent() &&
                author.equals(shipment.getUser())) {
            Long orderShopId = shipment.getOrderShopId();
            OrderShop orderShop = this.orderShopRepo.findById(shipment.getOrderShopId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                                                   OrderShop.class.getSimpleName(),
                                                                                   "Id",
                                                                                   orderShopId)));
            if (updateDTO.getCreatedAt() == null) {
                updateDTO.setCreatedAt(new Date());
            }
            String originState = orderShop.getStatus().toString();
            if (updateDTO.getStatus() != null) {
                switch (updateDTO.getStatus()) {
                    case ORDER_CANCELLED -> {
                        shipment.setStatus(EShipmentStatus.CANCELLED);
                        shipment.setCompletedAt(updateDTO.getCreatedAt());
                        orderShop.setStatus(EOrderStatus.ORDER_CANCELLED);
                    }
                    case ORDER_COMPLETED -> {
                        shipment.setStatus(EShipmentStatus.COMPLETED);
                        shipment.setCompletedAt(updateDTO.getCreatedAt());
                        orderShop.setStatus(EOrderStatus.ORDER_COMPLETED);
                    }
                    default -> {
                        shipment.setStatus(EShipmentStatus.SHIPPING);
                        orderShop.setStatus(updateDTO.getStatus());
                    }
                }
            }
            if (updateDTO.getLog() != null) {
                OrderLog orderLog =
                        new OrderLog(updateDTO.getLog(), updateDTO.getCreatedAt(), loginKey, author.getRole());
                orderShop.setLog(Utils.addLogToOrderShop(orderLog, orderShop.getLog()));
            } else if (updateDTO.getStatus() != null && !Objects.equals(originState, orderShop.getStatus().toString())) {
                String log = String.format(Utils.ORDER_STATE_CHANGE_LOG, originState, orderShop.getStatus().toString());
                OrderLog orderLog = new OrderLog(log, updateDTO.getCreatedAt(), loginKey, author.getRole());
                orderShop.setLog(Utils.addLogToOrderShop(orderLog, orderShop.getLog()));
            }
            orderShop = this.orderShopRepo.save(orderShop);
            shipment = this.shipmentRepo.save(shipment);
            return this.shipmentMapper.shipmentToShipmentResponseDTO(shipment);
        } else {
            throw new UserNotPermissionException();
        }
    }
}
