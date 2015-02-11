<%@include file="include/header.jsp" %>

<script>
    $(function() {
        renderQuestion.renderQuestionFromQueryParameterIfItExists();
    });
</script>
<span id="questionFromQueryStringPlaceholder"></span>


</hr>

<h3>Latest Questions </h3>

<span id="latestQuestionsPlaceholder"></span>


<%@include file="include/footer.jsp" %>