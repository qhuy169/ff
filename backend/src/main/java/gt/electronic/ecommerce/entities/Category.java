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
@Table(name = "tbl_category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

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

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH })
  private Set<Category> categories = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "tbl_category_brands", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
  private Set<Brand> brands = new HashSet<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "thumbnail_id")
  private Image thumbnail;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "icon_id")
  private Image icon;

  @PreRemove
  private void preRemove() {
    categories.forEach(child -> child.setParentCategory(null));
  }

  public Category(String name, Image thumbnail, Image icon, Set<Brand> brands) {
    this.name = name;
    this.slug = Utils.vnToSlug(name);
    this.thumbnail = thumbnail;
    this.icon = icon;
    this.brands = brands;
  }
}
