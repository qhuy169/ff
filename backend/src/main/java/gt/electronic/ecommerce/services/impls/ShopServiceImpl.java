package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.ShopCreationDTO;
import gt.electronic.ecommerce.dto.response.ShopResponseDTO;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.mapper.ShopMapper;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.models.enums.EPattern;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.repositories.UserRepository;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.services.LocationService;
import gt.electronic.ecommerce.services.ShopService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.config.CodeConfig;
import gt.electronic.ecommerce.utils.GenerateUtil;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:04 AM
 */
@Service
@Transactional
public class ShopServiceImpl implements ShopService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Shop.class.getSimpleName();
  private ImageService imageService;

  @Autowired
  public void ImageService(ImageService imageService) {
    this.imageService = imageService;
  }

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  private LocationService locationService;

  @Autowired
  public void LocationService(LocationService locationService) {
    this.locationService = locationService;
  }

  private ShopMapper shopMapper;

  @Autowired
  public void ShopMapper(ShopMapper shopMapper) {
    this.shopMapper = shopMapper;
  }

  private ShopRepository shopRepo;

  @Autowired
  public void ShopRepository(ShopRepository shopRepo) {
    this.shopRepo = shopRepo;
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

  @Value("${app.shop.percent}")
  private Double shopDefaultPercent;

  @Override
  public List<ShopResponseDTO> getAllShops(String keyword) {
    this.LOGGER.info(
        String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, "Keyword", keyword));
    List<Shop> shopList = this.shopRepo.findAll(keyword);
    if (shopList.size() < 1) {
      throw new ResourceNotFound(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }
    return shopList.stream().map(shop -> this.shopMapper.shopToShopResponseDTO(shop)).collect(Collectors.toList());
  }

  @Override
  public ShopResponseDTO getShopById(Long id) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
    Shop shop = this.shopRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    return this.shopMapper.shopToShopResponseDTO(shop);
  }

  @Override
  public ShopResponseDTO getShopBySlug(String slug) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "Slug", slug));
    Shop entityFound = this.shopRepo
        .findBySlug(slug)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "Slug",
                    slug)));
    return this.shopMapper.shopToShopResponseDTO(entityFound);
  }

  @Override
  public ShopResponseDTO getShopByUser(Long userId) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "UserId", userId));
    User userFound = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", userId)));
    Shop shop = this.shopRepo
        .findByUser(userFound)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "UserId",
                    userFound.getId())));
    return this.shopMapper.shopToShopResponseDTO(shop);
  }

  @Override
  public ShopResponseDTO getShopByAccessToken(String accessToken) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "accessToken", accessToken));
    String loginKey = jwtTokenUtil.getUserNameFromJwtToken(accessToken);
    User userFound;
    if (Utils.isPattern(loginKey) == EPattern.PHONE) {
      userFound = this.userRepo
          .findByPhone(loginKey)
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      User.class.getSimpleName(),
                      "accessToken",
                      accessToken)));
    } else {
      userFound = this.userRepo
          .findByEmail(loginKey)
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      User.class.getSimpleName(),
                      "accessToken",
                      accessToken)));
    }
    Shop entityFound = this.shopRepo.findByUser(userFound).orElseThrow(
        () -> new ResourceNotFound(
            String.format(
                Utils.OBJECT_NOT_FOUND_BY_FIELD,
                branchName,
                "accessToken",
                accessToken)));
    return this.shopMapper.shopToShopResponseDTO(entityFound);
  }

  @Override
  public ShopResponseDTO registerShop(User user) {
    // this.LOGGER.info(
    // String.format(
    // Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
    // check shop name is existed
    if (this.shopRepo.findByUser(user).isPresent()) {
      throw new ResourceAlreadyExistsException(String.format(
          Utils.OBJECT_EXISTED_BY_FIELD,
          branchName,
          "User",
          user.getId()));
    }

    // update role user to role seller
    if (user.getRole() == ERole.ROLE_CUSTOMER) {
      user.setRole(ERole.ROLE_SELLER);
    }

    Shop newEntity = new Shop();
    String usernameGenerate;
    do {
      usernameGenerate = GenerateUtil.generate(CodeConfig.length(Utils.LENGTH_USERNAME_GENERATE));
    } while (this.shopRepo.findByName(usernameGenerate).isPresent());
    newEntity.setName(usernameGenerate);
    newEntity.setSlug(Utils.vnToSlug(usernameGenerate));
    // newEntity.setDescription(creationDTO.getDescription());
    newEntity.setUser(user);
    newEntity.setEmail(user.getEmail());
    newEntity.setEmailVerified(false);
    newEntity.setPhone(user.getPhone());
    newEntity.setPhoneVerified(false);
    newEntity.setPercent(shopDefaultPercent);
    // set address
    if (user.getAddresses() != null && user.getAddresses().iterator().hasNext()) {
      Address address = user.getAddresses().iterator().next();
      newEntity.setLine(address.getLine());
      newEntity.setLocation(address.getLocation());
    }

    return this.shopMapper.shopToShopResponseDTO(this.shopRepo.save(newEntity));
  }

  @Override
  public ShopResponseDTO createShop(
      String loginKey, ShopCreationDTO creationDTO,
      MultipartFile avatarFile, MultipartFile backgroundFile,
      boolean... isAdmin) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
    // check shop name is existed
    if (this.shopRepo.findByName(creationDTO.getName()).isPresent()) {
      throw new ResourceAlreadyExistsException(
          String.format(
              Utils.OBJECT_EXISTED_BY_FIELD,
              branchName,
              "Name",
              creationDTO.getName()));
    }

    User userFound;
    // check if admin
    if (isAdmin != null && isAdmin[0]) {
      userFound = this.userRepo
          .findById(creationDTO.getUserId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      User.class.getSimpleName(),
                      "Id",
                      creationDTO.getUserId())));
    } else { // if without admin
      userFound = this.userService.getUserByLoginKey(loginKey);
    }
    if (this.shopRepo.findByUser(userFound).isPresent()) {
      throw new ResourceAlreadyExistsException(String.format(
          Utils.OBJECT_EXISTED_BY_FIELD,
          branchName,
          "User",
          userFound.getId()));
    }

    // update role user to role seller
    userFound.setRole(ERole.ROLE_SELLER);

    Shop newEntity = new Shop();
    newEntity.setName(creationDTO.getName());
    newEntity.setSlug(Utils.vnToSlug(creationDTO.getName()));
    newEntity.setDescription(creationDTO.getDescription());
    newEntity.setUser(userFound);
    newEntity.setEmail(creationDTO.getEmail());
    newEntity.setEmailVerified(false);
    newEntity.setPhone(creationDTO.getPhone());
    newEntity.setPhoneVerified(false);
    newEntity.setPercent(shopDefaultPercent);
    // set address
    if (creationDTO.getAddress() != null) {
      Location location = new Location();
      location.setCommune(creationDTO.getAddress().getWard());
      location.setDistrict(creationDTO.getAddress().getDistrict());
      location.setProvince(creationDTO.getAddress().getCity());
      newEntity.setLine(creationDTO.getAddress().getHomeAdd());
      newEntity.setLocation(this.locationService.saveLocation(location));
    }
    // set avatar
    if (avatarFile != null && !avatarFile.isEmpty()) {
      Image image = this.imageService.createImageByMultipartFile(avatarFile, EImageType.IMAGE_SHOP);
      newEntity.setAvatar(image);
    }
    // set background
    if (backgroundFile != null && !backgroundFile.isEmpty()) {
      Image image = this.imageService.createImageByMultipartFile(backgroundFile, EImageType.IMAGE_SHOP_BACKGROUND);
      newEntity.setBackground(image);
    }

    return this.shopMapper.shopToShopResponseDTO(this.shopRepo.save(newEntity));
  }

  @Override
  public ShopResponseDTO updateShop(
      String loginKey, Long id, ShopCreationDTO creationDTO,
      MultipartFile avatarFile, MultipartFile backgroundFile,
      boolean... isAdmin) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_UPDATE_OBJECT, branchName, "Name", creationDTO.getName()));
    // check shop is existed
    Shop entityFound = this.shopRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        String.format(
            Utils.OBJECT_NOT_FOUND_BY_FIELD,
            branchName,
            "ID",
            id)));
    if (creationDTO.getName() != null && !Objects.equals(entityFound.getName(), creationDTO.getName())) {
      // check shop name is existed
      if (this.shopRepo.findByName(creationDTO.getName()).isPresent()) {
        throw new ResourceAlreadyExistsException(
            String.format(
                Utils.OBJECT_EXISTED_BY_FIELD,
                branchName,
                "Name",
                creationDTO.getName()));
      }
    }

    User userFound;
    // check if admin
    if (isAdmin != null && isAdmin[0]) {
      userFound = this.userRepo
          .findById(creationDTO.getUserId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      User.class.getSimpleName(),
                      "Id",
                      creationDTO.getUserId())));
    } else { // if without admin
      userFound = this.userService.getUserByLoginKey(loginKey);
      if (!Objects.equals(userFound.getId(), entityFound.getUser().getId())) {
        throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
      }
    }

    if (entityFound.getUser() != userFound && this.shopRepo.findByUser(userFound).isPresent()) {
      throw new ResourceAlreadyExistsException(String.format(
          Utils.OBJECT_EXISTED_BY_FIELD,
          branchName,
          "User",
          userFound.getId()));
    }

    // update role user to role seller if it have not already updated
    if (userFound.getRole() != ERole.ROLE_SELLER) {
      userFound.setRole(ERole.ROLE_SELLER);
    }

    if (creationDTO.getName() != null) {
      entityFound.setName(creationDTO.getName());
      entityFound.setSlug(Utils.vnToSlug(creationDTO.getName()));
    }
    if (creationDTO.getDescription() != null) {
      entityFound.setDescription(creationDTO.getDescription());
    }
    entityFound.setUser(userFound);
    if (creationDTO.getEmail() != null) {
      entityFound.setEmail(creationDTO.getEmail());
      entityFound.setEmailVerified(false);
    }
    if (creationDTO.getPhone() != null) {
      entityFound.setPhone(creationDTO.getPhone());
      entityFound.setPhoneVerified(false);
    }
    entityFound.setPercent(shopDefaultPercent);
    // set address
    if (creationDTO.getAddress() != null) {
      Location location = new Location();
      location.setCommune(creationDTO.getAddress().getWard());
      location.setDistrict(creationDTO.getAddress().getDistrict());
      location.setProvince(creationDTO.getAddress().getCity());
      entityFound.setLine(creationDTO.getAddress().getHomeAdd());
      entityFound.setLocation(this.locationService.saveLocation(location));
    }
    // update avatar
    if (avatarFile != null && !avatarFile.isEmpty()) {
      // delete old avatar if it exists
      if (entityFound.getAvatar() != null) {
        this.imageService.deleteImageById(entityFound.getAvatar().getId());
      }
      // set new avatar
      Image image = this.imageService.createImageByMultipartFile(avatarFile, EImageType.IMAGE_SHOP);
      entityFound.setAvatar(image);
    }
    // update background
    if (backgroundFile != null && !backgroundFile.isEmpty()) {
      // delete old background if it exists
      if (entityFound.getBackground() != null) {
        this.imageService.deleteImageById(entityFound.getBackground().getId());
      }
      // set new background
      Image image = this.imageService.createImageByMultipartFile(backgroundFile, EImageType.IMAGE_SHOP_BACKGROUND);
      entityFound.setBackground(image);
    }

    return this.shopMapper.shopToShopResponseDTO(this.shopRepo.save(entityFound));
  }

  @Override
  public ShopResponseDTO deleteShopById(String loginKey, Long id, boolean... isAdmin) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_DELETE_OBJECT, branchName, "Id", id));
    // check shop is existed
    Shop entityFound = this.shopRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        String.format(
            Utils.OBJECT_NOT_FOUND_BY_FIELD,
            branchName,
            "ID",
            id)));

    User userFound;
    // if without admin
    if (isAdmin != null && !isAdmin[0]) {
      userFound = this.userService.getUserByLoginKey(loginKey);
      if (!Objects.equals(userFound.getId(), entityFound.getUser().getId())) {
        throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
      }
    }

    // delete old avatar if it exists
    if (entityFound.getAvatar() != null) {
      this.imageService.deleteImageById(entityFound.getAvatar().getId());
    }

    // delete old background if it exists
    if (entityFound.getBackground() != null) {
      this.imageService.deleteImageById(entityFound.getBackground().getId());
    }

    // delete entity
    this.shopRepo.deleteById(entityFound.getId());

    return null;
  }
}
