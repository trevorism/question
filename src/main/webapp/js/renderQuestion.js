var renderQuestion = (function (){

    function render(parentElement, id){
        var success = function(questionHolder){
            if(!questionHolder.question){
                return;
            }
            $('#questionHeaderPlaceholder').append("<h3>Question</h3>");
            displayElementFromQuestion(parentElement, questionHolder);
        };

        if(parentElement != null && id != null)
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

    function buildNameDateWrapper(name, fromNow) {
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

    function displayElementFromQuestion(element, questionHolder) {
        var selector = $('#' + element);
        var dateValue = moment(questionHolder.date);
        var fromNow = dateValue.fromNow();
        var answer = "Question Not Answered Yet";
        if (questionHolder.answer != null)
            answer = questionHolder.answer;


        var nameDateWrapper = buildNameDateWrapper(questionHolder.name, fromNow);
        var component = $("<div/>",{
            class: 'container'
        });

        var panelHeader = $("<div/>",{
            class: "panel-heading",
            text: "Q: " + questionHolder.question
        });

        var panelBody = $("<div/>",{
            class: "panel-body",
            text: "A: " + answer
        });
        var panelWrapper = $("<div/>", {
           class: "panel panel-info"
        });

        panelWrapper.append(panelHeader);
        panelWrapper.append(panelBody);
        component.append(nameDateWrapper);
        component.append(panelWrapper);
        selector.append(component);

    }

    function renderQuestionFromQueryString(){
        var id = window.location.search.substring(4);
        if(!isNaN(parseInt(id))){
            render("queryStringPlaceholder", id);
        }
    }

    function displayLatest(){
        var selector = $('#latestQuestionsPlaceholder');


    }

    return {
        fromId: render,
        displayLatest: displayLatest,
        fromQueryString : renderQuestionFromQueryString
    };

})();