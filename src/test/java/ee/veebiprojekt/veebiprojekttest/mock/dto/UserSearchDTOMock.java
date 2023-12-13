package ee.veebiprojekt.veebiprojekttest.mock.dto;

import ee.veebiprojekt.veebiprojekttest.dto.UserSearchDTO;

public class UserSearchDTOMock {

    public static UserSearchDTO mockUserSearchDTO(String username, String email, String fullName, Long id) {
        return new UserSearchDTO(id, username, email, fullName, 10, 0, "id", null);
    }
}
