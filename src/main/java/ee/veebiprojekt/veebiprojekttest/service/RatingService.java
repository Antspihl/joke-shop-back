package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.exception.InvalidValueException;
import ee.veebiprojekt.veebiprojekttest.mapper.RatingMapper;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final JokeService jokeService;
    private static final String RATING = "Rating";

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper, JokeService jokeService) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.jokeService = jokeService;
    }

    private Rating authRatingDto(RatingDTO ratingDTO) {
        log.debug("Authenticating ratingDTO: {}", ratingDTO);
        if (ratingRepository.findById(ratingDTO.jokeId()).isEmpty()) {
            log.debug("Joke with id {} not found", ratingDTO.jokeId());
            throw new EntityNotFoundException("Joke", ratingDTO.jokeId());
        }
        if (ratingDTO.ratingValue() < 1 || ratingDTO.ratingValue() > 5) {
            log.debug("Rating value {} is invalid", ratingDTO.ratingValue());
            throw new InvalidValueException(RATING, "value", ratingDTO.ratingValue());
        }
        Rating rating = new Rating();
        rating.setRatingValue(ratingDTO.ratingValue());
        rating.setJokeId(ratingDTO.jokeId());
        rating.setAuthorId(ratingDTO.authorId());
        log.debug("Authenticated rating: {}", rating);
        return rating;
    }

    public RatingDTO addRating(RatingDTO ratingDTO) {
        log.debug("Adding rating: {}", ratingDTO);
        Rating rating = authRatingDto(ratingDTO);
        ratingRepository.save(rating);
        jokeService.recalculateJokeRating(ratingDTO.jokeId());
        log.debug("Added rating: {}", rating);
        return ratingDTO;
    }

    public double getJokeRating(Long jokeId) {
        log.debug("Getting rating for joke with id {}", jokeId);
        List<Rating> ratings = ratingRepository.findAllByJokeId(jokeId);
        return ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
    }

    public List<Rating> getRatings() {
        log.debug("Getting all ratings");
        return ratingRepository.findAll();
    }

    public RatingDTO editRating(RatingDTO ratingDTO, long id) {
        log.debug("Editing rating with id {}", id);
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(RATING, id));
        if (ratingDTO.ratingValue() != null) rating.setRatingValue(ratingDTO.ratingValue());
        if (ratingDTO.jokeId() != null) rating.setJokeId(ratingDTO.jokeId());
        ratingRepository.save(rating);
        jokeService.recalculateJokeRating(rating.getJokeId());
        log.debug("Edited rating: {}", rating);
        return ratingMapper.toDTO(rating);
    }

    public RatingDTO deleteRating(Long id) {
        log.debug("Deleting rating with id {}", id);
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(RATING, id));
        ratingRepository.deleteById(id);
        jokeService.recalculateJokeRating(rating.getJokeId());
        log.debug("Deleted rating: {}", rating);
        return ratingMapper.toDTO(rating);
    }
}
