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
}
