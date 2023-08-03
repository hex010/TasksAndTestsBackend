package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.entity.QuizResult;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    int mark;
    private Date resultDate;

    public QuizResultDTO(QuizResult quizResult) {
        this.mark = quizResult.getMark();
        this.resultDate = quizResult.getResultDate();
    }
}
