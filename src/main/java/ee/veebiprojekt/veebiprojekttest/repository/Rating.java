package ee.veebiprojekt.veebiprojekttest.repository;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "ratings")
@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long productId;
    private int rating;
}
