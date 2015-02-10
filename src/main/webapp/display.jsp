<%@include file="include/header.jsp" %>

<script>
    $(function() {
        renderQuestion.fromQueryString();
        renderQuestion.displayLatest();
    });
</script>


<span id="questionHeaderPlaceholder"></span>
<span id="queryStringPlaceholder"></span>

</hr>

<h3>Latest Questions</h3>

<span id="latestQuestionsPlaceholder"></span>


<%@include file="include/footer.jsp" %>