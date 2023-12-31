package ee.veebiprojekt.veebiprojekttest.mock.entity;

import ee.veebiprojekt.veebiprojekttest.entity.Rating;

public class RatingMock {

    public static Rating shallowRating(Long id){
        return Rating.builder().
                id(id).
                authorId(1L).
                jokeId(1L)
                .ratingValue(5)
                .build();
    }
}
