package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.FieldNotUniqueException;
import ee.veebiprojekt.veebiprojekttest.mapper.UserMapper;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO register(UserDTO userDTO) {
        //Check if userDTO has no null values
        if (userDTO.username() == null || userDTO.passwordHash() == null ||
                userDTO.email() == null || userDTO.fullName() == null) {
            throw new IllegalArgumentException("UserDTO has null values");
        }
        //Check if username is already taken
        if (userRepository.findByUsername(userDTO.username()) != null) {
            throw new FieldNotUniqueException(userDTO.username());
        }
        //Check if email is already taken
        if (userRepository.findByEmail(userDTO.email()) != null) {
            throw new FieldNotUniqueException(userDTO.email());
        }
        User user = userMapper.toEntity(userDTO);
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

    public boolean checkPasswordByUsername(String username, String passwordHash) {
        User user = userRepository.findByUsername(username);
        return user.getPasswordHash().equals(passwordHash);
    }

    public boolean checkPasswordByEmail(String email, String passwordHash) {
        User user = userRepository.findByEmail(email);
        return user.getPasswordHash().equals(passwordHash);
    }
}
