package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "shop_carts")
@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long cartId;
    @Column(name = "user_id")
    private long userId;
}
