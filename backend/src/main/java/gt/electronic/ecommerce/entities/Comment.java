package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.ECommentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
@Table(name = "tbl_comment")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content", length = 500, nullable = false)
  @NotNull(message = "An content is required!")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tbl_comment_images", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
  private Set<Image> imageGallery = new HashSet<>();

  public void addImage(Image image) {
    imageGallery.add(image);
  }

  public void removeImage(Image image) {
    imageGallery.remove(image);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rely_for_user_id")
  private User relyForUser;

  @ManyToOne
  @JoinColumn(name = "main_comment_id")
  private Comment mainComment;

  @OneToMany(mappedBy = "mainComment", cascade = CascadeType.ALL)
  private Set<Comment> childComments = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "main_feedback_id")
  private Feedback mainFeedback;

  @Enumerated(EnumType.STRING)
  @Column(name = "comment_type", length = 50, nullable = false)
  @NonNull
  private ECommentType commentType;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  // public Comment(Product product, User author, String content, User
  // relyForUser, Comment mainComment,
  // Feedback mainFeedback) {
  // this.content = content;
  // this.product = product;
  // this.author = author;
  // this.mainComment = mainComment;
  // this.mainFeedback = mainFeedback;
  // if (mainFeedback == null) {
  // this.commentType = ECommentType.COMMENT;
  // if (mainComment != null) {
  // this.relyForUser = mainComment.getAuthor();
  // }
  // } else {
  // this.relyForUser = mainComment.getAuthor();
  // this.commentType = ECommentType.FEEDBACK;
  // }
  // }
  //
  public void setMainComment(Product product, User author, String content) {
    this.content = content;
    this.product = product;
    this.author = author;
    this.commentType = ECommentType.COMMENT;
  }

  public void setChildComment(Comment mainComment, User author, String content) {
    this.content = content;
    this.product = mainComment.getProduct();
    this.author = author;
    this.mainComment = mainComment;
    this.relyForUser = mainComment.getAuthor();
    this.commentType = ECommentType.COMMENT;
  }

  public void setChildFeedback(Feedback mainFeedback, User author, String content) {
    this.content = content;
    this.product = mainFeedback.getProduct();
    this.author = author;
    this.mainFeedback = mainFeedback;
    this.relyForUser = mainFeedback.getAuthor();
    this.commentType = ECommentType.FEEDBACK;
  }
}
