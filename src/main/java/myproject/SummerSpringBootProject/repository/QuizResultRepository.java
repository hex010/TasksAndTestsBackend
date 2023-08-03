package myproject.SummerSpringBootProject.repository;

import myproject.SummerSpringBootProject.entity.Quiz;
import myproject.SummerSpringBootProject.entity.QuizResult;
import myproject.SummerSpringBootProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
    boolean existsByUserAndQuiz(User user, Quiz quiz);
    Optional<QuizResult> findByQuizIdAndUserId(int quizId, int userId);
}
