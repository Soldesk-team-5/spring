$(document).ready(function() {
    // 모달 열기
    var modal = document.getElementById("myModal");
    var modalFrame = document.getElementById("modalFrame");
    var closeBtn = document.getElementsByClassName("close")[0];

    // 이벤트 위임을 사용하여 부모 요소에 이벤트 리스너 등록
    $("#gamesContainer").on("click", ".modalLink", function(e) {
        e.preventDefault(); // 기본 링크 동작 막기
        modal.style.display = "block";
        modalFrame.src = this.href;
        document.body.style.overflow = 'hidden';
    });

    $("#gamesContainer2").on("click", ".modalLink2", function(e) {
            e.preventDefault(); // 기본 링크 동작 막기
            modal.style.display = "block";
            modalFrame.src = this.href;
            document.body.style.overflow = 'hidden';
        });

    $("#gamesContainer3").on("click", ".modalLink3", function(e) {
            e.preventDefault(); // 기본 링크 동작 막기
            modal.style.display = "block";
            modalFrame.src = this.href;
            document.body.style.overflow = 'hidden';
        });

    $("#gamesContainer4").on("click", ".modalLink4", function(e) {
            e.preventDefault(); // 기본 링크 동작 막기
            modal.style.display = "block";
            modalFrame.src = this.href;
            document.body.style.overflow = 'hidden';
        });

    $("#gamesContainer5").on("click", ".modalLink5", function(e) {
            e.preventDefault(); // 기본 링크 동작 막기
            modal.style.display = "block";
            modalFrame.src = this.href;
            document.body.style.overflow = 'hidden';
        });

    // 모달 닫기
    closeBtn.onclick = function() {
      modal.style.display = "none";
      modalFrame.src = ""; // iframe 내용 초기화
      document.body.style.removeProperty('overflow');
    };

    // 모달 바깥 영역 클릭 시 모달 닫기
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
        modalFrame.src = ""; // iframe 내용 초기화
        document.body.style.removeProperty('overflow');
      }
    };
});
