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
                            alert("장바구니를 이용하시려면, 로그인 해주세요!");
                        },
        });
    });
});