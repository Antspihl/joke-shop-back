package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Data;


@Table(name = "cart_items")
@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @Column(name = "cart_id")
    private long cartId;
    @Column(name = "joke_id")
    private long jokeId;
}
