package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.LoginDto;
import ee.veebiprojekt.veebiprojekttest.dto.RegisterDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterDTO registerDTO) {
        log.debug("REST request to register user: {}", registerDTO);
        return userService.register(registerDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        log.debug("REST request to login user: {}", loginDto);
        return userService.login(loginDto);
    }
}
