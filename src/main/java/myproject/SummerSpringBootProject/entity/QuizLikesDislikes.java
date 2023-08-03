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
@Table(name = "quizLikesDislikes")
public class QuizLikesDislikes {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Quiz quiz;

    private boolean thisIsLike;

    private Date reactionAt;
}
