package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.mapper.UserMapper;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserDTO getUserInfo(String username) {
        User user = userRepository.findByUsername(username);
        user.setPasswordHash(null);
        return userMapper.toDTO(user);
    }

    public boolean checkPasswordByUsername(String username, String passwordHash) {
        User user = userRepository.findByUsername(username);
        return user.getPasswordHash().equals(passwordHash);
    }

    public boolean checkPasswordByEmail(String email, String passwordHash) {
        User user = userRepository.findByEmail(email);
        return user.getPasswordHash().equals(passwordHash);
    }
}
