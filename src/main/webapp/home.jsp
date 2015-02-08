<%@include file="include/header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h2>Ask a Question</h2>
        <p>I know the answer to your question. It's just a matter of time before you get the answer.</p>
    </div>


    <div class="form-group">
        <label for="nameId">Name</label>
        <input type="text" class="form-control" id="nameId" placeholder="Optional Name" />
    </div>
    <div class="form-group">
        <label for="questionId">Question</label>
        <textarea id="questionId" class="form-control" rows="5"></textarea>
    </div>
    <button id="submitButton" class="btn btn-primary">Submit</button>

</div>

<%@include file="include/footer.jsp" %>
