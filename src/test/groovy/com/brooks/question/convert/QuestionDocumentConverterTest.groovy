package com.brooks.question.convert

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import org.apache.lucene.document.Document
import org.junit.Test

/**
 * @author tbrooks
 */
class QuestionDocumentConverterTest {

    @Test
    void testConversionWithoutAnswer() {
        Question original = new Question(id: "2", name: "name", questionText: "Who?", dateAsked: new Date())
        Question converted = performConversion(original)

        assertQuestionsAreSame(original, converted)
        assertAnswersAreNull(converted, original)

    }

    @Test
    void testConversionWithoutName() {
        Question original = new Question(id: "2", questionText: "Who?", dateAsked: new Date())
        Question converted = performConversion(original)

        assertQuestionsAreSame(original, converted)
        assertAnswersAreNull(converted, original)
    }


    @Test
    void testConversionWithAnswer() {
        Answer answer = new Answer(answerText: "Trevor", dateAnswered: new Date())
        Question original = new Question(id: "2", name: "name", questionText: "Who?", dateAsked: new Date(), answer: answer)

        Question converted = performConversion(original)
        assertQuestionsAreSame(original, converted)

        assertAnswersAreSame(converted, original)
    }


    private assertAnswersAreNull(Question converted, Question original) {
        assert converted.answer == null
        assert original.answer == converted.answer
    }

    private assertAnswersAreSame(Question converted, Question original) {
        assert converted.answer
        assert converted.answer.answerText == original.answer.answerText
        assertDatesAreSimilar(original.answer.dateAnswered, converted.answer.dateAnswered)
    }

    private assertQuestionsAreSame(Question original, Question converted) {
        assert original.id == converted.id
        assert original.name == converted.name
        assert original.questionText == converted.questionText
        assertDatesAreSimilar(original.dateAsked, converted.dateAsked)
    }

    private Date assertDatesAreSimilar(Date date1, Date date2){
        assert Math.abs(date1.time - date2.time) < 1000

    }

    private performConversion(Question original) {
        QuestionDocumentConverter questionDocumentConverter = new QuestionDocumentConverter()
        Document document = questionDocumentConverter.convert(original)
        Question converted = questionDocumentConverter.convert(document)
        return converted
    }

}
