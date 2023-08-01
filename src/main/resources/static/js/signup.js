let userIdError = '';
let passwordError = '';
let passwordCheckError = '';
let nicknameError = '';

function showErrorPopup(message) {
  alert(message);
}

function validateUserId() {
  const userId = document.getElementById('userId').value.trim();
  const userIdRegex = /^[A-Za-z0-9]{4,20}$/;
  if (userId === '') {
    userIdError = '아이디를 입력해주세요.';
  } else if (!userIdRegex.test(userId)) {
    userIdError = '아이디 : 숫자와 영문으로만 구성되며 4자 이상 20자 이하';
  } else {
    userIdError = '';
  }
}

function validatePassword() {
  const password = document.getElementById('password').value.trim();
  const passwordRegex = /^[A-Za-z0-9]{4,20}$/;
  if (password === '') {
    passwordError = '비밀번호를 입력해주세요.';
  } else if (!passwordRegex.test(password)) {
    passwordError = '비밀번호 : 숫자와 영문으로만 구성되며 4자 이상 20자 이하';
  } else {
    passwordError = '';
  }
}

function validatePasswordCheck() {
  const passwordCheck = document.getElementById('passwordCheck').value.trim();
  const password = document.getElementById('password').value.trim();
  if (passwordCheck === '') {
    passwordCheckError = '비밀번호를 재입력해주세요.';
  } else if (passwordCheck !== password) {
    passwordCheckError = '비밀번호가 일치하지 않습니다.';
  } else {
    passwordCheckError = '';
  }
}

function validateNickname() {
  const nickname = document.getElementById('nickname').value.trim();
  const nicknameRegex = /^[A-Za-z0-9가-힣]{1,20}$/;
  if (nickname === '') {
    nicknameError = '닉네임을 입력해주세요.';
  } else if (!nicknameRegex.test(nickname)) {
    nicknameError = '닉네임 : 한글, 숫자, 영문으로만 구성되며 20자 이하';
  } else {
    nicknameError = '';
  }
}

function validateForm() {
  validateUserId();
  validatePassword();
  validatePasswordCheck();
  validateNickname();

  if (userIdError || passwordError || passwordCheckError || nicknameError) {
    let errorMessage = '';
    if (userIdError) errorMessage += userIdError + '\n';
    if (passwordError) errorMessage += passwordError + '\n';
    if (passwordCheckError) errorMessage += passwordCheckError + '\n';
    if (nicknameError) errorMessage += nicknameError;

    showErrorPopup(errorMessage);
    return false;
  }

  return true;
}