package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.clazzs.FullAddress;
import gt.electronic.ecommerce.utils.Utils;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 09/09/2025 - 1:38 PM
 * @project gt-backend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_shop")
public class Shop {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  @Size(message = "Invalid email size.", max = 50, min = 1)
  @NotNull(message = "An name is required!")
  private String name;

  @Column(name = "slug", length = 50, nullable = false, unique = true)
  @Size(message = "Invalid slug size.", max = 50, min = 1)
  @NotNull(message = "An name is required!")
  private String slug;

  @Column(name = "description")
  private String description;

  @OneToOne(optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private User user;

  @Column(name = "email", length = 320, unique = true)
  @Size(message = "Invalid email size.", max = 320, min = 10)
  // @NotNull(message = "An email is required!")
  @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
  private String email;

  @Column(name = "is_email_verified", nullable = false)
  @NotNull(message = "An isEmailVerified is required!")
  private boolean isEmailVerified;

  @Column(name = "phone", length = 13, unique = true)
  @Size(message = "Invalid phone size.", max = 13, min = 9)
  // @NotNull(message = "An phone is required!")
  @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
  private String phone;

  @Column(name = "is_phone_verified", nullable = false)
  @NotNull(message = "An isPhoneVerified is required!")
  private boolean isPhoneVerified;

  @Column(name = "percent", nullable = false)
  @NotNull(message = "An percent is required!")
  @DecimalMin(value = "0.01", message = "Percent must be greater than or equal to 0.01")
  @DecimalMax(value = "1", message = "Percent must be smaller than or equal to 1")
  private Double percent;

  @Column(name = "line", nullable = false)
  @NotNull(message = "An line is required!")
  private String line;

  @ManyToOne
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
  private Set<Product> products = new HashSet<>();

  @Column(name = "enabled", nullable = true)
  @NotNull(message = "An enabled is required!")
  private boolean enabled = true;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "avatar")
  private Image avatar;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "background")
  private Image background;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "shop")
  private Set<Discount> discounts = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "shop_price_id")
  private ShopPrice shopPrice;

  @Column(columnDefinition = "DATETIME(6)")
  private Date registerPriceAt;

  @Column(columnDefinition = "DATETIME(6)")
  private Date endPriceAt;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Shop that) || getClass() != o.getClass())
      return false;
    return this.getId().equals(that.getId());
  }

  public Shop(String name, User user, FullAddress fullAddress, String avatar, String background) {
    this.name = name;
    this.slug = Utils.vnToSlug(name);
    user.setShop(this);
    this.user = user;
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.percent = 0.05;
    this.line = fullAddress.getLine();
    this.location = fullAddress.getLocation();
    this.enabled = true;
    this.avatar = Image.createImageAvatarShop(avatar);
    this.background = Image.createImageBackgroundShop(background);
  }
}
