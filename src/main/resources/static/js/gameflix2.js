// 캐러셀 초기화 함수
function initCarousel() {
const container = document.getElementById('gamesContainer2');
const games = container.getElementsByClassName('modalLink2');
let isMoving = false;

// 슬라이드 이벤트 처리 함수
function slide(direction) {
  if (isMoving) return;
  isMoving = true;

  const scrollStep = 200; // 필요에 따라 스크롤 스텝을 조정합니다.
  const scrollAmount = direction === 'left' ? -scrollStep : scrollStep;
  const currentScroll = container.scrollLeft;
  let newScroll = currentScroll + scrollAmount;

  // 스크롤 한계 설정하여 너무 멀리 가지 않도록 합니다.
  const scrollLimit = container.scrollWidth - container.clientWidth;
  newScroll = Math.min(Math.max(newScroll, 0), scrollLimit);

  // 스크롤 애니메이션
  const startTime = performance.now();
  function step(timestamp) {
    const elapsed = timestamp - startTime;
    container.scrollLeft = currentScroll + (newScroll - currentScroll) * elapsed / 200; // 200ms 애니메이션 지속 시간
    if (elapsed < 200) {
      requestAnimationFrame(step);
    } else {
      isMoving = false;
    }
  }
  requestAnimationFrame(step);
}

// 캐러셀 버튼용 이벤트 리스너 추가
const leftButton = document.getElementById('carouselLeft2');
const rightButton = document.getElementById('carouselRight2');

leftButton.addEventListener('click', () => slide('left'));
rightButton.addEventListener('click', () => slide('right'));
}

// 문서가 준비될 때까지 기다립니다.
document.addEventListener('DOMContentLoaded', initCarousel);
