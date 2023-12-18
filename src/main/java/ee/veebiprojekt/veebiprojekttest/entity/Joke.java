package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "jokes")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Builder.Default
    private BigDecimal price = BigDecimal.ONE;
    @Column(name = "times_bought")
    private long timesBought;
    @Column(name = "rating")
    private double rating;
    @Column(name = "created_by")
    private long createdBy;
}
