package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.ESentiment;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 12/09/2025 - 4:58 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_feedback")
public class Feedback {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @Column(name = "content", length = 500, nullable = false)
  @NotNull(message = "An content is required!")
  private String content;

  @Column(name = "star", nullable = false)
  @DecimalMin(value = "-1", message = "Star must be equal to -1 or greater than or equal to 1")
  @DecimalMax(value = "5", message = "Star must be smaller than or equal to 5")
  @NotNull(message = "An star is required!")
  private int star;

  @Enumerated(EnumType.STRING)
  @Column(name = "sentiment", length = 50)
  // @NotNull(message = "An name is required!")
  private ESentiment sentiment;

  @OneToMany(mappedBy = "mainFeedback", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Comment> childComments = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "tbl_feedback_images", joinColumns = @JoinColumn(name = "feedback_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
  private Set<Image> imageGallery = new HashSet<>();

  public void addImage(Image image) {
    imageGallery.add(image);
  }

  public void removeImage(Image image) {
    imageGallery.remove(image);
  }

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  public Feedback(Product product, User author, String content, Integer star) {
    this.product = product;
    this.author = author;
    this.content = content;
    this.star = star;
  }

  public Feedback(Product product, User author, String content, Integer star, ESentiment sentiment) {
    this.product = product;
    this.author = author;
    this.content = content;
    this.star = star;
    this.sentiment = sentiment;
  }
}
