package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.utils.Utils;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 09/09/2025 - 1:38 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_brand")
public class Brand {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  @Size(message = "Invalid name size.", max = 50, min = 1)
  @NotNull(message = "An name is required!")
  private String name;

  @Column(name = "slug", length = 50, nullable = false, unique = true)
  @Size(message = "Invalid slug size.", max = 50, min = 1)
  @NotNull(message = "An name is required!")
  private String slug;

  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "tbl_category_brands", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "image_id")
  private Image image;

  public Brand(String name) {
    this.name = name;
    this.slug = Utils.vnToSlug(name);
  }

  public Brand(String name, Image image) {
    this.name = name;
    this.slug = Utils.vnToSlug(name);
    this.image = image;
  }
}
