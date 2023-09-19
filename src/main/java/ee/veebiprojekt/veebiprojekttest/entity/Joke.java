package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "jokes")
@Entity
@Data
public class Joke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="joke_id")
    private long id;
    @Column(name="setup")
    private String setup;
    @Column(name="punchline")
    private String punchline;
}
