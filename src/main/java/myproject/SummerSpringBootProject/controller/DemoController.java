package myproject.SummerSpringBootProject.controller;

import lombok.AllArgsConstructor;
import myproject.SummerSpringBootProject.dtos.MultipleQuizDTO;
import myproject.SummerSpringBootProject.entity.*;
import myproject.SummerSpringBootProject.repository.QuizRepository;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RequestMapping("/api/v1")
@RestController
@AllArgsConstructor
public class DemoController {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    @GetMapping ("/hiuser")
    public ResponseEntity<String> greetingAuth()
    {
        return ResponseEntity.ok("Hi user");
    }

    @GetMapping ("/hiadmin")
    public ResponseEntity<String> greetingAuth2()
    {
        return ResponseEntity.ok("Hi admin");
    }

    @GetMapping ("/hirandom")
    public ResponseEntity<String> greetingAuth3()
    {
        return ResponseEntity.ok("Hi random");
    }

    @PostMapping("/createquiz")
    public ResponseEntity<MultipleQuizDTO> createQuiz() {
        User author = userRepository.findByEmail("193robi.new@gmail.com").orElseThrow(() -> new UsernameNotFoundException("Vartotojo nera"));
        Quiz quiz = Quiz.builder().author(author).creationDate(new Date()).header("Testas apie java kalba (kitoks)").description("Sis testas patikrins ar jus mokate c# kalba, ar ismanote pagrindines strukturas, ar mokate objektinio programavimo karkasus.").build();

        Question question1 = Question.builder().text("Question 1 text").quiz(quiz).createdAt(new Date()).build();
        Question question2 = Question.builder().text("Question 2 text").quiz(quiz).createdAt(new Date()).build();

        Answer q1Answer1 = Answer.builder().text("Answer 1 for Question 1").question(question1).createdAt(new Date()).build();
        Answer q1Answer2 = Answer.builder().text("Answer 2 for Question 1").question(question1).createdAt(new Date()).build();
        Answer q1Answer3 = Answer.builder().text("Answer 3 for Question 1").question(question1).createdAt(new Date()).build();

        Answer q2Answer1 = Answer.builder().text("Answer 1 for Question 2").question(question2).createdAt(new Date()).build();
        Answer q2Answer2 = Answer.builder().text("Answer 2 for Question 2").question(question2).createdAt(new Date()).build();
        Answer q2Answer3 = Answer.builder().text("Answer 3 for Question 2").question(question2).createdAt(new Date()).build();

        question1.setAnswers(Arrays.asList(q1Answer1, q1Answer2, q1Answer3));
        question2.setAnswers(Arrays.asList(q2Answer1, q2Answer2, q2Answer3));

        question1.setCorrectAnswer(q1Answer2);
        question2.setCorrectAnswer(q2Answer3);

        quiz.setQuestions(Arrays.asList(question1, question2));

        QuizLikesDislikes quizLikesDislike1 = QuizLikesDislikes.builder().user(author).quiz(quiz).thisIsLike(true).reactionAt(new Date()).build();

        quiz.setQuizLikesDislikes(Collections.singletonList(quizLikesDislike1));

        MultipleQuizDTO quizDTO = new MultipleQuizDTO(quiz, false);
        quizRepository.save(quiz);
        return new ResponseEntity<>(quizDTO, HttpStatus.OK);
    }

    @GetMapping("/getFirstQuiz")
    public ResponseEntity<Quiz> getFirstQuiz() {
        Quiz quiz = quizRepository.findById(1).orElseThrow();
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @DeleteMapping("/deleteQuiz")
    public ResponseEntity<?> deleteQuiz() {
        Quiz quiz = quizRepository.findById(1).orElseThrow();
        quizRepository.delete(quiz);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
