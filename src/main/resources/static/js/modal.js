// 팝업 레이어 열기
const modal = document.getElementById("showGameModal");
const btn = document.getElementById("openModalBtn"); // 팝업을 열 버튼 선택자에 맞게 수정
const span = document.getElementsByClassName("close")[0];

btn.addEventListener("click", function() {
  modal.style.display = "block";
});

span.addEventListener("click", function() {
  modal.style.display = "none";
});

window.addEventListener("click", function(event) {
  if (event.target === modal) {
    modal.style.display = "none";
  }
});