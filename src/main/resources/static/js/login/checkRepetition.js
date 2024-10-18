$(document).ready(function() {
    let timerInterval;
    var typingTimer;
    var doneTypingInterval = 1000;
    var isUserIdValid = false;
    var isPasswordValid = false;
    var isBirthValid = false;
    var isGenderSelected = false;
    var isSubmitting = false;  // 중복 제출 방지 플래그
    var isSmsSent = false;     // SMS 발송 여부

    // 아이디 입력 체크
    $('#userId').on('keyup', function() {
        clearTimeout(typingTimer);
        var userId = $(this).val();
        if (userId.length > 0) {
            typingTimer = setTimeout(function() {
                checkUserId(userId);
            }, doneTypingInterval);
        } else {
            $('#userIdMessage').text('').removeClass('success error');
            $('#userId').css('border', '1px solid #ddd').css('border-bottom', 'none');
            isUserIdValid = false;
        }
    });

    // 비밀번호 입력 체크
    $('#password').on('keyup', function() {
        clearTimeout(typingTimer);
        var password = $(this).val();
        if (password.length > 0) {
            typingTimer = setTimeout(function() {
                checkPassword(password);
            }, doneTypingInterval);
        } else {
            $('#passwordMessage').text('').removeClass('success error');
            $('#password').css('border', '1px solid #ddd');
            isPasswordValid = false;
        }
    });

    // 생년월일 체크 (정확히 6자리 숫자인지 확인)
    $('#birth').on('keyup', function() {
        var birth = $(this).val();
        if (/^\d{6}$/.test(birth)) {  // 생년월일이 6자리인지 확인
            isBirthValid = true;
            $('#birth').css('border', '1px solid #ddd');
        } else {
            isBirthValid = false;
            $('#birth').css('border', '2px solid red');
        }
    });

    // 성별 체크
    $('input[name="gender"]').on('change', function() {
        isGenderSelected = $('input[name="gender"]:checked').length > 0;
    });

    // 아이디 중복 확인 함수
    function checkUserId(userId) {
        $.ajax({
            url: '/user/checkUserId',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ userId: userId }),
            success: function(response) {
                if (response.repetition === "repetition") {
                    $('#userIdMessage').text('*중복된 아이디입니다.').addClass('error').removeClass('success');
                    $('#userId').css('border', '2px solid red');
                    isUserIdValid = false;
                } else if (response.repetition === "iderror") {
                    $('#userIdMessage').text('*아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.').addClass('error').removeClass('success');
                    $('#userId').css('border', '2px solid red');
                    isUserIdValid = false;
                } else {
                    $('#userIdMessage').text('*사용 가능한 아이디입니다.').addClass('success').removeClass('error');
                    $('#userId').css('border', '1px solid #ddd').css('border-bottom', 'none');
                    isUserIdValid = true;
                }
            },
            error: function(xhr, status, error) {
                $('#userIdMessage').text('*중복 확인 중 오류 발생.').addClass('error').removeClass('success');
                $('#userId').css('border', '2px solid red');
                isUserIdValid = false;
            }
        });
    }

    // 비밀번호 유효성 검사 함수
    function checkPassword(password) {
        const lowercasePattern = /[a-z]/;
        const uppercasePattern = /[A-Z]/;
        const digitPattern = /\d/;
        const specialCharPattern = /[!@#$%^&*()_+|~=`{}\[\]:";'<>?,./]/;

        var matchCount = 0;
        if (lowercasePattern.test(password)) matchCount++;
        if (uppercasePattern.test(password)) matchCount++;
        if (digitPattern.test(password)) matchCount++;
        if (specialCharPattern.test(password)) matchCount++;

        if (password.length >= 8 && password.length <= 16 && matchCount >= 2) {
            $('#passwordMessage').text('*사용 가능한 비밀번호입니다.').removeClass('error').addClass('success');
            $('#password').css('border', '1px solid #ddd');
            isPasswordValid = true;
        } else {
            $('#passwordMessage').text('*비밀번호는 8~16자이고, 영문 대문자, 소문자, 숫자, 특수문자 중 두 가지 이상을 포함해야 합니다.').removeClass('success').addClass('error');
            $('#password').css('border', '2px solid red');
            isPasswordValid = false;
        }
    }

    // 버튼 클릭 이벤트 처리 (submit 대신)
    $("#sendSmsBtn").on("click", function() {
        if (isSubmitting || isSmsSent) return;  // 중복 제출 방지

        // 유효성 검사 통과 여부 확인
        if (isUserIdValid && isPasswordValid && isBirthValid && isGenderSelected) {
            const formData = {
                phoneNumber: $("#phoneNumber").val()
            };

            // 중복 제출 방지 플래그 설정
            isSubmitting = true;

            // 인증번호 발송 요청
            fetch('/sms/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            })
            .then(response => response.text())
            .then(data => {
                alert("인증번호가 발송되었습니다: " + data);
                $("#authSection").show(); // 인증번호 입력란과 확인 버튼 표시

                // 타이머 시작
                startTimer(180);  // 3분 타이머 설정
                $("#sendSmsBtn").text("재발송").prop("disabled", true); // 버튼 텍스트를 "재발송"으로 변경하고 비활성화
                isSmsSent = true;  // 인증번호 발송 상태 갱신

                // 인증번호 발송 후 입력 필드 비활성화
                $("#userId").prop("disabled", true);
                $("#password").prop("disabled", true);
                $("#name").prop("disabled", true);
                $("#phoneNumber").prop("disabled", true);
                $("#birth").prop("disabled", true);
                $("input[name='gender']").prop("disabled", true);
                $("input[name='terms']").prop("disabled", true);
            })
            .catch(error => console.error('Error:', error))
            .finally(() => {
                isSubmitting = false;  // 요청 처리 후 플래그 해제
            });
        } else {
            alert("아이디, 비밀번호, 생년월일 또는 성별을 올바르게 입력하세요.");
        }
    });

    // 인증 확인 버튼 클릭 시
    $("#verifyCodeBtn").on("click", function() {
        const authCode = $("#authCodeInput").val();

        fetch('/sms/verify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ authCode: authCode })
        })
        .then(response => response.text())
        .then(data => {
            if (data.trim() === "Verification successful") {
                alert("인증에 성공했습니다.");
                $("#verifyCodeBtn").text("인증완료").prop("disabled", true);
                $("#authCodeInput").prop("disabled", true);
                clearInterval(timerInterval);  // 타이머 멈추기
                $("#sendSmsBtn").hide();  // 재발송 버튼 히든 처리

                // 인증 완료 후 가입하기 버튼 활성화
                $("#joinDone").show(); 
            } else {
                alert("인증번호가 올바르지 않습니다.");
            }
        })
        .catch(error => console.error('Error:', error));
    });

    // 타이머 시작 함수
    function startTimer(duration) {
        let timer = duration, minutes, seconds;
        clearInterval(timerInterval);  // 기존 타이머 초기화
        timerInterval = setInterval(function() {
            minutes = Math.floor(timer / 60);
            seconds = timer % 60;
            seconds = seconds < 10 ? '0' + seconds : seconds;

            // 타이머 출력
            $('#timer').text(`${minutes}:${seconds}`);

            if (--timer < 0) {
                clearInterval(timerInterval);
                $('#timer').text("");  // 타이머 종료 시 텍스트 삭제
                alert("인증 시간이 만료되었습니다. 인증번호를 재발송해주세요.");

                // 인증번호 입력란 비활성화
                $("#authCodeInput").prop("disabled", true);
                $("#verifyCodeBtn").prop("disabled", true);

                // 세션에 저장된 인증번호 삭제 요청
                fetch('/clearSessionCode', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(() => {
                    isSmsSent = false;  // 인증번호 만료 후 다시 발송 가능하게 설정
                    $("#sendSmsBtn").prop("disabled", false);  // 재발송 버튼 활성화
                    $("#sendSmsBtn").text("재발송"); // 버튼 텍스트 "재발송"으로 변경
                    console.log("Session code cleared");
                })
                .catch(error => console.error('Error clearing session code:', error));
            }
        }, 1000);
    }

    $("#joinBtn").on("click", function() {
        // 새로운 폼 엘리먼트를 동적으로 생성
        const form = document.createElement("form");
        form.setAttribute("method", "POST");
        form.setAttribute("action", "/user/join");  // 서버로 보낼 경로 설정
    
        // userId 입력 필드 생성 및 폼에 추가
        const userIdField = document.createElement("input");
        userIdField.setAttribute("type", "hidden");
        userIdField.setAttribute("name", "userId");
        userIdField.setAttribute("value", $("#userId").val());
        form.appendChild(userIdField);
    
        // password 입력 필드 생성 및 폼에 추가
        const passwordField = document.createElement("input");
        passwordField.setAttribute("type", "hidden");
        passwordField.setAttribute("name", "password");
        passwordField.setAttribute("value", $("#password").val());
        form.appendChild(passwordField);
    
        // name 입력 필드 생성 및 폼에 추가
        const nameField = document.createElement("input");
        nameField.setAttribute("type", "hidden");
        nameField.setAttribute("name", "name");
        nameField.setAttribute("value", $("#name").val());
        form.appendChild(nameField);
    
        // phoneNumber 입력 필드 생성 및 폼에 추가
        const phoneNumberField = document.createElement("input");
        phoneNumberField.setAttribute("type", "hidden");
        phoneNumberField.setAttribute("name", "phoneNumber");
        phoneNumberField.setAttribute("value", $("#phoneNumber").val());
        form.appendChild(phoneNumberField);
    
        // birth 입력 필드 생성 및 폼에 추가
        const birthField = document.createElement("input");
        birthField.setAttribute("type", "hidden");
        birthField.setAttribute("name", "birth");
        birthField.setAttribute("value", $("#birth").val());
        form.appendChild(birthField);
    
        // gender 입력 필드 생성 및 폼에 추가
        const genderField = document.createElement("input");
        genderField.setAttribute("type", "hidden");
        genderField.setAttribute("name", "gender");
        genderField.setAttribute("value", $("input[name='gender']:checked").val());
        form.appendChild(genderField);
    
        // 폼을 body에 추가하고 제출
        document.body.appendChild(form);
        form.submit();  // 폼 제출
    });
});
