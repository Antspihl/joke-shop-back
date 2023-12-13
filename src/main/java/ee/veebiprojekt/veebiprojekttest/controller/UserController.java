package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserSearchDTO;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username, Principal principal) {
        log.debug("REST request to get user: {}", username);
        return userService.getUserInfo(username, principal.getName());
    }

    @GetMapping("/all")
    public List<UserDTO> getUsers() {
        log.debug("REST request to get all users");
        return userService.getUsers();
    }


    @GetMapping(produces = {"application/json"}, consumes = {"application/json"})
    public List<UserDTO> getUsersPage(@RequestBody UserSearchDTO userSearchDTO) {
        log.debug("REST request to get all users");
        return userService.getUsersPaginated(userSearchDTO);
    }
}
