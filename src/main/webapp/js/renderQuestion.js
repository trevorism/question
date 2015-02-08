var renderQuestion = (function (){

    function render(element, id){
        var success = function(data){
            var selector = $('#' + element);
            var dateValue = moment(data.date);
            var fromNow = dateValue.format("MM/DD/YYYY");

            selector.load("include/displayQuestion.html", function(){
                $("#nameContent").append(data.name);
                $("#dateContent").append(fromNow);
                $("#questionContent").append(data.question);
                $("#answerContent").append(data.answer);
            });

        };

        getContent(id, success);
    }

    function getContent(id, successFunction){
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: "service/question/" + id,
            success: successFunction
        });
    }

    return {
        render: render
    };

})();