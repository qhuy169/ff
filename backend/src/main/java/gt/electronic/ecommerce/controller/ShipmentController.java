package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.OrderShipmentUpdateDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.dto.response.ShipmentResponseDTO;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.EShipmentStatus;
import gt.electronic.ecommerce.services.ShipmentService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.PRODUCT_PER_PAGE;

@RestController
@RequestMapping(value = "/api/v1/shipment")
@CrossOrigin(origins = "*")
@RolesAllowed({ERole.Names.SHIPPER})
public class ShipmentController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = "Shipment";

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private ShipmentService shipmentService;

    @Autowired
    public void ShipmentService(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/same-area")
    public ResponseObject<Page<OrderResponseDTO>> getShipmentSameAre(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.ACTION_SUCCESSFULLY),
                                    this.shipmentService.getAllOrdersSameAre(loginKey, pageable));
    }

    @PostMapping("/receive-order")
    ResponseObject<List<ShipmentResponseDTO>> receiveOrderShipments(
            @RequestBody List<Long> orderShipmentIds,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.ACTION_SUCCESSFULLY),
                                    this.shipmentService.receiveOrderShipments(loginKey, orderShipmentIds));
    }

    @GetMapping("/get-all")
    ResponseObject<Page<ShipmentResponseDTO>> getAllOrderShipmentsByShipper(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "status", required = false, defaultValue = "null") EShipmentStatus status,
            HttpServletRequest request) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.GET_ALL_OBJECT_SUCCESSFULLY, branchName),
                                    this.shipmentService.getAllOrderShipmentsByShipper(loginKey,
                                                                                       status,
                                                                                       pageable));
    }

    @GetMapping("/{id}")
    ResponseObject<ShipmentResponseDTO> getOrderShipmentsByShipper(
            @PathVariable(name = "id") String id,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.GET_ALL_OBJECT_SUCCESSFULLY, branchName),
                                    this.shipmentService.getOrderShipment(loginKey, id));
    }

    @PutMapping("/{id}")
    ResponseObject<ShipmentResponseDTO> updateLogOrderShipmentsByShipper(
            @PathVariable(name = "id") String id,
            @RequestBody OrderShipmentUpdateDTO updateDTO,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.GET_ALL_OBJECT_SUCCESSFULLY, branchName),
                                    this.shipmentService.updateOrderShipment(loginKey, id, updateDTO));
    }



}
