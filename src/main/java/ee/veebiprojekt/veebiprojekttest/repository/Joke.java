package ee.veebiprojekt.veebiprojekttest.repository;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "jokes")
@Entity
@Data
public class Joke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="setup")
    private String setup;
    @Column(name="punchline")
    private String punchline;
}
