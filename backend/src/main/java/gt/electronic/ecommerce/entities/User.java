package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.AuthProvider;
import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:08 PM
 * @project gt-backend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User implements OAuth2User, UserDetails {
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 20, nullable = false, unique = true)
    @NotNull(message = "An username is required!")
    private String username;

    // private boolean isChangedUsername;

    @Column(name = "password", length = 64)
    // @NotNull(message = "An password is required!")
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    @NotNull(message = "An firstname is required!")
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    @NotNull(message = "An lastname is required!")
    private String lastName;

    @Column(name = "email", length = 320, unique = true)
    @Size(message = "Invalid email size.", max = 320, min = 10)
    // @NotNull(message = "An email is required!")
    @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
    private String email;

    @Column(name = "is_email_verified", nullable = false)
    @NotNull(message = "An isEmailVerified is required!")
    private boolean isEmailVerified = false;

    @Column(name = "phone", length = 13, unique = true)
    @Size(message = "Invalid phone size.", max = 13, min = 9)
    // @NotNull(message = "An phone is required!")
    @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
    private String phone;

    @Column(name = "is_phone_verified", nullable = false)
    @NotNull(message = "An isPhoneVerified is required!")
    private boolean isPhoneVerified = false;

    @Column(name = "identity_card", length = 12, unique = true)
    @Size(message = "Invalid identityCard size.", max = 12, min = 9)
    private String identityCard;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 50, nullable = false)
    @NotNull(message = "An gender is required!")
    private EGender gender = EGender.UNKNOWN;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Shop shop;

    @Column(name = "enabled", nullable = false)
    @NotNull(message = "An enabled is required!")
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar")
    private Image avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    @NotNull(message = "An role is required!")
    private ERole role = ERole.ROLE_CUSTOMER;

    @OneToMany(mappedBy = "relyForUser", cascade = CascadeType.ALL)
    private Set<Comment> commentReliedList = new HashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.local;

    private String providerId;

    @Transient
    private Map<String, Object> attributes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_discounts", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts = new HashSet<>();

    public void addDiscount(Discount newDiscount) {
        this.discounts.add(newDiscount);
    }

    public void removeDiscount(Discount discount) {
        this.discounts.remove(discount);
    }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // List<SimpleGrantedAuthority> authories = new ArrayList<>();
    // for (Role role : roles) {
    // authories.add(new SimpleGrantedAuthority(role.getName().toString()));
    // }
    //
    // return authories;
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role + ""));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @PreRemove
    private void preRemove() {
        commentReliedList.forEach(child -> child.setRelyForUser(null));
        this.discounts = new HashSet<>();
    }

    public User(Long id, String username, String email, String password, ERole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User create(User user) {

        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    public static User create(User user, Map<String, Object> attributes) {
        User userPrincipal = User.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User that))
            return false;
        return this.id.equals(that.getId());
    }
}
