$(window).on('scroll', function () {
    const $header = $('header');
    const $logo = $('.logo');

    if ($(window).scrollTop() > 0) {
        $header.addClass('scrolled');
        $logo.addClass('hide');
    } else {
        $header.removeClass('scrolled');
        $logo.removeClass('hide');
    }
});
    const alarmSocket = new WebSocket("ws://172.30.1.90:8080/alarm");
    let callStatus = false;
    const alarmSound = document.getElementById("alarmSound");
    alarmSocket.onmessage = (event) => {
        const alarm = JSON.parse(event.data);
        console.log(alarm);
        if(alarm.type === 'chat') {

        } else if (alarm.type === 'post') {

        } else if (alarm.type === 'voice') {
            if (alarm.content === 'request') {
                if(callStatus === false) {
                    createRequest(alarm); // 통화 요청 UI 생성
                    alarmSound.play();
                } else {
                alarmSocket.send(JSON.stringify({
                    type: 'voice',
                    senderId: userId,
                    receiverId: senderId,
                    content: 'callback'
                }));
            }
        } else if (alarm.content === 'refuse') {
            alert("상대방이 화상 채팅 요청을 거절하였습니다.");
            fetch(`/board/list`)
                .then(response => response.text())
                .then(data => {
                    screen.html(data);
                })
                .catch(error => {
                    console.error('화면 로딩 중 오류 발생', error);
                });
            tryCall = false;
        } else if (alarm.content === 'accept') {
            callStatus = true;
        } else if (alarm.content === 'callback') {
            console.log("상대방이 통화중");
            alert("상대방이 통화중 입니다.");
            fetch(`/board/list`)
                .then(response => response.text())
                .then(data => {
                    screen.html(data);
                })
                .catch(error => {
                    console.error('화면 로딩 중 오류 발생', error);
                });
        }
    }
};

$(document).ready(function () {
    $('body').css('opacity', '1');
    $('.transition-link a').on('click', function (e) {
        var linkUrl = $(this).attr('href');
        e.preventDefault();
        $('body').animate({ opacity: 0 }, 500, function () {
            window.location.href = linkUrl;
        });
    });
    window.addEventListener('pageshow', function (event) {
        if (event.persisted || window.performance && window.performance.navigation.type === 2) {
            $('body').css('opacity', '1');
        }
    });
});

    $(document).ready(function() {
        $('body').css('opacity', '1');
        $('.transition-link a').on('click', function(e) {
            var linkUrl = $(this).attr('href');
            e.preventDefault();
            $('body').animate({ opacity: 0 }, 500, function() {
                window.location.href = linkUrl;
            });
        });
        window.addEventListener('pageshow', function(event) {
            if (event.persisted || window.performance && window.performance.navigation.type === 2) {
                $('body').css('opacity', '1');
            }
        });
    });

    $(document).ready(function () {
        $('#alarm-icon').on('click', function () {
            var $alarmBox = $('.alarm-box');

            // 알림창이 숨겨져 있으면 슬라이드 인, 그렇지 않으면 슬라이드 아웃
            if ($alarmBox.css('display') === 'none') {
                $alarmBox.removeClass('hide-slide-out').addClass('show-slide-in');
                $alarmBox.show();
            } else {
                $alarmBox.removeClass('show-slide-in').addClass('hide-slide-out');
                setTimeout(function () {
                    $alarmBox.hide();
                }, 400); // 애니메이션이 끝난 후 숨김 처리
            }
        });

        // 클릭이 알림 박스 외부에서 발생하면 닫기
        $(document).on('click', function (event) {
            var $target = $(event.target);
            if (!$target.closest('.alarm-box').length && !$target.is('#alarm-icon')) {
                $('.alarm-box').removeClass('show-slide-in').addClass('hide-slide-out');
                setTimeout(function () {
                    $('.alarm-box').hide();
                }, 400);
            }
        });
    });

    // 통화 요청 UI 생성 함수
    function createRequest(alarm) {
        // 프로필 이미지, 닉네임, 버튼 컨테이너 HTML 구조 생성
        const requestContent = `
            <div class="sender-profile" id="sender-profile"></div>
            <div class="sender-nickname" id="sender-nickname">${alarm.senderNickname} <br>님의 통화 요청</div>
            <div class="button-container">
                <button class="accept-btn" onclick="acceptInvitation(${alarm.senderId})">✔</button>
                <button class="refuse-btn" onclick="declineInvitation(${alarm.senderId})">✘</button>
            </div>
        `;

        // voice-chat 요소 업데이트
        $('.voice-chat').html(requestContent).removeClass('hide').addClass('slide-in');
    }

    // 수락 버튼 클릭 이벤트
    function acceptInvitation(senderId) {
        // 수락 시 특정 경로로 fetch 요청 보내기
        alarmSocket.send(JSON.stringify({
            type: 'voice',
            senderId: userId,
            receiverId: senderId,
            content: 'accept'
        }));
        fetch(`/test/${senderId}`)
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('화면 로딩 중 오류 발생', error);
            });
        callStatus = true;
        $('.voice-chat').addClass('hide'); // UI 숨기기
        // 알람음 중지 및 위치 초기화
        alarmSound.pause();
        alarmSound.currentTime = 0;

    }

    // 거절 버튼 클릭 이벤트
    function declineInvitation(senderId) {

        // 거절 메시지 WebSocket으로 전송
        alarmSocket.send(JSON.stringify({
            type: 'voice',
            senderId: userId,
            receiverId: senderId,
            content: 'refuse'
        }));

        $('.voice-chat').addClass('hide'); // UI 숨기기
        // 알람음 중지 및 위치 초기화
        alarmSound.pause();
        alarmSound.currentTime = 0;

    }