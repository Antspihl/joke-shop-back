package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
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
        // todo throw exceptions if jokeId or ratingValue are invalid
        /* if (jokeRepository.findById(ratingDTO.jokeId()).isEmpty()) {
            return "No such product";
        }
        if (ratingDTO.ratingValue() < 1 || ratingDTO.ratingValue() > 5) {
            return "Rating must be between 1 and 5";
        }*/
        Rating rating = new Rating();
        rating.setRatingValue(ratingDTO.ratingValue());
        rating.setJokeId(ratingDTO.jokeId());
        ratingRepository.save(rating);
        return ratingDTO;
    }
}
