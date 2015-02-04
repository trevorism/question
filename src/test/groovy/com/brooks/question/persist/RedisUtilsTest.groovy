package com.brooks.question.persist

import org.junit.Test;

class RedisUtilsTest {

    @Test
    void testQuestionKey() {
        assert "question/5" == RedisUtils.questionKey(5)
        assert "question/5" == RedisUtils.questionKey("5")
        assert "question/50" == RedisUtils.questionKey(50)

    }

    @Test
    void testAnswerKey() {
        assert "answer/5" == RedisUtils.answerKey(5)
        assert "answer/5" == RedisUtils.answerKey("5")
        assert "answer/50" == RedisUtils.answerKey("50")
    }

    @Test
    void testIdFromQuestionKey() {
        assert "5" == RedisUtils.idFromQuestionKey("question/5")
        assert "50" == RedisUtils.idFromQuestionKey("question/50")
    }
}