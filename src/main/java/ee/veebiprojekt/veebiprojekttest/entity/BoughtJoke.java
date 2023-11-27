package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "bought_jokes")
@Entity
@Setter
@Getter
public class BoughtJoke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bought_joke_id")
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "joke_id")
    private long jokeId;
}
