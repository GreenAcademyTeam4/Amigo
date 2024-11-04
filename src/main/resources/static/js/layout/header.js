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

const alarmSocket = new WebSocket("ws://192.168.112.46:8080/alarm");
let callStatus = false;
const alarmSound = document.getElementById("alarmSound");

alarmSocket.onmessage = (event) => {
    const alarm = JSON.parse(event.data);
    console.log(alarm);
    if (alarm.type === 'chat') {
        // Handle chat alarms
    } else if (alarm.type === 'post') {
        // Handle post alarms
    } else if (alarm.type === 'voice') {
        if (alarm.content === 'request') {
            if (callStatus === false) {
                createRequest(alarm); // 통화 요청 UI 생성
                alarmSound.play();
                // 사운드가 종료될 때마다 반복 재생
                alarmSound.addEventListener("ended", function() {
                    alarmSound.currentTime = 0; // 시작 위치로 이동
                    alarmSound.play();          // 재생
                });
            } else {
                alarmSocket.send(JSON.stringify({
                    type: 'voice',
                    senderId: userId,
                    receiverId: alarm.senderId,
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
            callStatus = false;
        } else if (alarm.content === 'accept') {
            callStatus = true;
        } else if (alarm.content === 'callback') {
            alert("상대방이 통화 중 입니다.");
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

    $('#alarm-icon').on('click', function () {
        var $alarmBox = $('.alarm-box');

        if ($alarmBox.css('display') === 'none') {
            $alarmBox.removeClass('hide-slide-out').addClass('show-slide-in');
            $alarmBox.show();
        } else {
            $alarmBox.removeClass('show-slide-in').addClass('hide-slide-out');
            setTimeout(function () {
                $alarmBox.hide();
            }, 400);
        }
    });

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

function createRequest(alarm) {
    const requestContent = `
        <div class="sender-profile" id="sender-profile"></div>
        <div class="sender-nickname" id="sender-nickname">${alarm.senderNickname} <br>님의 통화 요청</div>
        <div class="button-container">
            <button class="accept-btn" onclick="acceptInvitation(${alarm.senderId})">✔</button>
            <button class="refuse-btn" onclick="declineInvitation(${alarm.senderId})">✘</button>
        </div>
    `;

    $('.voice-chat').html(requestContent).removeClass('hide').addClass('slide-in');
}
// 통화 수락하는 함수
function acceptInvitation(senderId) {
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
    $('.voice-chat').addClass('hide');
    alarmSound.removeEventListener();
    alarmSound.pause();
    alarmSound.currentTime = 0;
}
// 통화 거절하는 함수
function declineInvitation(senderId) {
    alarmSocket.send(JSON.stringify({
        type: 'voice',
        senderId: userId,
        receiverId: senderId,
        content: 'refuse'
    }));
    $('.voice-chat').addClass('hide');
    alarmSound.removeEventListener();
    alarmSound.pause();
    alarmSound.currentTime = 0;
}

$(document).ready(function() {
    // 친구 관리
    // $('#friendLink').on('click', function(event) {
    //     event.preventDefault();
    //     fetchContent('/friend');
    // });

    $('#storeLink').on('click', function(event) {
        fetch('/store/shop')
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('화면 로딩 중 오류 발생', error);
            });
    });
    
    // 공지 사이트
    // $('#noticeLink').on('click', function(event) {
    //     event.preventDefault();
    //     fetchContent('/notice');
    // });

    // 쪽지 사이트
    // $('#messageLink').on('click', function(event) {
    //     event.preventDefault();
    //     fetchContent('/message');
    // });

    function fetchContent(url) {
        fetch(url, {
            method: 'GET'
        })
            .then(response => response.text())
            .then(data => {
                $('#screen-area').html(data);
            })
            .catch(error => console.error('Error fetching content:', error));
    }
});