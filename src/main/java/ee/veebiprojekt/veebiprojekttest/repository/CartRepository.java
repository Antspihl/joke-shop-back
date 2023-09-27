package ee.veebiprojekt.veebiprojekttest.repository;

import ee.veebiprojekt.veebiprojekttest.entity.Cart;
import ee.veebiprojekt.veebiprojekttest.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select item from CartItem item where item.cartId = :cartId")
    List<CartItem> findCartItemsByCartId(@Param("cartId") long id);
}
