package com.brooks.question.convert

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import org.apache.lucene.document.DateTools
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField

/**
 * @author tbrooks
 */
class QuestionDocumentConverter {

    public static final String UNANSWERED = "__unanswered"

    static Document convert(Question question){
        Document document = new Document()

        document.add(new TextField("id", question.id, Field.Store.YES))
        if(question.name)
            document.add(new TextField("name", question.name, Field.Store.YES))

        document.add(new TextField("questionText", question.questionText, Field.Store.YES))
        document.add(new TextField("dateAsked", DateTools.dateToString(question.dateAsked, DateTools.Resolution.SECOND), Field.Store.YES))

        if(question.answer){
            document.add(new TextField("answerText", question.answer.answerText, Field.Store.YES))
            document.add(new TextField("dateAnswered", DateTools.dateToString(question.answer.dateAnswered, DateTools.Resolution.SECOND), Field.Store.YES))
        }
        else{
            document.add(new TextField("answerText", UNANSWERED, Field.Store.YES))
        }

        return document
    }

    static Question convert(Document document){

        String id = document.get("id")
        String name = document.get("name")
        String questionText = document.get("questionText")
        Date dateAsked = DateTools.stringToDate(document.get("dateAsked"))
        Answer answer = buildAnswer(document)

        return new Question(id: id, name: name, questionText: questionText, dateAsked: dateAsked, answer: answer)
    }

   private static buildAnswer(Document document) {
        def answerText = document.get("answerText")
        if (answerText != UNANSWERED) {
            Date dateAnswered = DateTools.stringToDate(document.get("dateAnswered"))
            return new Answer(answerText: answerText, dateAnswered: dateAnswered)
        }
        return null
    }
}
