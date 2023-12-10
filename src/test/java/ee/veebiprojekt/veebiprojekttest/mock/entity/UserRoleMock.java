package ee.veebiprojekt.veebiprojekttest.mock.entity;

import ee.veebiprojekt.veebiprojekttest.entity.UserRole;

public class UserRoleMock {

    public static UserRole getUserRole(Long id) {
        return new UserRole(
                id,
                2L
        );
    }


}
