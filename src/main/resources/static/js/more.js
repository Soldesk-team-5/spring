document.addEventListener("DOMContentLoaded", function() {
  const container = document.querySelector(".tag-container");
  const items = document.querySelectorAll(".tag-item");
  const moreButton = document.getElementById("load");
  const itemsPerLoad = 20; // 한 번에 추가로 표시할 아이템 개수
    let currentItemsToShow = itemsPerLoad; // 현재 표시된 아이템 개수

    // 초기에 일부 아이템을 표시
    for (let i = 0; i < currentItemsToShow; i++) {
      items[i].style.display = "flex";
    }

    // 더보기 버튼 클릭 시 추가 아이템 표시
    moreButton.addEventListener("click", function() {
      const remainingItems = items.length - currentItemsToShow;
      const itemsToShow = Math.min(itemsPerLoad, remainingItems);
      for (let i = currentItemsToShow; i < currentItemsToShow + itemsToShow; i++) {
        items[i].style.display = "flex";
      }
      currentItemsToShow += itemsToShow;

      // 모든 아이템을 표시한 경우 더보기 버튼 숨김
      if (currentItemsToShow >= items.length) {
        moreButton.style.display = "none";
      }
    });
  });