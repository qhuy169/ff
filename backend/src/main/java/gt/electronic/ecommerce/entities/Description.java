package gt.electronic.ecommerce.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:00 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_description")
public class Description {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @NotNull(message = "An name is required!")
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tbl_description_categories", joinColumns = @JoinColumn(name = "description_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  public void addCategory(Category category) {
    categories.add(category);
  }

  public void removeImage(Category category) {
    categories.remove(category);
  }

}
