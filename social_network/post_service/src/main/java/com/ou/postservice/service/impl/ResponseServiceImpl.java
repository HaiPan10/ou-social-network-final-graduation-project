// package com.ou.postservice.service.impl;


// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.util.Date;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Propagation;
// import org.springframework.transaction.annotation.Transactional;

// import com.ou.postservice.pojo.Answer;
// import com.ou.postservice.pojo.AnswerOption;
// import com.ou.postservice.pojo.Post;
// import com.ou.postservice.pojo.Question;
// import com.ou.postservice.pojo.QuestionOption;
// import com.ou.postservice.pojo.Response;
// import com.ou.postservice.pojo.User;
// import com.ou.postservice.repository.repositoryJPA.AnswerOptionRepositoryJPA;
// import com.ou.postservice.repository.repositoryJPA.AnswerRepositoryJPA;
// import com.ou.postservice.repository.repositoryJPA.QuestionOptionRepositoryJPA;
// import com.ou.postservice.repository.repositoryJPA.QuestionRepositoryJPA;
// import com.ou.postservice.repository.repositoryJPA.ResponseRepositoryJPA;
// import com.ou.postservice.service.interfaces.PostService;
// import com.ou.postservice.service.interfaces.ResponseService;
// import com.ou.postservice.service.interfaces.UserService;

// @Service
// @Transactional(rollbackFor = Exception.class)
// public class ResponseServiceImpl implements ResponseService {
//     // @Autowired
//     // private ResponseRepository responseRepository;
//     @Autowired
//     private ResponseRepositoryJPA responseRepositoryJPA;
//     @Autowired
//     private QuestionRepositoryJPA questionRepositoryJPA;
//     @Autowired
//     private AnswerRepositoryJPA answerRepositoryJPA;
//     @Autowired
//     private QuestionOptionRepositoryJPA questionOptionRepositoryJPA;
//     @Autowired
//     private AnswerOptionRepositoryJPA answerOptionRepositoryJPA;
//     @Autowired
//     private PostService postService;
//     @Autowired
//     private UserService userService;

//     @Override
//     @Transactional(propagation = Propagation.REQUIRED)
//     public Response create(Response response) throws Exception {
//         try {
//             Post persistPost = postService.retrieve(response.getSurveyId().getId());
//             User persistUser = userService.retrieve(response.getUserId());
//             Response transientResponse = new Response();
//             transientResponse.setUserId(persistUser.getId());
//             transientResponse.setSurveyId(persistPost.getPostSurvey());
//             transientResponse.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
//             // session.save(transientResponse);
//             responseRepositoryJPA.save(transientResponse);

//             response.getAnswers().forEach(answer -> {
//                 // Question persistQuestion = session.get(Question.class, answer.getQuestionId().getId());
//                 Question persistQuestion = questionRepositoryJPA.findById(answer.getQuestionId().getId()).get();
//                 if (persistQuestion == null) {
//                     try {
//                         throw new Exception("In valid question!");
//                     } catch (Exception e) {
//                         e.printStackTrace();
//                     }
//                 }
//                 Answer transientAnswer = new Answer();
//                 transientAnswer.setQuestionId(persistQuestion);
//                 transientAnswer.setValue(answer.getValue());
//                 transientAnswer.setResponseId(transientResponse);
//                 // session.save(transientAnswer);
//                 answerRepositoryJPA.save(transientAnswer);

//                 if (answer.getAnswerOptions() != null) {
//                     answer.getAnswerOptions().forEach(answerOption -> {
//                         AnswerOption transientAnswerOption = new AnswerOption();
//                         // QuestionOption persistQuestionOption = session.get(QuestionOption.class,
//                         //         answerOption.getQuestionOptionId().getId());
//                         QuestionOption persistQuestionOption = 
//                             questionOptionRepositoryJPA.findById(answerOption.getQuestionOptionId().getId()).get();
//                         if (persistQuestionOption == null) {
//                             try {
//                                 throw new Exception("In valid question option!");
//                             } catch (Exception e) {
//                                 e.printStackTrace();
//                             }
//                         }
//                         transientAnswerOption.setQuestionOptionId(persistQuestionOption);
//                         transientAnswerOption.setAnswerId(transientAnswer);
//                         // session.save(transientAnswerOption);
//                         answerOptionRepositoryJPA.save(transientAnswerOption);
//                     });
//                 }
//             });
//             return transientResponse;
//         } catch (Exception ex) {
//             throw new Exception(ex.getMessage());
//         }
//     }

//     @Override
//     public List<Answer> getTextAnswers(Long questionId) {
//         return responseRepositoryJPA.getTextAnswers(questionId);
//     }
    
// }
