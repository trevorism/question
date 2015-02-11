var renderQuestion = (function (){

    function renderLatestQuestions(){
        var selector = $("#latestQuestionsPlaceholder");

        getLatestQuestions(function(data){
            for(var i=0, len=data.length; i < len; i++){
                selector.append(renderQuestion(data[i]));
            }
        });
    }

    function getLatestQuestions(successFunction){
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: "service/question/latestAnswered",
            success: successFunction
        });
    }


    function renderQuestionFromQueryParameterIfItExists(){
        var selector = $('#questionFromQueryStringPlaceholder');
        var id = getIdFromQueryString();

        var success = function(questionHolder){
            if(!questionHolder.question){
                return;
            }

            selector.append("<h3>Question</h3>");
            selector.append(renderQuestion(questionHolder));
        };

        if(selector != null && id != null)
            getQuestionById(id, success);
    }

    function getQuestionById(id, successFunction){
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: "service/question/" + id,
            success: successFunction
        });
    }

    function buildNameDateDisplay(name, fromNow) {
        var nameDateWrapper = $("<div/>", {
            class: 'rightAlign alignMiddleForBootstrap'
        });
        var nameElement = $("<b/>", {
            text: name
        });
        nameDateWrapper.append(nameElement);
        nameDateWrapper.append(" : ");
        nameDateWrapper.append(fromNow);
        return nameDateWrapper;
    }

    function buildQuestionAnswerDisplay(questionHolder, answer) {
        var panelHeader = $("<div/>", {
            class: "panel-heading",
            text: "Q: " + questionHolder.question
        });

        var panelBody = $("<div/>", {
            class: "panel-body",
            text: "A: " + answer
        });
        var panelWrapper = $("<div/>", {
            class: "panel panel-info"
        });

        panelWrapper.append(panelHeader);
        panelWrapper.append(panelBody);
        return panelWrapper;
    }

    function renderQuestion(questionHolder) {
        var dateValue = moment(questionHolder.date);
        var fromNow = dateValue.fromNow();
        var answer = "Question Not Answered Yet";
        if (questionHolder.answer != null)
            answer = questionHolder.answer;


        var nameDateWrapper = buildNameDateDisplay(questionHolder.name, fromNow);
        var component = $("<div/>",{
            class: 'container'
        });

        var panelWrapper = buildQuestionAnswerDisplay(questionHolder, answer);
        component.append(nameDateWrapper);
        component.append(panelWrapper);

        return component;
    }

    function getIdFromQueryString(){
        var id = window.location.search.substring(4);
        if(!isNaN(parseInt(id))){
            return id;
        }
    }

    function displayQuestion(parentElement, id){
        var selector = $('#' + parentElement);

        var success = function(questionHolder){
            if(!questionHolder.question){
                return;
            }
            selector.append(renderQuestion(questionHolder));
        };

        if(selector != null && id != null)
            getQuestionById(id, success);

    }


    $(function(){
        renderLatestQuestions();
    });

    return {
        renderQuestionFromQueryParameterIfItExists: renderQuestionFromQueryParameterIfItExists,
        displayQuestion: displayQuestion
    };

})();