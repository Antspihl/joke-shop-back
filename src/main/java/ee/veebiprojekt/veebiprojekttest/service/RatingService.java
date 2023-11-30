package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.exception.InvalidValueException;
import ee.veebiprojekt.veebiprojekttest.exception.WrongOwnerException;
import ee.veebiprojekt.veebiprojekttest.mapper.RatingMapper;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final JokeService jokeService;
    private final UserRepository userRepository;
    private static final String RATING = "Rating";

    public RatingService(RatingRepository ratingRepository, RatingMapper ratingMapper, JokeService jokeService, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.jokeService = jokeService;
        this.userRepository = userRepository;
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
        log.debug("Authenticated rating: {}", rating);
        return rating;
    }

    public RatingDTO addRating(RatingDTO ratingDTO, String username) {
        log.debug("Adding rating: {} by {}", ratingDTO, username);
        User user = userRepository.findByUsername(username);
        Rating rating = authRatingDto(ratingDTO);
        rating.setAuthorId(user.getUserId());
        ratingRepository.save(rating);
        jokeService.recalculateJokeRating(ratingDTO.jokeId());
        log.debug("Added rating: {} by {}", rating, username);
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

    public RatingDTO editRating(RatingDTO ratingDTO, long id, String username) {
        log.debug("Editing rating with id {}", id);
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(RATING, id));
        User user = userRepository.findByUsername(username);
        verifyRatingOwner(rating, user);
        if (ratingDTO.ratingValue() != null) rating.setRatingValue(ratingDTO.ratingValue());
        if (ratingDTO.jokeId() != null) rating.setJokeId(ratingDTO.jokeId());
        ratingRepository.save(rating);
        jokeService.recalculateJokeRating(rating.getJokeId());
        log.debug("Edited rating: {}", rating);
        return ratingMapper.toDTO(rating);
    }

    public RatingDTO deleteRating(Long id, String username) {
        log.debug("Deleting rating with id {}", id);
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(RATING, id));
        User user = userRepository.findByUsername(username);
        verifyRatingOwner(rating, user);
        ratingRepository.deleteById(id);
        jokeService.recalculateJokeRating(rating.getJokeId());
        log.debug("Deleted rating: {}", rating);
        return ratingMapper.toDTO(rating);
    }

    private void verifyRatingOwner(Rating rating, User user) {
        if (rating.getAuthorId() != user.getUserId()) {
            log.debug("User {} is not the author of rating with id {}", user.getFullName(), rating.getId());
            throw new WrongOwnerException(RATING, rating.getId(), user.getUserId());
        }
    }
}
