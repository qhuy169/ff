package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.entities.Message;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.repositories.MessageRepository;
import gt.electronic.ecommerce.services.MessageService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = Message.class.getSimpleName();
    private MessageRepository messageRepo;

    @Autowired
    public void MessageRepository(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Page<Message> getMessageProduct(String loginKey, Long shopId, Pageable pageable) {
        this.LOGGER.info(
                String.format(Utils.LOG_GET_ALL_OBJECT_BY_USER, branchName, Shop.class.getSimpleName(), loginKey));
        User authorFound = this.userService.getUserByLoginKey(loginKey);
        if (Objects.equals(authorFound.getShop().getId(), shopId) || authorFound.getRole() == ERole.ROLE_ADMIN) {
            Page page = this.messageRepo.findAllByShopId(shopId, pageable);
            if (page.getContent().size() < 1) {
                throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
            }
            return page;
        } else {
            throw new UserNotPermissionException();
        }
    }
}
