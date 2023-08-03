package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.entity.Answer;
import myproject.SummerSpringBootProject.entity.Question;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Integer id;
    private String text;
    private List<Answer> answers;

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.text = question.getText();
        this.answers = question.getAnswers();
    }
}
