package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    Page<Message> getMessageProduct(String loginKey, Long shopId, Pageable pageable);
}
