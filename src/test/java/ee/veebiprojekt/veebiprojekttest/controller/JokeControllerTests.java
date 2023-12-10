package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.exception.JokeShopExceptionHandler;
import ee.veebiprojekt.veebiprojekttest.mock.dto.JokeDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.JokeMock;
import ee.veebiprojekt.veebiprojekttest.repository.BoughtJokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import ee.veebiprojekt.veebiprojekttest.service.JokeService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JokeControllerTests {

    @MockBean
    private JokeService jokeService;

    @MockBean
    private JokeShopExceptionHandler jokeShopExceptionHandler;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private BoughtJokeRepository boughtJokeRepository;

    @Mock
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private static Joke testJoke;
    private static JokeDTO testJokeDTO;
    private static JokeDTO editedJokeDTO;
    private static JokeDTO setupJokeDTO;
    
    private static String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        testJoke = JokeMock.shallowJoke(1L);
        testJokeDTO = JokeDTOMock.shallowJokeDTO(1L);
        editedJokeDTO = JokeDTOMock.shallowEditedJokeDTO(2L);
        setupJokeDTO = JokeDTOMock.shallowSetupJokeDTO(3L);
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
    void testGetSetups() {
        when(jokeService.getSetups()).thenReturn(Collections.singletonList(setupJokeDTO));
        when(jokeRepository.findAll()).thenReturn(Collections.singletonList(testJoke));

        given()
                .when()
                .get("/api/jokes/setups")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo((int) setupJokeDTO.id()))
                .body("[0].setup", equalTo(setupJokeDTO.setup()))
                .body("[0].punchline", equalTo(null));
    }

    @Test
    void testGetTop3() {
        when(jokeService.getTop3()).thenReturn(Collections.singletonList(testJokeDTO));
        when(jokeRepository.findAllByOrderByTimesBoughtDesc()).thenReturn(Collections.singletonList(testJoke));

        given()
                .when()
                .get("/api/jokes/top3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo((int) testJokeDTO.id()))
                .body("[0].setup", equalTo(testJokeDTO.setup()))
                .body("[0].punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    void testGetTop3WithAuth() {
        when(jokeService.getTop3("user")).thenReturn(Collections.singletonList(testJokeDTO));
        when(jokeRepository.findAllByOrderByTimesBoughtDesc()).thenReturn(Collections.singletonList(testJoke));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/top3")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo((int) testJokeDTO.id()))
                .body("[0].setup", equalTo(testJokeDTO.setup()))
                .body("[0].punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    void testAddJoke() {
        when(jokeService.addJoke(testJokeDTO, "user")).thenReturn(testJokeDTO);
        when(jokeRepository.save(testJoke)).thenReturn(testJoke);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .contentType("application/json")
                .body(testJokeDTO)
                .post("/api/jokes/add")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo((int) testJokeDTO.id()))
                .body("setup", equalTo(testJokeDTO.setup()))
                .body("punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    void testBuyJoke() {
        when(jokeService.buyJoke(1L, "user")).thenReturn(testJokeDTO);
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testJoke));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/buy/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo((int) testJokeDTO.id()))
                .body("setup", equalTo(testJokeDTO.setup()))
                .body("punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void testGetJokeById() {
        when(jokeService.getJoke(1L)).thenReturn(testJokeDTO);
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testJoke));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/get/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo((int) testJokeDTO.id()))
                .body("setup", equalTo(testJokeDTO.setup()))
                .body("punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void testEditJoke() {
        when(jokeService.editJoke(1L, editedJokeDTO)).thenReturn(editedJokeDTO);
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testJoke));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .contentType("application/json")
                .body(editedJokeDTO)
                .put("/api/jokes/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo((int) editedJokeDTO.id()))
                .body("setup", equalTo(editedJokeDTO.setup()))
                .body("punchline", equalTo(editedJokeDTO.punchline()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void testDeleteJoke() {
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testJoke));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/api/jokes/1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testGetBoughtJokes() {
        when(jokeService.getBoughtJokes("user")).thenReturn(Collections.singletonList(testJokeDTO));
        when(jokeRepository.findAll()).thenReturn(Collections.singletonList(testJoke));
        when(userRepository.findByUsername("user")).thenReturn(null);
        when(boughtJokeRepository.findJokeIdsByUserId(1L)).thenReturn(Collections.singletonList(1L));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/bought")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo((int) testJokeDTO.id()))
                .body("[0].setup", equalTo(testJokeDTO.setup()))
                .body("[0].punchline", equalTo(testJokeDTO.punchline()));
    }

    @Test
    void testGetSetupsWithAuth() {
        when(jokeService.getSetups("user")).thenReturn(Collections.singletonList(setupJokeDTO));
        when(jokeRepository.findAll()).thenReturn(Collections.singletonList(testJoke));
        when(userRepository.findByUsername("user")).thenReturn(null);
        when(boughtJokeRepository.findJokeIdsByUserId(1L)).thenReturn(Collections.singletonList(1L));

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/setups")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo((int) setupJokeDTO.id()))
                .body("[0].setup", equalTo(setupJokeDTO.setup()))
                .body("[0].punchline", equalTo(setupJokeDTO.punchline()));
    }

    @Test
    void testGetJokeThrowNotFound() {
        when(jokeService.getJoke(1L))
                .thenThrow(new EntityNotFoundException("Joke", 1L));
        when(jokeRepository.findById(1L)).thenReturn(Optional.empty());

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/get/1");

        verify(jokeShopExceptionHandler).handleUserException(any(EntityNotFoundException.class));
    }

    @Test
    void testEditJokeThrowNotFound() {
        when(jokeService.editJoke(1L, editedJokeDTO))
                .thenThrow(new EntityNotFoundException("Joke", 1L));
        when(jokeRepository.findById(1L)).thenReturn(Optional.empty());

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .contentType("application/json")
                .body(editedJokeDTO)
                .put("/api/jokes/1");

        verify(jokeShopExceptionHandler).handleUserException(any(EntityNotFoundException.class));
    }

    @Test
    void testBuyJokeThrowNotFound() {
        when(jokeService.buyJoke(1L, "user"))
                .thenThrow(new EntityNotFoundException("Joke", 1L));
        when(jokeRepository.findById(1L)).thenReturn(Optional.empty());

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/jokes/buy/1");

        verify(jokeShopExceptionHandler).handleUserException(any(EntityNotFoundException.class));
    }

}