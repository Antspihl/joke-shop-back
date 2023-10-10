package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }
}
