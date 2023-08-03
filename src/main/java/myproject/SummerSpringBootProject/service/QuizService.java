package myproject.SummerSpringBootProject.service;

import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.dtos.FinishedQuizDTO;
import myproject.SummerSpringBootProject.dtos.MultipleQuizDTO;
import myproject.SummerSpringBootProject.dtos.QuizResultDTO;
import myproject.SummerSpringBootProject.dtos.SingleQuizDTO;
import myproject.SummerSpringBootProject.entity.*;
import myproject.SummerSpringBootProject.exception.AlreadySolvedQuizException;
import myproject.SummerSpringBootProject.exception.MismatchedQuestionAnswerCountException;
import myproject.SummerSpringBootProject.exception.NotFoundException;
import myproject.SummerSpringBootProject.repository.QuizRepository;
import myproject.SummerSpringBootProject.repository.QuizResultRepository;
import myproject.SummerSpringBootProject.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    private final QuizResultRepository quizResultRepository;

    public List<MultipleQuizDTO> getAllQuizzes(String sortOption, String searchOption) {
        Sort sort = null;
        String sortField = null;
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortOption != null && !sortOption.isEmpty()) {
            String[] _sort = sortOption.split(",");
            if(_sort.length < 1 || _sort.length > 2) throw new IllegalArgumentException("Invalid sort option: " + sortOption);

            if(_sort.length == 1){
                sortField = sortOption;
            } else {
                sortField = _sort[0];
                switch (_sort[1]){
                    case "desc": {
                        sortDirection = Sort.Direction.DESC;
                        break;
                    }
                    case "asc": {
                        sortDirection = Sort.Direction.ASC;
                        break;
                    }
                    default:{
                        throw new IllegalArgumentException("Invalid sort option: " + sortOption);
                    }
                }
            }

            switch (sortField) {
                case "date":
                    sort = Sort.by(sortDirection, "creationDate");
                    break;
                case "likes":
                    sort = Sort.by(sortDirection, "likes");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sort option: " + sortOption);
            }
        }
        List<Quiz> quizList;

        if(searchOption.trim().length() >= 3)
            quizList = (sort != null) ? quizRepository.findByHeaderContainingIgnoreCase(searchOption, sort) : quizRepository.findByHeaderContainingIgnoreCase(searchOption, null);
        else
            quizList = (sort != null) ? quizRepository.findAll(sort) : quizRepository.findAll();


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userEmail.isEmpty() ? null : userRepository.findByEmail(userEmail).orElse(null);

//        List<MultipleQuizDTO> quizDTOList = new ArrayList<>();
//        boolean quizSolved = false;
//
//        for(Quiz currentQuiz : quizList) {
//            if(user != null && quizResultRepository.existsByUserAndQuiz(user, currentQuiz))
//                quizSolved = true;
//
//            quizDTOList.add(new MultipleQuizDTO(currentQuiz, quizSolved));
//            quizSolved = false;
//        }

        List<MultipleQuizDTO> quizDTOList = quizList.stream()
                .map(currentQuiz -> {
                    boolean quizSolved = false;
                    if (user != null && quizResultRepository.existsByUserAndQuiz(user, currentQuiz)) {
                        quizSolved = true;
                    }
                    return new MultipleQuizDTO(currentQuiz, quizSolved);
                }).toList();

        return quizDTOList;
    }

    public SingleQuizDTO getQuiz(int id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundException("Testas nerastas"));
        return new SingleQuizDTO(quiz);
    }

    public void finishQuiz(FinishedQuizDTO finishedQuizDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("Vartotojas nerastas."));
        Quiz quiz = quizRepository.findById(finishedQuizDTO.getQuiz_id()).orElseThrow(() -> new NotFoundException("Testas nerastas"));

        if(quiz.getQuestions().size() != finishedQuizDTO.getAnswers().length) throw new MismatchedQuestionAnswerCountException("Klaida skaičiuojant testo rezultatą.");
        if(quizResultRepository.existsByUserAndQuiz(user, quiz)) throw new AlreadySolvedQuizException("Jau sprendėte šį testą.");


        int totalCorrect = 0;
        for(int i = 0; i < finishedQuizDTO.getAnswers().length; i++) {
            if(finishedQuizDTO.getAnswers()[i] == quiz.getQuestions().get(i).getCorrectAnswer().getId()) {
                totalCorrect++;
            }
        }

        float mark = (float) quiz.getQuestions().size() / totalCorrect * 10;
        QuizResult quizResult = new QuizResult(user, quiz, Math.round(mark));

        quizResultRepository.save(quizResult);
    }

    public QuizResultDTO getQuizResult(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("Vartotojas nerastas."));

        QuizResult quizResult =  quizResultRepository.findByQuizIdAndUserId(id, user.getId()).orElseThrow(() -> new NotFoundException("Nepavyko rasti testo rezultatą."));

        return new QuizResultDTO(quizResult);
    }
}
