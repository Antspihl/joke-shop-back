package ee.veebiprojekt.veebiprojekttest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsersPageResponseDto {
    private List<UserDTO> pageUsers;
    private Long totalUsersCount;
}
