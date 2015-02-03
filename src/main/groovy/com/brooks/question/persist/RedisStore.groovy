package com.brooks.question.persist

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import redis.clients.jedis.Jedis

import java.text.SimpleDateFormat

/**
 * @author tbrooks
 */
class RedisStore implements Store {

    public static final String ALL_QUESTIONS_QUERY = "question/*"
    public static final String NEXT_KEY = "nextkey"
    public static final String DATE_FORMAT = "yyMMddhhmmss"
    private final Jedis jedis;

    RedisStore() {
        jedis = new Jedis("localhost")
    }

    private def redisTransaction(Closure closure) {
        jedis.connect()
        def value = closure.call()
        jedis.close()
        return value
    }

    @Override
    Set<String> getAllQuestionIds() {
        redisTransaction {
            jedis.keys(ALL_QUESTIONS_QUERY)
        }
    }

    @Override
    Question getQuestion(final String id) {
        redisTransaction {
            Question question = createQuestionFromStore(id)
            question.answer = getAnswerFromStore(id)
            return question
        }
    }

    @Override
    String createQuestion(Question question) {
        redisTransaction {
            Long id = jedis.incr(NEXT_KEY)
            String key = "question/$id"
            if (!question.name?.trim())
                question.name = "Anonymous"

            jedis.hset(key, "date", new Date().format(DATE_FORMAT))
            jedis.hset(key, "question", question.question)
            jedis.hset(key, "name", question.name)
            return key
        }

    }

    @Override
    List<Question> searchQuestions(String query) {
        return null
    }

    @Override
    List<Question> getUnansweredQuestions() {
        return null
    }

    @Override
    void answerQuestion(String id, Answer answer) {
        String key = "answer/$id"
        redisTransaction {
            jedis.hset(key, "date", new Date().format(DATE_FORMAT))
            jedis.hset(key, "answer", answer.answer)
        }
    }

    @Override
    boolean deleteQuestion(String id) {
        redisTransaction {
            String key = "question/$id"
            return jedis.del(key) > 0
        }
    }

    @Override
    boolean deleteAnswer(String id) {
        redisTransaction {
            String key = "answer/$id"
            return jedis.del(key) > 0
        }

    }

    private String getAnswerFromStore(String id) {
        String answerKey = "answer/$id"
        Map<String, String> answerData = jedis.hgetAll(answerKey);
        return answerData.get("answer")
    }

    private Question createQuestionFromStore(String id) {
        String key = "question/$id"
        Map<String, String> data = jedis.hgetAll(key)
        def longId = Long.parseLong(id)
        def name = data.get("name")
        def questionText = data.get("question")
        Date date = getDateFromData(data)
        return new Question(id: longId, name: name, question: questionText, date: date)
    }

    private Date getDateFromData(Map<String, String> data) {
        def date = null
        if (data.get("date"))
            date = new SimpleDateFormat(DATE_FORMAT).parse(data.get("date"))
        return date
    }
}
