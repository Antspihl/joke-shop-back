package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.JokeDTO;
import ee.veebiprojekt.veebiprojekttest.entity.BoughtJoke;
import ee.veebiprojekt.veebiprojekttest.entity.Joke;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.mapper.JokeMapper;
import ee.veebiprojekt.veebiprojekttest.mock.dto.JokeDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.JokeMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.RatingMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.UserMock;
import ee.veebiprojekt.veebiprojekttest.repository.BoughtJokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.JokeRepository;
import ee.veebiprojekt.veebiprojekttest.repository.RatingRepository;
import ee.veebiprojekt.veebiprojekttest.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeServiceTests {

    @Captor
    private ArgumentCaptor<Joke> jokeCaptor;

    @InjectMocks
    private JokeService jokeService;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoughtJokeRepository boughtJokeRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private JokeMapper jokeMapper;

    private static JokeDTO testJokeDto;
    private static Joke testJoke;
    private static JokeDTO testEditedJokeDto;
    private static Joke testEditedJoke;
    private static User testUser;
    private static Rating testRating;
    private static BoughtJoke testBoughtJoke;
    private static List<Joke> jokeList;
    private static List<JokeDTO> jokeDTOList;
    private static List<Long> jokeIdList;
    private final String username = "user";

    @BeforeAll
    static void setUp() {
        testJokeDto = JokeDTOMock.shallowJokeDTO(1L);
        testJoke = JokeMock.shallowJoke(1L);
        testEditedJokeDto = JokeDTOMock.shallowEditedJokeDTO(1L);
        testEditedJoke = JokeMock.shallowEditedJoke(1L);
        testUser = UserMock.shallowUser(1L);
        testRating = RatingMock.shallowRating(1L);
        testBoughtJoke = new BoughtJoke(1L, 1L);
        jokeList = List.of(testJoke);
        jokeDTOList = List.of(testJokeDto);
        jokeIdList = List.of(1L);
    }

    @Test
    void testAddJokeSuccess() {
        when(jokeRepository.save(any(Joke.class))).thenReturn(testJoke);
        when(jokeMapper.toDTO(any(Joke.class))).thenReturn(testJokeDto);
        when(userRepository.findByUsername(username)).thenReturn(testUser);

        JokeDTO savedDto = jokeService.addJoke(testJokeDto, username);

        verify(jokeRepository, times(1)).save(jokeCaptor.capture());

        assertThat(testJokeDto.id()).isEqualTo(savedDto.id());
    }

    @Test
    void testGetByIdSuccess() {
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.of(testJoke));
        when(jokeMapper.toDTO(any(Joke.class))).thenReturn(testJokeDto);

        JokeDTO savedDto = jokeService.getJoke(1L);

        verify(jokeRepository, times(1)).findById(1L);

        assertThat(testJokeDto.id()).isEqualTo(savedDto.id());
    }

    @Test
    void testUpdateJokeSuccess() {
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.of(testJoke));
        when(jokeRepository.save(any(Joke.class))).thenReturn(testEditedJoke);
        when(jokeMapper.toDTO(any(Joke.class))).thenReturn(testJokeDto);

        JokeDTO savedDto = jokeService.editJoke(1, testEditedJokeDto);

        verify(jokeRepository, times(1)).save(jokeCaptor.capture());

        assertThat(testJokeDto.id()).isEqualTo(savedDto.id());
    }

    @Test
    void testRecalculateJokeRatingSuccess() {
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.of(testJoke));
        when(jokeRepository.save(any(Joke.class))).thenReturn(testJoke);
        when(ratingRepository.findAllByJokeId(1L)).thenReturn(Collections.singletonList(testRating));

        jokeService.recalculateJokeRating(1L);

        verify(jokeRepository, times(1)).save(jokeCaptor.capture());
    }

    @Test
    void testDeleteJokeSuccess() {
        jokeService.deleteJoke(1L);

        verify(jokeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetJokesSuccess() {
        when(jokeRepository.findAll()).thenReturn(jokeList);
        when(jokeMapper.toDTOList(anyList())).thenReturn(jokeDTOList);

        jokeService.getJokes();

        verify(jokeRepository, times(1)).findAll();
    }

    @Test
    void testGetTop3JokesSuccess() {
        when(jokeRepository.findAllByOrderByTimesBoughtDesc()).thenReturn(jokeList);
        when(jokeMapper.toDTOList(anyList())).thenReturn(jokeDTOList);

        jokeService.getTop3();

        verify(jokeRepository, times(1)).findAllByOrderByTimesBoughtDesc();
    }

    @Test
    void testGetSetupsSuccess() {
        when(jokeRepository.findAll()).thenReturn(jokeList);
        when(jokeMapper.toDTOList(anyList())).thenReturn(jokeDTOList);

        jokeService.getSetups();

        verify(jokeRepository, times(1)).findAll();
    }

    @Test
    void testGetBoughtJokesSuccess() {
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(boughtJokeRepository.findJokeIdsByUserId(testUser.getUserId())).thenReturn(jokeIdList);

        jokeService.getBoughtJokes(username);

        verify(jokeRepository, times(1)).findAllById(jokeIdList);
    }

    @Test
    void testBuyJokeSuccess() {
        when(userRepository.findByUsername(username)).thenReturn(testUser);
        when(jokeRepository.findById(1L)).thenReturn(java.util.Optional.of(testJoke));
        when(boughtJokeRepository.save(any(BoughtJoke.class))).thenReturn(testBoughtJoke);

        jokeService.buyJoke(1L, username);

        verify(boughtJokeRepository, times(1)).save(any(BoughtJoke.class));
    }
}
