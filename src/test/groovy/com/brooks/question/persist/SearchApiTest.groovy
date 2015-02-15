package com.brooks.question.persist

import com.brooks.question.api.FileBasedLucene
import com.brooks.question.api.MemoryBasedLucene
import com.brooks.question.convert.QuestionDocumentConverter
import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import org.junit.Test

/**
 * @author tbrooks
 */
class SearchApiTest {

    @Test
    void testGetInvalidId() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())

        Question converted = searchApi.getQuestion("boo")

        assert converted
        assert converted.isNull()

    }

    @Test
    void testGetInvalidNumberId() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())

        Question converted = searchApi.getQuestion("-2")

        assert converted
        assert converted.isNull()
    }

    @Test
    void testCreateQuestion() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())
        String questionText = "Is bob real?"
        Question question = new Question(questionText: questionText)


        def id = searchApi.createQuestion(question)
        Question converted = searchApi.getQuestion(id)

        assert converted.questionText == questionText
        assert !converted.name
        assert converted.dateAsked
    }

    @Test
    void testInsertAnswerQuestion() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())
        String questionText = "Is bob real?"
        String answerText = "The answer is no."
        Question question = new Question(questionText: questionText)

        def id = searchApi.createQuestion(question)
        searchApi.answerQuestion(id, answerText)

        Question converted = searchApi.getQuestion(id)

        assert !converted.name
        assert converted.questionText == questionText
        assert converted.answer.answerText == answerText


    }

    @Test
    void testDeleteQuestion() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())
        String questionText = "Is bob real?"
        Question question = new Question(questionText: questionText)

        def id = searchApi.createQuestion(question)
        Question converted = searchApi.getQuestion(id)

        assert converted
        assert converted.questionText == questionText

        searchApi.deleteQuestion(id)
        Question deleted = searchApi.getQuestion(id)

        assert deleted.isNull()
    }

    @Test
    void testDeleteAnswer() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())
        String questionText = "Is bob real?"
        String answerText = "The answer is no."
        Question question = new Question(questionText: questionText)

        def id = searchApi.createQuestion(question)
        searchApi.answerQuestion(id, answerText)
        searchApi.deleteAnswer(id)
        Question converted = searchApi.getQuestion(id)

        assert !converted.name
        assert converted.questionText == questionText
        assert !converted.answer
    }


    @Test
    void testGetLatestAnsweredQuestions() {
        SearchApi searchApi = new SearchApi(new MemoryBasedLucene())

        //Insert
        searchApi.createQuestion(new Question(questionText: "1q", answer: new Answer(answerText: "3answer", dateAnswered: new Date())))
        searchApi.createQuestion(new Question(questionText: "2q", answer: new Answer(answerText: "1answer", dateAnswered: new Date())))
        searchApi.createQuestion(new Question(questionText: "3q", answer: new Answer(answerText: "2answer", dateAnswered: new Date())))
        searchApi.createQuestion(new Question(questionText: "4q"))
        searchApi.createQuestion(new Question(questionText: "5q"))

        //
        searchApi.searchQuestions("2").each {
            println it
        }

    }

}