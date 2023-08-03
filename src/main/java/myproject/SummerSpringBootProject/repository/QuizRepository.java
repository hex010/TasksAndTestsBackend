package myproject.SummerSpringBootProject.repository;

import io.micrometer.common.lang.Nullable;
import myproject.SummerSpringBootProject.entity.Quiz;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByHeaderContainingIgnoreCase(String searchValue, @Nullable Sort sort); //sort nera butinas
}
