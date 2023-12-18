package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
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

    public RatingDTO addRating(RatingDTO ratingDTO, String username) {
        log.debug("Adding rating: {} by {}", ratingDTO, username);
        User user = userRepository.findByUsername(username);
        Rating rating = ratingRepository.getJokeRating(ratingDTO.jokeId(), user.getUserId());
        if (rating != null) {
            return editRating(ratingDTO, rating.getId(), username);
        }
        rating = Rating.builder()
                .ratingValue(ratingDTO.ratingValue())
                .authorId(user.getUserId())
                .jokeId(ratingDTO.jokeId())
                .build();
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

    public void deleteAllRatingFromUser(Long userId) {
        log.debug("Deleting all ratings from user with id {}", userId);
        List<Rating> ratings = ratingRepository.findAllByAuthorId(userId);
        ratings.forEach(rating -> ratingRepository.deleteById(rating.getId()));
        log.debug("Deleted all ratings from user with id {}", userId);
    }
}
