package myproject.SummerSpringBootProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizResult")
public class QuizResult {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Quiz quiz;

    private int mark;

    private Date resultDate;

    public QuizResult(User user, Quiz quiz, int mark) {
        this.user = user;
        this.quiz = quiz;
        this.mark = mark;
        resultDate = new Date();
    }
}
