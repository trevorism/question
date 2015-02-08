
$( document ).ready(function() {

    $('#submitButton').click(function(){
        var data = {};
        data.name = $("#nameId").val();
        data.question = $("#questionId").val();

        $.ajax({
            type: "POST",
            contentType : 'application/json',
            url: "service/question/",
            dataType: 'text',
            data: JSON.stringify(data),
            success: function(data){

            }

        });
    });


});

