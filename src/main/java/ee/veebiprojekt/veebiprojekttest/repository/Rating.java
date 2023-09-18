package ee.veebiprojekt.veebiprojekttest.repository;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "ratings")
@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rating_id")
    private long id;
    @Column(name="joke_id")
    private Long productId;
    @Column(name="rating_value")
    private int ratingValue;
}
