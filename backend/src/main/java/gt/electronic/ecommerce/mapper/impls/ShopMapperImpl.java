package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.ShopResponseDTO;
import gt.electronic.ecommerce.dto.response.ShopSimpleResponseDTO;
import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.mapper.AddressMapper;
import gt.electronic.ecommerce.mapper.ShopMapper;
import gt.electronic.ecommerce.mapper.ShopPriceMapper;
import gt.electronic.ecommerce.mapper.UserMapper;
import gt.electronic.ecommerce.utils.Utils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author quang huy
 * @created 01/11/2025 - 8:33 PM
 */
@Component
public class ShopMapperImpl implements ShopMapper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private AddressMapper addressMapper;

    @Autowired
    public void AddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    private ShopPriceMapper shopPriceMapper;

    @Autowired
    public void ShopPriceMapper(ShopPriceMapper shopPriceMapper) {
        this.shopPriceMapper = shopPriceMapper;
    }

    private UserMapper userMapper;

    @Autowired
    public void UserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ShopResponseDTO shopToShopResponseDTO(Shop entity) {
        if (entity == null) {
            return null;
        }

        ShopResponseDTO responseDTO = new ShopResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setSlug(entity.getSlug());
        responseDTO.setDescription(entity.getDescription());
        responseDTO.setUser(userMapper.userToUserSimpleResponseDTO(entity.getUser()));
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setEmailVerified(entity.isEmailVerified());
        responseDTO.setPhone(entity.getPhone());
        responseDTO.setPhoneVerified(entity.getUser().isPhoneVerified());
        responseDTO.setPercent(entity.getPercent());
        responseDTO.setAddress(addressMapper.lineAndLocationToAddressResponseDTO(entity.getLine(),
                entity.getLocation()));
        responseDTO.setEnabled(entity.isEnabled());
        if (entity.getAvatar() != null) {
            responseDTO.setAvatar(Utils.getUrlFromPathImage(entity.getAvatar().getPath()));
        }
        if (entity.getBackground() != null) {
            responseDTO.setBackground(Utils.getUrlFromPathImage(entity.getBackground().getPath()));
        }
        responseDTO.setCreatedAt(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());
        responseDTO.setLastLogin(Utils.getTimeDistance(new Date(), new Date()).getTimeDistance());
        if (entity.getShopPrice() != null) {
            responseDTO.setShopPrice(shopPriceMapper.shopPriceToShopPriceResponseDTO(entity.getShopPrice()));
        }
        responseDTO.setRegisterPriceAt(entity.getRegisterPriceAt());
        responseDTO.setEndPriceAt(entity.getEndPriceAt());
        return responseDTO;
    }

    @Override
    public ShopSimpleResponseDTO shopToShopSimpleResponseDTO(Shop entity) {
        if (entity == null) {
            return null;
        }

        ShopSimpleResponseDTO responseDTO = new ShopSimpleResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setSlug(entity.getSlug());
        responseDTO.setDescription(entity.getDescription());
        responseDTO.setUser(userMapper.userToUserSimpleResponseDTO(entity.getUser()));
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setPhone(entity.getPhone());
        responseDTO.setAddress(addressMapper.lineAndLocationToAddressResponseDTO(entity.getLine(),
                entity.getLocation()));
        if (entity.getAvatar() != null) {
            responseDTO.setAvatar(Utils.getUrlFromPathImage(entity.getAvatar().getPath()));
        }
        if (entity.getBackground() != null) {
            responseDTO.setBackground(Utils.getUrlFromPathImage(entity.getBackground().getPath()));
        }
        responseDTO.setLastLogin(Utils.getTimeDistance(new Date(), new Date()).getTimeDistance());
        return responseDTO;
    }
}
