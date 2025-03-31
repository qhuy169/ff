package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.response.ShopPriceResponseDTO;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.ShopPrice;
import gt.electronic.ecommerce.mapper.ShopPriceMapper;
import gt.electronic.ecommerce.repositories.ShopPriceRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.UtilsService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UtilsServiceImpl implements UtilsService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = ShopPrice.class.getSimpleName();
    private ShopPriceMapper shopPriceMapper;

    @Autowired
    public void ShopPriceMapper(ShopPriceMapper shopPriceMapper) {
        this.shopPriceMapper = shopPriceMapper;
    }
    private ShopPriceRepository shopPriceRepo;

    @Autowired
    public void ShopPriceRepository(ShopPriceRepository shopPriceRepo) {
        this.shopPriceRepo = shopPriceRepo;
    }
    @Override
    public List<ShopPriceResponseDTO> getAllShopPrice() {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
        List<ShopPrice> list = this.shopPriceRepo.findAll();
        return list.stream().map(shopPrice -> shopPriceMapper.shopPriceToShopPriceResponseDTO(shopPrice)).toList();
    }
}
