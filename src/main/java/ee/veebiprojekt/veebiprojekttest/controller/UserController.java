package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userService.getUserInfo(username);
    }

    @GetMapping("/all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/username/{username}/password/{passwordHash}")
    public boolean checkPasswordByUsername(@PathVariable String username, @PathVariable String passwordHash) {
        return userService.checkPasswordByUsername(username, passwordHash);
    }

    @GetMapping("/email/{email}/password/{passwordHash}")
    public boolean checkPasswordByEmail(@PathVariable String email, @PathVariable String passwordHash) {
        return userService.checkPasswordByEmail(email, passwordHash);
    }
}
