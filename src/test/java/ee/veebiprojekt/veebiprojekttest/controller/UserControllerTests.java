package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.LoginDTO;
import ee.veebiprojekt.veebiprojekttest.dto.RegisterDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.mock.dto.LoginDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.dto.RegisterDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.dto.UserDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.UserMock;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {

    @MockBean
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private static User testUser;
    private static UserDTO testUserDTO;
    private static List<UserDTO> testUserDTOList;
    private static String testJwtToken;
    private static RegisterDTO testRegisterDTO;
    private static LoginDTO testLoginDTO;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        testUser = UserMock.shallowUser(1L);
        testUserDTO = UserDTOMock.shallowUserDTO(1L);
        testJwtToken = "testJwtToken";
        testRegisterDTO = RegisterDTOMock.shallowRegisterDTO();
        testLoginDTO = LoginDTOMock.shallowLoginDTO();
        testUserDTOList = new ArrayList<>();
        testUserDTOList.add(testUserDTO);
    }

    @Test
    void testRegister() {
        when(userService.register(any())).thenReturn(testUserDTO);
        when(userRepository.save(any())).thenReturn(testUser);

        given()
                .contentType("application/json")
                .body(testRegisterDTO)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testLogin() {
        when(userService.login(any())).thenReturn(testJwtToken);
        when(userRepository.save(any())).thenReturn(testUser);

        given()
                .contentType("application/json")
                .body(testLoginDTO)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testGetUsers() {
        when(userService.getUsers()).thenReturn(testUserDTOList);
        when(userRepository.save(any())).thenReturn(testUser);

        given()
                .contentType("application/json")
                .body(testUserDTO)
                .when()
                .get("/api/users/all")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
