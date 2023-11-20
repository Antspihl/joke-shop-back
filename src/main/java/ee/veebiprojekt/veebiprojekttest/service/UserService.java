package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.LoginDto;
import ee.veebiprojekt.veebiprojekttest.dto.RegisterDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.FieldNotUniqueException;
import ee.veebiprojekt.veebiprojekttest.mapper.UserMapper;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Key jwtSecretKey = Keys.hmacShaKeyFor("Kui on meri hülgehall, ja sind ründamas suur hall".getBytes());

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(RegisterDTO registerDTO) {
        if (registerDTO.username() == null || registerDTO.password() == null ||
                registerDTO.email() == null || registerDTO.fullName() == null) {
            throw new IllegalArgumentException("UserDTO has null values");
        }
        if (userRepository.findByUsername(registerDTO.username()) != null) {
            throw new FieldNotUniqueException(registerDTO.username());
        }
        if (userRepository.findByEmail(registerDTO.email()) != null) {
            throw new FieldNotUniqueException(registerDTO.email());
        }
        UserDTO userDto = new UserDTO(
                null, registerDTO.username(),
                registerDTO.password(), registerDTO.email(), registerDTO.fullName());
        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(registerDTO.password()));
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDTO getUserInfo(String username) {
        User user = userRepository.findByUsername(username);
        user.setPasswordHash(null);
        return userMapper.toDTO(user);
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPasswordHash(null));
        return userMapper.toDTOList(users);
    }

    public String login(LoginDto loginDto) {
        String username = loginDto.username();
        String password = loginDto.password();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Incorrect username");
        }
        boolean correctPassword = passwordEncoder.matches(password, user.getPasswordHash());
        if (!correctPassword) {
            throw new IllegalArgumentException("Incorrect password");
        }
        Map<String, Object> claims = Map.of("username", username);
        return Jwts.builder()
                .setSubject("naljapood")
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
