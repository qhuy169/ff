package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EImageType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author quang huy
 * @created 09/09/2025 - 2:04 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tbl_image")
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "path", nullable = false)
  @NotNull(message = "An path is required!")
  private String path;

  @Enumerated(EnumType.STRING)
  @Column(name = "image_type", length = 50, nullable = false)
  @NotNull(message = "An imageType is required!")
  private EImageType imageType;

  public Image(String path, EImageType imageType) {
    this.path = path;
    this.imageType = imageType;
  }

  public static Image createImageProduct(String name) {
    String path = EImageType.IMAGE_PRODUCT + "/" + name;
    EImageType imageType = EImageType.IMAGE_PRODUCT;
    return new Image(path, imageType);
  }

  public static Set<Image> createImageProductGallery(String[] names) {
    Set<Image> gallery = new HashSet<>();
    for (String name : names) {
      String path = EImageType.IMAGE_PRODUCT_GALLERY + "/" + name;
      EImageType imageType = EImageType.IMAGE_PRODUCT_GALLERY;
      Image image = new Image(path, imageType);
      gallery.add(image);
    }
    return gallery;
  }

  public static Image createImageBrand(String name) {
    return name == null || Objects.equals(name, "") ? null
        : new Image(EImageType.IMAGE_BRAND + "/" + name, EImageType.IMAGE_BRAND);
  }

  public static Image createImageCategory(String name) {
    return name == null || Objects.equals(name, "") ? null
        : new Image(EImageType.IMAGE_CATEGORY + "/" + name, EImageType.IMAGE_CATEGORY);
  }

  public static Image createImageAvatarShop(String name) {
    return name == null || Objects.equals(name, "") ? null
        : new Image(EImageType.IMAGE_SHOP + "/" + name, EImageType.IMAGE_SHOP);
  }

  public static Image createImageBackgroundShop(String name) {
    return name == null || Objects.equals(name, "") ? null
        : new Image(EImageType.IMAGE_SHOP_BACKGROUND + "/" + name, EImageType.IMAGE_SHOP_BACKGROUND);
  }

  public static Image createImageDiscount(String name) {
    return name == null || Objects.equals(name, "") ? null
        : new Image(EImageType.IMAGE_DISCOUNT + "/" + name, EImageType.IMAGE_DISCOUNT);
  }
}
