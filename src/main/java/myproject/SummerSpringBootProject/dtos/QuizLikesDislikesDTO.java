package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.entity.QuizLikesDislikes;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizLikesDislikesDTO {
    private Integer id;
    private Integer userId;
    private Integer quizId;
    private boolean thisIsLike;

    private Date reactionAt;

    public QuizLikesDislikesDTO(QuizLikesDislikes quizLikesDislikes) {
        this.id = quizLikesDislikes.getId();
        this.userId = quizLikesDislikes.getUser().getId();
        this.quizId = quizLikesDislikes.getQuiz().getId();
        this.thisIsLike = quizLikesDislikes.isThisIsLike();
        this.reactionAt = quizLikesDislikes.getReactionAt();
    }
}
