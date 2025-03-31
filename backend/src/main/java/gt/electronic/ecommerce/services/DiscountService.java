package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.DiscountCreationDTO;
import gt.electronic.ecommerce.dto.request.DiscountUpdateDTO;
import gt.electronic.ecommerce.dto.response.DiscountResponseDTO;
import gt.electronic.ecommerce.models.clazzs.DiscountCodes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:01 AM
 */
public interface DiscountService {
  Page<DiscountResponseDTO> getAllDiscountByShop(Long shopId, Pageable pageable);

  Page<DiscountResponseDTO> searchDiscount(String name, Double percent, String code,
      Date startDate, Date endDate, Double fromPercent, Double toPercent, Date fromDate,
      Date toDate, Long shopId, Pageable pageable);
      Page<DiscountResponseDTO> getAllDiscounts(Pageable pageable);


  DiscountResponseDTO getDiscountById(Long id);

  DiscountResponseDTO checkDiscountByCode(String loginKey, String code);

  DiscountResponseDTO createDiscount(String loginKey, DiscountCreationDTO creationDTO, MultipartFile imageFile);

  DiscountResponseDTO updateDiscount(String loginKey, Long id, DiscountUpdateDTO updateDTO, MultipartFile imageFile);

  DiscountResponseDTO deleteDiscountById(String loginKey, Long id);

  List<DiscountResponseDTO> getAllDiscountByUser(String loginKey, Pageable pageable);

  List<DiscountResponseDTO> addAllDiscountToUser(String loginKey, DiscountCodes discountCodes);

  List<DiscountResponseDTO> removeAllDiscountFromUser(String loginKey, DiscountCodes discountCodes);
}
