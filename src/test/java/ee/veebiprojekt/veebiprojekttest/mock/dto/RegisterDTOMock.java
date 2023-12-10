package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.RegisterDTO;

public class RegisterDTOMock {

    public static RegisterDTO shallowRegisterDTO() {
        return new RegisterDTO(
                "username",
                "password",
                "email",
                "fullName"
        );
    }

    public static RegisterDTO illegalRegisterDTO() {
        return new RegisterDTO(
                "username",
                null,
                "email",
                "fullName"
        );
    }
}
