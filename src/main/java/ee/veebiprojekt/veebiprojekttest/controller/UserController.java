package ee.veebiprojekt.veebiprojekttest.controller;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserSearchDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UsersPageResponseDto;
import ee.veebiprojekt.veebiprojekttest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public UserDTO getUser(Principal principal) {
        log.debug("REST request to get user: {}", principal.getName());
        return userService.getUserInfo(principal.getName());
    }

    @GetMapping("/usersTable")
    public UsersPageResponseDto getUsersPage(@RequestParam(required = false) Long userId,
                                             @RequestParam(required = false, defaultValue = "") String username,
                                             @RequestParam(required = false, defaultValue = "") String email,
                                             @RequestParam(required = false, defaultValue = "") String fullName,
                                             @RequestParam(defaultValue = "10") Integer limit,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "userId") String sort,
                                             @RequestParam(defaultValue = "ASC") String dir) {
        UserSearchDTO userSearchDTO = UserSearchDTO.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .email(email)
                .fullName(fullName)
                .limit(limit)
                .page(page)
                .sort(sort)
                .dir(Sort.Direction.valueOf(dir))
                .build();
        log.debug("REST request to get all users");
        return userService.getUserPageResponse(userSearchDTO);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") long id) {
        log.debug("REST request to remove user: {}", id);
        userService.deleteUser(id);
    }
}
