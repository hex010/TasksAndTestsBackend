package myproject.SummerSpringBootProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @ManyToOne
    private User author;

    private String header;
    private String description;
    private int timerInSeconds;
    private Date creationDate;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //FetchType.LAZY nepakraus klausimus tuo paciu metu, kai bandom gauti si objekta.
    //Klausimai pasikraus tik tada, jeigu paciame kode bandysiu pasiekti si list'a
    //jeigu kartu su Quiz objektu norima iskart istraukti ir questions, tada reikia nurodyti FetchType.EAGER (by default jis ir yra nurodytas)
    private List<Question> questions;

    private int likes = 0;
    private int dislikes = 0;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizLikesDislikes> quizLikesDislikes;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizResult> quizResults;
}
