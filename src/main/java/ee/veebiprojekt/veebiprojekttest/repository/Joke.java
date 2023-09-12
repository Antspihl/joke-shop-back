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
    private String setup;
    private String punchline;
}
