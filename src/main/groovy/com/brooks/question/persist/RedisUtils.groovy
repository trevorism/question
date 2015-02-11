package com.brooks.question.persist

import java.text.SimpleDateFormat

/**
 * @author tbrooks
 */
class RedisUtils {

    static String questionKey(def id) {
        "question/$id"
    }

    static String answerKey(def id) {
        "answer/$id"
    }

    static String idFromQuestionKey(String questionKey) {
        println questionKey
        questionKey["question/".length()..-1]
    }

    static Date getDateFromData(Map<String, String> data) {
        def date = null
        if (data.get("date"))
            date = new SimpleDateFormat(RedisQuestionApi.DATE_FORMAT).parse(data.get("date"))
        return date
    }
}
