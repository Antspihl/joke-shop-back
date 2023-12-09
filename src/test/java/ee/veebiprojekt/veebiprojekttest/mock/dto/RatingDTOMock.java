package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;

public class RatingDTOMock {

    public static RatingDTO shallowRatingDTO(long jokeId) {
        return new RatingDTO(jokeId, 5);
    }
}
