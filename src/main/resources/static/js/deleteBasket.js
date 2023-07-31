$(document).ready(function () {
    $("#deleteBtn").click(function () {
        var checkedItems = [];
        $(".itemCheckbox:checked").each(function () {
            checkedItems.push($(this).val());
        });

        if (checkedItems.length > 0) {
            var data = {
                "items": checkedItems
            };

            $.ajax({
                type: "GET",
                url: "/basketDelete", // 서버의 컨트롤러 엔드포인트 URL
                data: data,
                success: function (response) {
                    alert("선택한 게임이 삭제되었습니다.");
                    window.location.reload(); // 페이지 새로고침
                },
                error: function () {
                    console.log("Error");
                }
            });
        } else {
            alert("삭제할 항목을 선택해주세요.");
        }
    });
});