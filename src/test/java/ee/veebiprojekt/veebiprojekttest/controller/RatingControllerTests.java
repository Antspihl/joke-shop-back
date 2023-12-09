package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.exception.JokeShopExceptionHandler;
import ee.veebiprojekt.veebiprojekttest.exception.WrongOwnerException;
import ee.veebiprojekt.veebiprojekttest.mock.dto.RatingDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.RatingMock;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import ee.veebiprojekt.veebiprojekttest.service.RatingService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatingControllerTests {

    @MockBean
    private RatingService RatingService;

    @MockBean
    private JokeShopExceptionHandler jokeShopExceptionHandler;

    @Mock
    private RatingRepository RatingRepository;

    private static Rating testRating;
    private static RatingDTO testRatingDTO;

    private final String username = "user";

    @LocalServerPort
    private int port;

    private static String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        testRating = RatingMock.shallowRating(1L);
        testRatingDTO = RatingDTOMock.shallowRatingDTO(1L);

        jwtToken = given()
                .contentType("application/json")
                .body("{\"username\":\"user\",\"password\":\"user\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();
    }

    @Test
    void addRating() {
        when(RatingService.addRating(testRatingDTO, username)).thenReturn(testRatingDTO);
        when(RatingRepository.save(testRating)).thenReturn(testRating);
        when(RatingRepository.findById(testRatingDTO.jokeId())).thenReturn(java.util.Optional.ofNullable(testRating));


        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(testRatingDTO)
                .when()
                .post("/api/ratings")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getJokeRating() {
        when(RatingService.getJokeRating(testRatingDTO.jokeId())).thenReturn(5.0);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .when()
                .get("/api/ratings/" + testRatingDTO.jokeId())
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getRatings() {
        when(RatingService.getRatings()).thenReturn(java.util.List.of(testRating));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .when()
                .get("/api/ratings")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void editRating() {
        when(RatingService.editRating(testRatingDTO, testRatingDTO.jokeId(), username)).thenReturn(testRatingDTO);
        when(RatingRepository.findById(testRatingDTO.jokeId())).thenReturn(java.util.Optional.ofNullable(testRating));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(testRatingDTO)
                .when()
                .put("/api/ratings/" + testRatingDTO.jokeId())
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteRating() {
        when(RatingService.deleteRating(testRatingDTO.jokeId(), username)).thenReturn(testRatingDTO);
        when(RatingRepository.findById(testRatingDTO.jokeId())).thenReturn(java.util.Optional.ofNullable(testRating));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .when()
                .delete("/api/ratings/" + testRatingDTO.jokeId())
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void addRatingUnauthorized() {
        when(RatingService.addRating(testRatingDTO, username))
                .thenThrow(new WrongOwnerException("Rating", testRatingDTO.jokeId(), 1L));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(testRatingDTO)
                .when()
                .post("/api/ratings");


        verify(jokeShopExceptionHandler).handleUserException(any(WrongOwnerException.class));
    }
}
