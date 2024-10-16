$(document).ready(function() {
    var typingTimer;
    var doneTypingInterval = 1000;

    $('#userId').on('keyup', function() {
        clearTimeout(typingTimer);
        var userId = $(this).val();
        if (userId.length > 0) {
            typingTimer = setTimeout(function() {
                checkUserId(userId);
            }, doneTypingInterval);
        } else {
            $('#userIdMessage').text('').removeClass('success error');
            $('#userId').css('border', '1px solid #ddd').css('border-bottom','none');
        }
    });

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
        }
    });

    function checkUserId(userId) {
        console.log("Checking userId:", userId);
        $.ajax({
            url: '/user/checkUserId',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ userId: userId }),
            success: function(response) {
                console.log("Server response:", response);
                if (response.repetition === "repetition") {
                    $('#userIdMessage').text('*중복된 아이디입니다.')
                        .addClass('error')
                        .removeClass('success');
                    $('#userId').css('border', '2px solid red');
                } else if(response.repetition === "iderror") {
                    $('#userIdMessage').text('*아이디: 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.')
                        .addClass('error')
                        .removeClass('success');
                    $('#userId').css('border', '2px solid red');
                } else {
                    $('#userIdMessage').text('*사용 가능한 아이디입니다.')
                        .addClass('success')
                        .removeClass('error');
                    $('#userId').css('border', '1px solid #ddd').css('border-bottom','none');
                }
            },
            error: function(xhr, status, error) {

                $('#userIdMessage').text('*중복 확인 중 오류 발생.')
                    .addClass('error')
                    .removeClass('success');
                $('#userId').css('border', '2px solid red');
            }
        });
    }
    function checkPassword(password){
        const lowercasePattern = /[a-z]/;    // 소문자
        const uppercasePattern = /[A-Z]/;    // 대문자
        const digitPattern = /\d/;           // 숫자
        const specialCharPattern = /[!@#$%^&*()_+|~=`{}\[\]:";'<>?,./]/;

        $('#password').on('keyup', function() {
            var password = $(this).val();
            var message = '';
            var matchCount = 0;


            if (password === '') {
                $('#passwordMessage').text('').removeClass('error success');
                return;
            }

            // 각 패턴이 비밀번호에 포함되어 있는지 확인
            if (lowercasePattern.test(password)) matchCount++;
            if (uppercasePattern.test(password)) matchCount++;
            if (digitPattern.test(password)) matchCount++;
            if (specialCharPattern.test(password)) matchCount++;

            // 비밀번호가 8자 이상 16자 이하이고 두 가지 이상의 패턴을 만족하는 경우
            if (password.length >= 8 && password.length <= 16 && matchCount >= 2) {
                message = '*사용 가능한 비밀번호입니다.';
                $('#passwordMessage').removeClass('error').addClass('success');
                $('#password').css('border', '1px solid #ddd');
            } else {
                message = '*비밀번호는 8~16자이고, 영문 대문자, 소문자, 숫자, 특수문자 중 두 가지 이상을 포함해야 합니다.';
                $('#passwordMessage').removeClass('success').addClass('error');
                $('#password').css('border', '2px solid red');
            }

            $('#passwordMessage').text(message);
        });

    }
});

