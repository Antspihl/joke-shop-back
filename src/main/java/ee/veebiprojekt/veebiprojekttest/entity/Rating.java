package ee.veebiprojekt.veebiprojekttest.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "ratings")
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rating_id")
    private long id;
    @Column(name="joke_id")
    private long jokeId;
    @Column(name="rating_value")
    private int ratingValue;
    @Column(name="author_id")
    private long authorId;
}
