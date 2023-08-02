let nicknameError = '';
let passwordError = '';

function validateNickname() {
  const nickname = document.getElementById('changedNickname').value.trim();
  const nicknameRegex = /^[A-Za-z0-9가-힣]{1,20}$/;
  if (!nicknameRegex.test(nickname)) {
    nicknameError = '닉네임 : 한글, 숫자, 영문으로만 구성되며 20자 이하';
  } else {
    nicknameError = '';
  }
}

function validatePassword() {
  const password = document.getElementById('changedPassword').value.trim();
  const passwordRegex = /^[A-Za-z0-9]{4,20}$/;
  if (!passwordRegex.test(password)) {
    passwordError = '비밀번호 : 숫자와 영문으로만 구성되며 4자 이상 20자 이하';
  } else {
    passwordError = '';
  }
}

function validateForm() {

  validateNickname();
  validatePassword();

  if (passwordError || nicknameError) {
      let errorMessage = '';
      if (passwordError) errorMessage += passwordError + '\n';
      if (nicknameError) errorMessage += nicknameError;

      showErrorPopup(errorMessage);
      return false;
    }

  return true;
}