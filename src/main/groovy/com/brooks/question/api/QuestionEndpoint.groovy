package com.brooks.question.api

import com.brooks.question.model.Answer
import com.brooks.question.model.Question

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author tbrooks
 */
@Path("/question/")
class QuestionEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getAllQuestionIds(){
        ["hello"]
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Question getQuestion(@PathParam("id") String id){
        new Question(name: "Trevor")
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String createQuestion(Question question){
        3
    }

    @GET
    @Path("search/{query}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String searchQuestions(@PathParam("query") String query){
        3
    }

    @GET
    @Path("unanswered")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Question> searchQuestions(){
        [new Question(name: "Trevor")]
    }

    @POST
    @Path("{id}/answer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response answerQuestion(@PathParam("id") String id, Answer answer){
        Response.ok();
    }

    @PUT
    @Path("{id}/answer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAnswer(@PathParam("id") String id, Answer answer){
        Response.ok();
    }

    @DELETE
    @Path("{id}")
    Response deleteQuestion(@PathParam("id") String id){
        Response.ok();
    }

    @DELETE
    @Path("answer/{id}")
    Response deleteAnswer(@PathParam("id") String id){
        Response.ok();
    }


}
