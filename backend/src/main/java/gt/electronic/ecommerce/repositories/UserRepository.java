package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:14 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface UserRepository extends SearchRepository<User, Long> {

  @Query(value = "select u from User u where :keyword like '' "
      + "or (lower(concat(u.id, ' ', u.username, ' ', u.phone)) like lower(concat('%', :keyword,'%'))) "
      + "or (u.email is not null and u.email like lower(concat('%', :keyword,'%'))) "
      + "or (u.firstName is not null and lower(u.firstName) like lower(concat('%', :keyword,'%'))) "
      + "or (u.lastName is not null and lower(u.lastName) like lower(concat('%', :keyword,'%')))")
  Page<User> findAll(String keyword, Pageable pageable);

  List<User> findUsersByUsername(String username);

  List<User> findUsersByEmail(String email);

  List<User> findUsersByPhone(String phone);

  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByPhone(String phone);

  Optional<User> findByPhone(String phone);

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Boolean existsByPhone(String phone);

  Boolean existsByUsername(String username);
}
