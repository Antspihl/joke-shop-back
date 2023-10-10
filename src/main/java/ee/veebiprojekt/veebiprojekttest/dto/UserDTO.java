package ee.veebiprojekt.veebiprojekttest.dto;

public record UserDTO(Long user_id, String username, String password_hash, String email, boolean isAdmin, String fullName, String salt) {
}
