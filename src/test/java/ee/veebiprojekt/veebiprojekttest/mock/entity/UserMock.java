package ee.veebiprojekt.veebiprojekttest.mock.entity;

import ee.veebiprojekt.veebiprojekttest.entity.User;


public class UserMock {
    public static User shallowUser(Long id) {
        User user = new User();
        user.setUserId(id);
        user.setUsername("user");
        user.setPasswordHash("password");
        user.setEmail("email");
        user.setFullName("fullname");
        return user;
    }
}
