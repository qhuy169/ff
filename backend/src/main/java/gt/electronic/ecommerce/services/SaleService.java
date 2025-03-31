package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.SaleCreationDTO;
import gt.electronic.ecommerce.dto.response.SaleResponseDTO;
import gt.electronic.ecommerce.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author quang huy
 * @created 16/09/2025 - 9:04 PM
 */
public interface SaleService {
  Page<SaleResponseDTO> getAllSales(boolean isHasChild, Pageable pageable);

  Page<SaleResponseDTO> searchAllSales(String name, Double fromPercent, Double toPercent, Date fromDate,
      Date toDate, boolean isHasChild, Pageable pageable);

  SaleResponseDTO getSaleById(Long id, boolean isHasChild);

  Sale getMostOptimalSaleByProduct(Long productId);

  SaleResponseDTO createSale(SaleCreationDTO creationDTO, MultipartFile thumbnailFile);

  SaleResponseDTO updateSale(Long id, SaleCreationDTO creationDTO, MultipartFile thumbnailFile);

  SaleResponseDTO deleteSaleById(Long id);
}
