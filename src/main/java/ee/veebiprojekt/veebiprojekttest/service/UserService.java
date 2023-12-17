package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.*;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.entity.UserRole;
import ee.veebiprojekt.veebiprojekttest.exception.FieldNotUniqueException;
import ee.veebiprojekt.veebiprojekttest.mapper.UserMapper;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRoleRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Key jwtSecretKey = Keys.hmacShaKeyFor("Kui on meri hülgehall, ja sind ründamas suur hall".getBytes());

    public UserService(UserRepository userRepository, UserMapper userMapper, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(RegisterDTO registerDTO) {
        log.debug("Registering user: {}", registerDTO);
        if (registerDTO.username() == null || registerDTO.password() == null ||
                registerDTO.email() == null || registerDTO.fullName() == null) {
            log.debug("UserDTO has null values");
            throw new IllegalArgumentException("UserDTO has null values");
        }
        if (userRepository.findByUsername(registerDTO.username()) != null) {
            log.debug("Username already exists");
            throw new FieldNotUniqueException("username");
        }
        if (userRepository.findByEmail(registerDTO.email()) != null) {
            log.debug("Email already exists");
            throw new FieldNotUniqueException("email");
        }

        UserDTO userDto = new UserDTO(
                null, registerDTO.username(),
                registerDTO.password(), registerDTO.email(), false, registerDTO.fullName());
        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(registerDTO.password()));

        userRepository.save(user);
        UserRole userRole = new UserRole(user.getUserId(), 2L);
        userRoleRepository.save(userRole);
        log.debug("Registered user: {}", user);
        return userMapper.toDTO(user);
    }

    public UserDTO getUserInfo(String username) {
        log.debug("Getting user info for user: {}", username);
        User user = userRepository.findByUsername(username);
        user.setPasswordHash(null);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> getUsers() {
        log.debug("Getting all users");
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPasswordHash(null));
        return userMapper.toDTOList(users);
    }

    public String login(LoginDTO loginDto) {
        log.debug("Logging in user: {}", loginDto.username());
        String username = loginDto.username();
        String password = loginDto.password();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.debug("User not found");
            throw new IllegalArgumentException("Incorrect username");
        }
        boolean correctPassword = passwordEncoder.matches(password, user.getPasswordHash());
        if (!correctPassword) {
            log.debug("Incorrect password");
            throw new IllegalArgumentException("Incorrect password");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        String jwt = Jwts.builder()
                .setSubject("naljapood")
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
        log.debug("Logged in user: {}", username);
        return jwt;
    }

    public List<UserDTO> getUsersPaginated(UserSearchDTO userSearchDTO) {
        log.debug("Getting all users");
        userSearchDTO.getSpecification();
        List<User> users = userRepository.findAll(userSearchDTO.getSpecification(), userSearchDTO.getPageable()).getContent();
        users.forEach(user -> user.setPasswordHash(null));
        return userMapper.toDTOList(users);
    }

    public UsersPageResponseDto getUserPageResponse(UserSearchDTO userSearchDTO) {
        List<UserDTO> userList = getUsersPaginated(userSearchDTO);
        return UsersPageResponseDto.builder()
                .pageUsers(userList)
                .totalUsersCount(userRepository.count())
                .build();
    }

    public void deleteUser(Long userId) {
        log.debug("Deleting user: {}", userId);
        userRepository.deleteById(userId);
        UserRole userRole = userRoleRepository.getUserRoleByUserId(userId);
        userRoleRepository.delete(userRole);
    }
}
