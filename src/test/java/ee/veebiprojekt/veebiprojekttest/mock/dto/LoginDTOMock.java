package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.LoginDTO;

public class LoginDTOMock {

    public static LoginDTO shallowLoginDTO() {
        return new LoginDTO(
                "username",
                "password"
        );
    }
}
