package com.brooks.question.api

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import com.brooks.question.persist.RedisStore
import com.brooks.question.persist.Store

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author tbrooks
 */
@Path("/question/")
class QuestionEndpoint {

    private static final Store redisStore = new RedisStore()

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Set<String> getAllQuestionIds(){
        redisStore.getAllQuestionIds()
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Question getQuestion(@PathParam("id") String id){
        redisStore.getQuestion(id)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String createQuestion(Question question){
        redisStore.createQuestion(question)
    }

    @GET
    @Path("search/{query}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response searchQuestions(@PathParam("query") String query){
        throw new WebApplicationException(404);
    }

    @GET
    @Path("unanswered")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Question> getUnansweredQuestions(){
        throw new WebApplicationException(404);
    }

    @POST
    @Path("{id}/answer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response answerQuestion(@PathParam("id") String id, Answer answer){
        redisStore.answerQuestion(id, answer)
    }

    @PUT
    @Path("{id}/answer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAnswer(@PathParam("id") String id, Answer answer){
        redisStore.answerQuestion(id, answer)
    }

    @DELETE
    @Path("{id}")
    Response deleteQuestion(@PathParam("id") String id){
        redisStore.deleteQuestion(id)
    }

    @DELETE
    @Path("answer/{id}")
    Response deleteAnswer(@PathParam("id") String id){
        redisStore.deleteAnswer(id)
    }


}
