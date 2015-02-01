package com.brooks.question.model
/**
 * @author tbrooks
 */

class Question {

    long id
    String name
    String question
    Date questionAskedDate = new Date()
    Answer answer
}
