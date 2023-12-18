package ee.veebiprojekt.veebiprojekttest.service;

import ee.veebiprojekt.veebiprojekttest.dto.RatingDTO;
import ee.veebiprojekt.veebiprojekttest.entity.Rating;
import ee.veebiprojekt.veebiprojekttest.entity.User;
import ee.veebiprojekt.veebiprojekttest.exception.EntityNotFoundException;
import ee.veebiprojekt.veebiprojekttest.exception.WrongOwnerException;
import ee.veebiprojekt.veebiprojekttest.mapper.RatingMapper;
import ee.veebiprojekt.veebiprojekttest.mock.dto.RatingDTOMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.RatingMock;
import ee.veebiprojekt.veebiprojekttest.mock.entity.UserMock;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTests {

    @Captor
    private ArgumentCaptor<Rating> ratingCaptor;

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private JokeService jokeService;

    private static Rating testRating;
    private static Rating testEditedRating;
    private static RatingDTO testRatingDto;
    private static RatingDTO testEditedRatingDto;
    private static User testUser;
    private static List<Rating> testRatingList;

    @BeforeAll
    static void setUp() {
        testRating = RatingMock.shallowRating(1L);
        testEditedRating = RatingMock.shallowRating(1L);
        testEditedRating.setRatingValue(5);
        testRatingDto = RatingDTOMock.shallowRatingDTO(1L);
        testEditedRatingDto = RatingDTOMock.shallowEditedRatingDTO(1L);
        testUser = UserMock.shallowUser(1L);
        testRatingList = List.of(testRating);
    }

    @Test
    void testAddRatingSuccess() {
        when(userRepository.findByUsername(any())).thenReturn(testUser);

        ratingService.addRating(testRatingDto, "test");

        verify(ratingRepository, times(1)).save(ratingCaptor.capture());
        verify(jokeService, times(1)).recalculateJokeRating(1L);
    }

    @Test
    void testGetJokeRatingSuccess() {
        when(ratingRepository.findAllByJokeId(1L)).thenReturn(testRatingList);

        ratingService.getJokeRating(1L);

        verify(ratingRepository, times(1)).findAllByJokeId(1L);
    }

    @Test
    void testGetRatingsSuccess() {
        when(ratingRepository.findAll()).thenReturn(testRatingList);

        ratingService.getRatings();

        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testEditRatingSuccess() {
        when(ratingRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testEditedRating));
        when(userRepository.findByUsername(any())).thenReturn(testUser);
        when(ratingRepository.save(testEditedRating)).thenReturn(testEditedRating);


        ratingService.editRating(testEditedRatingDto, 1L, "test");

        verify(ratingRepository, times(1)).save(ratingCaptor.capture());
    }

    @Test
    void testDeleteRatingSuccess() {
        when(ratingRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testRating));
        when(userRepository.findByUsername(any())).thenReturn(testUser);

        ratingService.deleteRating(1L, "test");

        verify(ratingMapper, times(1)).toDTO(testRating);
        verify(ratingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRatingFail() {
        when(ratingRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->ratingService.deleteRating(1L, "test"));

        verify(ratingRepository, times(0)).deleteById(1L);
    }

    @Test
    void testDeleteRatingWrongOwner() {
        when(ratingRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testRating));
        when(userRepository.findByUsername(any())).thenReturn(UserMock.shallowUser(2L));

        assertThrows(WrongOwnerException.class, () ->ratingService.deleteRating(1L, "test"));

        verify(ratingRepository, times(0)).deleteById(1L);
    }
}
