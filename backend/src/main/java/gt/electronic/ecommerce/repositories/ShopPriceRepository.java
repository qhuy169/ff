package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.ShopPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ShopPriceRepository extends JpaRepository<ShopPrice, Long> {
}
