package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;

public class UserDTOMock {

    public static UserDTO shallowUserDTO(Long id) {
        return new UserDTO(id, "user", "password", "email", "Full Name");
    }
}
