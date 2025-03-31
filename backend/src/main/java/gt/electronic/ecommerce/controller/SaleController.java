package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.SaleCreationDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.dto.response.SaleResponseDTO;
import gt.electronic.ecommerce.entities.Sale;
import gt.electronic.ecommerce.services.SaleService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.DEFAULT_SIZE;

/**
 * @author quang huy
 * @created 16/09/2025 - 9:11 PM
 */
@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin(origins = "*")
public class SaleController {
        private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        public static final String branchName = Sale.class.getSimpleName();

        private SaleService saleService;

        @Autowired
        public void SaleService(SaleService saleService) {
                this.saleService = saleService;
        }

        @GetMapping(value = "/search")
        public ResponseObject<Page<SaleResponseDTO>> searchSale(
                        @RequestParam(name = "title", required = false, defaultValue = "") String title,
                        @RequestParam(name = "fromPercent", required = false, defaultValue = "0.0") Double fromPercent,
                        @RequestParam(name = "toPercent", required = false, defaultValue = "1.0") Double toPercent,
                        @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
                        @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
                        @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                        @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) int size,
                        @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
                Sort sort = Sort.by(sortField);
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.saleService.searchAllSales(
                                                title, fromPercent, toPercent, fromDate, toDate, isHasChild, pageable));
        }

        @GetMapping
        public ResponseObject<Page<SaleResponseDTO>> getAllSales(
                        @RequestParam(name = "isHasChild", defaultValue = "false") Boolean isHasChild,
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                        @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) int size,
                        @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
                Sort sort = Sort.by(sortField);
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.saleService.getAllSales(isHasChild, pageable));
        }

        @GetMapping("/{id}")
        public ResponseObject<SaleResponseDTO> getSaleById(
                        @PathVariable(name = "id") Long id,
                        @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild) {
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.saleService.getSaleById(id, isHasChild));
        }

        @PostMapping
        public ResponseObject<SaleResponseDTO> createSale(
                        @RequestPart("data") @Valid SaleCreationDTO creationDTO,
                        @RequestPart(value = "image", required = false) MultipartFile imageFile) {
                return new ResponseObject<>(HttpStatus.CREATED,
                                String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
                                this.saleService.createSale(creationDTO, imageFile));
        }

        @PutMapping("/{id}")
        public ResponseObject<SaleResponseDTO> updateSale(
                        @PathVariable(name = "id") Long id,
                        @RequestPart("data") @Valid SaleCreationDTO creationDTO,
                        @RequestPart(value = "image", required = false) MultipartFile imageFile) {
                return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
                                this.saleService.updateSale(id, creationDTO, imageFile));
        }

        @DeleteMapping("/{id}")
        public ResponseObject<SaleResponseDTO> deleteSale(@PathVariable(name = "id") Long id) {
                return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
                                this.saleService.deleteSaleById(id));
        }
}
