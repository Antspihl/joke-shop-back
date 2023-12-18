package ee.veebiprojekt.veebiprojekttest.repository;

import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT e FROM Rating e WHERE e.jokeId = :jokeId")
    List<Rating> findAllByJokeId(@Param("jokeId") long id);

    @Query("SELECT e FROM Rating e WHERE e.authorId = :userId")
    List<Rating> findAllByAuthorId(Long userId);

    @Query("SELECT e FROM Rating e WHERE e.jokeId = :jokeId AND e.authorId = :authorId")
    Rating getJokeRating(long jokeId, long authorId);
}
