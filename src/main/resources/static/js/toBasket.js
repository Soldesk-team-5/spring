$(document).ready(function() {
    $('button[name="toBasket"]').click(function() {
        var game = document.getElementById("gamename").innerText;
        $.ajax({
            url: "/toBasket",
            type: "GET",
            data: {name : game},
            dataType: "text",
            success: function(data) {
                alert(data);
                },
            error: function(){
                            alert("Error occurred");
                        },
        });
    });
});