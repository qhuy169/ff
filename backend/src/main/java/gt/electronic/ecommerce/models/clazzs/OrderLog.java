package gt.electronic.ecommerce.models.clazzs;

import gt.electronic.ecommerce.models.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class OrderLog {
    private String log;
    private Date createdAt;
    private String createdLoginKey;
    private ERole createdRole;
}
