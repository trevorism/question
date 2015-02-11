package com.brooks.question.persist

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

import static com.brooks.question.persist.RedisUtils.*

/**
 * @author tbrooks
 */
class RedisQuestionApi implements QuestionApi {

    public static final String ALL_QUESTIONS_QUERY = "question/*"
    public static final String NEXT_KEY = "nextkey"
    public static final String DATE_FORMAT = "yyMMddHHmmss"
    public static final String UNANSWERED_KEY = "unanswered"
    public static final String LATEST_KEY = "latest"

    private static final JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(maxTotal: 1000, maxIdle: 1000, maxWaitMillis: 3000), "localhost", 6379, 0)
    private Jedis jedis

    RedisQuestionApi() {
        jedis = jedisPool.getResource()
    }

    @Override
    Set<String> getAllQuestionIds() {

        jedis.keys(ALL_QUESTIONS_QUERY)

    }

    @Override
    synchronized Question getQuestion(final String id) {
        return createQuestionFromId(id)
    }

    @Override
    String createQuestion(Question question) {
        Long id = jedis.incr(NEXT_KEY)
        String key = questionKey(id)
        if (!question.name?.trim())
            question.name = "Anonymous"

        jedis.hset(key, "date", new Date().format(DATE_FORMAT))
        jedis.hset(key, "question", question.question)
        jedis.hset(key, "name", question.name)
        jedis.sadd(UNANSWERED_KEY, key)
        return id.toString()
    }

    @Override
    List<Question> searchQuestions(String query) {
        return null
    }

    @Override
    List<Question> getUnansweredQuestions() {
        String key = UNANSWERED_KEY
        Set<String> members = jedis.smembers(key)
        List<Question> questions = members.collect{
            String id = RedisUtils.idFromQuestionKey(it)
            return createQuestionFromId(id)
        }
        return questions.sort{ a,b ->
            b.date <=> a.date
        }
    }

    @Override
    List<Question> getLatestAnsweredQuestions() {
        List<String> latestQuestionKeys = jedis.lrange(LATEST_KEY, 0, 5);
        latestQuestionKeys.collect{
            String id = RedisUtils.idFromQuestionKey(it)
            return createQuestionFromId(id)
        }
    }

    @Override
    void answerQuestion(String id, Answer answer) {
        String key = answerKey(id)
        jedis.hset(key, "date", new Date().format(DATE_FORMAT))
        jedis.hset(key, "answer", answer.answer)
        jedis.srem(UNANSWERED_KEY, questionKey(id))

        jedis.lpush(LATEST_KEY, questionKey(id));
        jedis.ltrim(LATEST_KEY, 0, 99);
    }

    @Override
    boolean deleteQuestion(String id) {
        String key = questionKey(id)
        jedis.srem(UNANSWERED_KEY, key)
        return jedis.del(key) > 0
    }

    @Override
    boolean deleteAnswer(String id) {
        String key = answerKey(id)
        jedis.sadd(UNANSWERED_KEY, questionKey(id))
        return jedis.del(key) > 0
    }

    private String getAnswerFromStore(String id) {
        String answerKey = "answer/$id"
        Map<String, String> answerData = jedis.hgetAll(answerKey);
        return answerData.get("answer")
    }

    private Question createQuestionFromId(String id) {
        String key = questionKey(id)
        Map<String, String> data = jedis.hgetAll(key)
        def longId = Long.parseLong(id)
        def name = data.get("name")
        def questionText = data.get("question")
        Date date = getDateFromData(data)
        return new Question(id: longId, name: name, question: questionText, date: date, answer: getAnswerFromStore(id))
    }


}
