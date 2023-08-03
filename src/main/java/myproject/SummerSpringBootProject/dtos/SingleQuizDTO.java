package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.entity.Quiz;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleQuizDTO {
    private int id;
    private int timerInSeconds;
    private List<QuestionDTO> questions;

    public SingleQuizDTO(Quiz quiz) {
        this.id = quiz.getId();
        this.timerInSeconds = quiz.getTimerInSeconds();
        this.questions = quiz.getQuestions().stream().map(QuestionDTO::new).toList();
    }
}
