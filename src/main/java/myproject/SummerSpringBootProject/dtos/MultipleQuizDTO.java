package myproject.SummerSpringBootProject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myproject.SummerSpringBootProject.entity.Quiz;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleQuizDTO {
    private Integer id;
    private String authorUsername;
    private Date creationDate;

    private String header;
    private String description;

    private Integer likes;
    private Integer dislikes;

    private boolean solved;

    //reacted users turi DTO, nes kitaip java imtu visa entity, o man tik id reikia userio ir quizo, tai teko dto kurti
    private List<QuizLikesDislikesDTO> reactedUsers;

    public MultipleQuizDTO(Quiz quiz, boolean solved) {
        this.id = quiz.getId();
        this.solved = solved;
        this.authorUsername = quiz.getAuthor().getFirstname() + " " + quiz.getAuthor().getLastname();
        this.creationDate = quiz.getCreationDate();
        this.header = quiz.getHeader();
        this.description = quiz.getDescription();
        this.likes = quiz.getLikes();
        this.dislikes = quiz.getDislikes();
        this.reactedUsers = quiz.getQuizLikesDislikes().stream().map(QuizLikesDislikesDTO::new).toList();
    }
}