package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "jokes")
@Entity
@Setter
@Getter
public class Joke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "joke_id")
    private long id;
    @Column(name = "setup")
    private String setup;
    @Column(name = "punchline")
    private String punchline;
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ONE;
    @Column(name = "times_bought")
    private long timesBought;
    @Column(name = "rating")
    private double rating;
    @Column(name = "created_by")
    private double createdBy;
}
