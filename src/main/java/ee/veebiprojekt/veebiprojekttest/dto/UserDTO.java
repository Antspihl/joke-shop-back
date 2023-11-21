package ee.veebiprojekt.veebiprojekttest.dto;

public record UserDTO(Long userId, String username, String passwordHash, String email, String fullName) {
}
