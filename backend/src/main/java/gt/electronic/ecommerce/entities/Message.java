package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EMessage;
import gt.electronic.ecommerce.models.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String toName;

    private String toEmail;

    private String title;

//    @Column(columnDefinition = "mediumtext(25000)")
    @Lob
    private String body;

//    @Column(columnDefinition = "mediumtext(25000)")
    @Lob
    private String products;

    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull(message = "An isBaned is required!")
    private boolean isBaned = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'MESSAGE_PRODUCT'")
    @NotNull(message = "An type is required!")
    private EMessage type = EMessage.MESSAGE_PRODUCT;

    @Column(columnDefinition = "DATETIME(6)")
    private Date scanAt;

    @Column(columnDefinition = "DATETIME(6)")
    private Date sendAt;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
