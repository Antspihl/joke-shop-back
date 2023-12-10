package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.LoginDTO;
import ee.veebiprojekt.veebiprojekttest.dto.RegisterDTO;
import ee.veebiprojekt.veebiprojekttest.dto.UserDTO;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.entity.UserRole;
import ee.veebiprojekt.veebiprojekttest.exception.FieldNotUniqueException;
import ee.veebiprojekt.veebiprojekttest.mapper.UserMapper;
import ee.veebiprojekt.veebiprojekttest.mock.dto.LoginDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.dto.RegisterDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.dto.UserDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.UserMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.UserRoleMock;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepository userRoleRepository;

    private static User testUser;
    private static UserDTO testUserDTO;
    private static LoginDTO testLoginDTO;
    private static RegisterDTO testRegisterDTO;
    private static RegisterDTO testIllegalRegisterDTO;
    private static UserRole testUserRole;

    @BeforeAll
    static void setUp() {
        testUser = UserMock.shallowUser(1L);
        testUserDTO = UserDTOMock.shallowUserDTO(1L);
        testLoginDTO = LoginDTOMock.shallowLoginDTO();
        testRegisterDTO = RegisterDTOMock.shallowRegisterDTO();
        testIllegalRegisterDTO = RegisterDTOMock.illegalRegisterDTO();
        testUserRole = UserRoleMock.getUserRole(1L);
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(testUser);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRoleRepository.save(any())).thenReturn(testUserRole);

        userService.register(testRegisterDTO);

        verify(userRepository, times(1)).save(userCaptor.capture());
    }

    @Test
    void testRegisterFailUserDTOHasNullValues() {
        assertThrows(IllegalArgumentException.class, () -> userService.register(testIllegalRegisterDTO));

        verify(userRepository, times(0)).save(userCaptor.capture());
    }

    @Test
    void testRegisterFailUsernameTaken() {
        when(userRepository.findByUsername(any())).thenReturn(testUser);

        assertThrows(FieldNotUniqueException.class, () -> userService.register(testRegisterDTO));

        verify(userRepository, times(0)).save(userCaptor.capture());
    }

    @Test
    void testRegisterFailEmailTaken() {
        when(userRepository.findByEmail(any())).thenReturn(testUser);

        assertThrows(FieldNotUniqueException.class, () -> userService.register(testRegisterDTO));

        verify(userRepository, times(0)).save(userCaptor.capture());
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername(any())).thenReturn(testUser);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        userService.login(testLoginDTO);

        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    void testLoginFailIncorrectUsername() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.login(testLoginDTO));

        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    void testLoginFailIncorrectPassword() {
        when(userRepository.findByUsername(any())).thenReturn(testUser);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login(testLoginDTO));

        verify(userRepository, times(1)).findByUsername(any());
    }


    @Test
    void testGetUserInfoSuccess() {
        when(userRepository.findByUsername(any())).thenReturn(testUser);
        when(userMapper.toDTO(any())).thenReturn(testUserDTO);

        userService.getUserInfo(testUser.getUsername(), testUser.getUsername());

        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    void testGetUserInfoFailUserNotAuthorized() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserInfo("AuthorizedUserName", "NOTAuthorizedUserName"));
    }

}
