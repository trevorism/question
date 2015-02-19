package com.brooks.question.persist

import com.brooks.question.model.Question

/**
 * @author tbrooks
 */
interface QuestionApi {

    Question getQuestion(String id)
    String createQuestion(Question question)
    void updateQuestion(String id, String questionText)

    void answerQuestion(String id, String answer)
    void deleteQuestion(String id)
    void deleteAnswer(String id)

    List<Question> searchQuestions(String query)
    List<Question> getUnansweredQuestions()
    List<Question> getLatestAnsweredQuestions()

}