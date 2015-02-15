package com.brooks.question.api

import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import com.brooks.question.persist.SearchApi
import com.brooks.question.persist.QuestionApi

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

    private static final QuestionApi questionApi = new SearchApi(new FileBasedLucene("lucene.db"))

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Set<String> getAllQuestionIds(){
        questionApi.getAllQuestionIds()
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Question getQuestion(@PathParam("id") String id){
        questionApi.getQuestion(id)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String createQuestion(Question question){
        questionApi.createQuestion(question)
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
        questionApi.getUnansweredQuestions()
    }

    @GET
    @Path("latestAnswered")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Question> getLatestAnsweredQuestions(){
        questionApi.getLatestAnsweredQuestions()
    }

    @POST
    @Path("{id}/answer")
    @Consumes(MediaType.APPLICATION_JSON)
    Response answerQuestion(@PathParam("id") String id, String answerText){
        questionApi.answerQuestion(id, answerText)
        return Response.ok().build()
    }

    @DELETE
    @Path("{id}")
    void deleteQuestion(@PathParam("id") String id){
        questionApi.deleteQuestion(id)
    }

    @DELETE
    @Path("answer/{id}")
    void deleteAnswer(@PathParam("id") String id){
        questionApi.deleteAnswer(id)
    }


}
