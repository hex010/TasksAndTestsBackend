package myproject.SummerSpringBootProject.service;
import myproject.SummerSpringBootProject.dtos.FinishedQuizDTO;
import myproject.SummerSpringBootProject.dtos.QuizResultDTO;
import myproject.SummerSpringBootProject.entity.*;
import myproject.SummerSpringBootProject.enums.Gender;
import myproject.SummerSpringBootProject.enums.Role;
import myproject.SummerSpringBootProject.exception.AlreadySolvedQuizException;
import myproject.SummerSpringBootProject.exception.MismatchedQuestionAnswerCountException;
import myproject.SummerSpringBootProject.exception.NotFoundException;
import myproject.SummerSpringBootProject.repository.QuizRepository;
import myproject.SummerSpringBootProject.repository.QuizResultRepository;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizResultRepository quizResultRepository;

    @Mock
    private Authentication authentication;

    private AutoCloseable autoCloseable;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        quizService = new QuizService(quizRepository, userRepository, quizResultRepository);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    private User createUser() {
        User user = new User(1, "email@example.com", "password", "fistname", "lastname", Gender.MALE, Role.USER, true);
        return user;
    }

    private Quiz createQuiz() {
        Quiz quiz = new Quiz();

        Question question1 = new Question();
        Question question2 = new Question();
        Question question3 = new Question();

        Answer answer1 = new Answer(1, "text 1", question1, new Date());
        Answer answer2 = new Answer(2, "text 2", question2, new Date());
        Answer answer3 = new Answer(3, "text 3", question3, new Date());
        question1.setCorrectAnswer(answer1);
        question2.setCorrectAnswer(answer2);
        question3.setCorrectAnswer(answer3);
        quiz.setQuestions(Arrays.asList(question1, question2, question3));

        return quiz;
    }

    private FinishedQuizDTO createFinishedQuizDTO() {
        FinishedQuizDTO finishedQuizDTO = new FinishedQuizDTO();
        finishedQuizDTO.setQuiz_id(1);
        finishedQuizDTO.setAnswers(new int[]{1, 2, 3});
        return finishedQuizDTO;
    }

    private QuizResult createQuizResult() {
        QuizResult quizResult = new QuizResult();

        quizResult.setId(1);
        quizResult.setMark(8);
        quizResult.setResultDate(new Date());

        return quizResult;
    }

    @Test
    void shouldFinishQuizIfEverythingIsCorrect() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        User user = createUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Quiz quiz = createQuiz();
        when(quizRepository.findById(anyInt())).thenReturn(Optional.of(quiz));
        FinishedQuizDTO finishedQuizDTO = createFinishedQuizDTO();
        when(quizResultRepository.existsByUserAndQuiz(any(User.class), any(Quiz.class))).thenReturn(false);

        quizService.finishQuiz(finishedQuizDTO);

        verify(quizResultRepository).save(any(QuizResult.class));
    }

    @Test
    void shouldNotFinishQuizAndThrowExceptionWhenQuestionsAndAnswersLengthsAreNotEqual() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        User user = createUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Quiz quiz = createQuiz();
        when(quizRepository.findById(anyInt())).thenReturn(Optional.of(quiz));
        FinishedQuizDTO finishedQuizDTO = createFinishedQuizDTO();
        when(quizResultRepository.existsByUserAndQuiz(any(User.class), any(Quiz.class))).thenReturn(false);

        finishedQuizDTO.setAnswers(new int[]{1, 2}); //pateiktu atsakymai bus 2, klausimu is viso bus 3

        MismatchedQuestionAnswerCountException exception = assertThrows(
                MismatchedQuestionAnswerCountException.class,
                () -> quizService.finishQuiz(finishedQuizDTO)
        );
        assertNotNull(exception); //turetu buti iskviestas exception
        assertEquals("Klaida skaičiuojant testo rezultatą.", exception.getMessage()); //exception message koks turi buti
        verifyNoInteractions(quizResultRepository); //save() ir jokie kiti metodai neturetu buti iskviesti
    }

    @Test
    void shouldNotFinishQuizAndThrowExceptionIfQuizWasAlreadySolvedByCurrentUser() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        User user = createUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Quiz quiz = createQuiz();
        when(quizRepository.findById(anyInt())).thenReturn(Optional.of(quiz));
        FinishedQuizDTO finishedQuizDTO = createFinishedQuizDTO();

        when(quizResultRepository.existsByUserAndQuiz(any(User.class), any(Quiz.class))).thenReturn(true); //imituoju, kad duomenu baze grazino, jog dabartinis user jau sprendi si quiz

        AlreadySolvedQuizException exception = assertThrows(
                AlreadySolvedQuizException.class,
                () -> quizService.finishQuiz(finishedQuizDTO)
        );
        assertNotNull(exception); //turetu buti iskviestas exception
        assertEquals("Jau sprendėte šį testą.", exception.getMessage()); //exception message koks turi buti
        verify(quizResultRepository).existsByUserAndQuiz(user, quiz); //if'as turejo suveikti ir iskviesti existsByUserAndQuiz()
        verifyNoMoreInteractions(quizResultRepository); //save() ir jokie kiti metodai neturetu buti iskviesti, nes exception
    }

    @Test
    void shouldGetQuizResultIfEverythingIsCorrect() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        User user = createUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        QuizResult quizResult = createQuizResult();
        when(quizResultRepository.findByQuizIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(quizResult)); //tarkim surado sekmingai

        QuizResultDTO resultDTO = quizService.getQuizResult(123);

        verify(userRepository).findByEmail("user@example.com"); //metodas findByEmail() turejo buti iskviestas su atitinkamu parametru ir jo reiksme turi buti kaip authentication'o username reiksme
        verify(quizResultRepository).findByQuizIdAndUserId(123, user.getId()); //metodas findByQuizIdAndUserId() turejo buti iskviestas su atitinkamais parametrais

        assertNotNull(resultDTO); //testuojama funkcija sekmingai turejo grazinti rezultata
        assertEquals(quizResult.getMark(), resultDTO.getMark());
    }

    @Test
    void shouldNotGetQuizResultIfUserNotFound() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty()); //imituojam, kad duomenu baze nesurado tokio vartotojo

        assertThrows(NotFoundException.class, () -> quizService.getQuizResult(123)); //turi buti NotFoundException exception
        verify(quizResultRepository, never()).findByQuizIdAndUserId(anyInt(), anyInt()); //findByQuizIdAndUserId neturi buti iskviestas
    }

    @Test
    void shouldNotGetQuizResultIfQuizResultNotFound() {
        when(authentication.getName()).thenReturn("user@example.com"); //imituojam elgsena authentication'o kai bandom isgauti username
        User user = createUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user)); //imituojam, kad duomenu baze nesurado tokio vartotojo
        when(quizResultRepository.findByQuizIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> quizService.getQuizResult(123)
        ); //turi buti NotFoundException exception

        assertEquals("Nepavyko rasti testo rezultatą.", exception.getMessage());
    }

    @Test
    void shouldNotGetAllQuizzesIfSortOptionIsInvalid() {
        String[] invalidSortOptions = {
                "invalidOption",
                "date,invalidDirection",
                "likes,invalidDirection",
                "invalidField,asc",
                ",",
                ",asc",
        };

        for (String sortOption : invalidSortOptions) {
            assertThrows(IllegalArgumentException.class, () -> quizService.getAllQuizzes(sortOption, ""));
            verify(quizRepository, never()).findAll(any(Sort.class));
        }

    }
}