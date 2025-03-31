package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.ProductBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ProcedureRepository extends JpaRepository<Product, Long> {

    @Query(value = "CALL updateBlackListProduct(:inShopId, :inStartDate, :inMinAll, :inMinNeg, :inStartNewSession);", nativeQuery = true)
    List<Object> updateBlackListProduct(@Param("inShopId") Long shopId,
                                        @Param("inStartDate") Date starDate,
                                        @Param("inMinAll") int minAll,
                                        @Param("inMinNeg") int minNeg,
                                        @Param("inStartNewSession") Date startNewSession);

    @Query(value = "CALL updateBlackProductStatus(:checkDate)", nativeQuery = true)
    List<Object> updateBlackProductStatus(@Param("checkDate") Date checkDate);

    @Query(value = "CALL updateShopPrice(:rangeDate);", nativeQuery = true)
    List<Object> updateShopPrice(@Param("rangeDate") int rangeDate);
}
