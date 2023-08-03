package myproject.SummerSpringBootProject.controller;

import lombok.AllArgsConstructor;
import myproject.SummerSpringBootProject.dtos.FinishedQuizDTO;
import myproject.SummerSpringBootProject.dtos.MultipleQuizDTO;
import myproject.SummerSpringBootProject.dtos.QuizResultDTO;
import myproject.SummerSpringBootProject.dtos.SingleQuizDTO;
import myproject.SummerSpringBootProject.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RequestMapping("/api/v1/quiz")
@RestController
@AllArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/all")
    public List<MultipleQuizDTO> getBasicInformationOfQuizzes(@RequestParam(value = "sort", required = false, defaultValue = "") String sortOption, @RequestParam(value = "search", required = false, defaultValue = "") String searchOption) {
        return quizService.getAllQuizzes(sortOption, searchOption);
    }

    @GetMapping("/{id}")
    public SingleQuizDTO getQuiz(@PathVariable int id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("/{id}/result")
    public QuizResultDTO getQuizResult(@PathVariable int id) {
        return quizService.getQuizResult(id);
    }

    @PostMapping("/finish")
    public ResponseEntity<Void> finishQuiz(@RequestBody FinishedQuizDTO finishedQuizDTO) {
        quizService.finishQuiz(finishedQuizDTO);
        return ResponseEntity.ok().build();
    }
}
