package com.brooks.question.model

import groovy.transform.ToString

/**
 * @author tbrooks
 */
@ToString(includeFields = true)
class Question {

    public final static Question NULL_QUESTION = new Question()

    String id
    Date dateAsked
    String name
    String questionText
    Answer answer


    boolean isNull(){
        id == null
    }

}
