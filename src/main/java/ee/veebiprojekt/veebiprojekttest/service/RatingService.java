package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.exception.InvalidValueException;
import ee.veebiprojekt.veebiprojekttest.mapper.RatingMapper;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    public RatingDTO addRating(RatingDTO ratingDTO) {
        if (ratingRepository.findById(ratingDTO.jokeId()).isEmpty()) {
            throw new EntityNotFoundException(ratingDTO.jokeId());
        }
        if (ratingDTO.ratingValue() < 1 || ratingDTO.ratingValue() > 5) {
            throw new InvalidValueException(ratingDTO.ratingValue());
        }
        Rating rating = new Rating();
        rating.setRatingValue(ratingDTO.ratingValue());
        rating.setJokeId(ratingDTO.jokeId());
        ratingRepository.save(rating);
        return ratingDTO;
    }

    public double getJokeRating(Long jokeId) {
        List<Rating> ratings = ratingRepository.findAllByJokeId(jokeId);
        return ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
    }

    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }
}
