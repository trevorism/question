package com.brooks.question.persist

import com.brooks.question.model.Answer
import com.brooks.question.model.Question

/**
 * @author tbrooks
 */
interface Store {

    Set<String> getAllQuestionIds()
    Question getQuestion(String id)
    String createQuestion(Question question)
    List<Question> searchQuestions(String query)
    Set<String> getUnansweredQuestions()
    void answerQuestion(String id, Answer answer)

    boolean deleteQuestion(String id)
    boolean deleteAnswer(String id)
}