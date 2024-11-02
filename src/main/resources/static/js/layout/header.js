const socket = new WebSocket("ws://localhost:8080/alarm");
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
socket.onmessage = (event) => {
    const alarm = JSON.parse(event.data);
    if (alarm.type === 'chat') {

    } else if (alarm.type === 'post') {

    } else if (alarm.type === 'voice') {

        $('.voice-chat').removeClass('hide').addClass('slide-in'); // 통화 요청이 오면 표시
        setTimeout(() => {
            $('.voice-chat').removeClass('slide-in'); // 애니메이션 후에 클래스 유지
        }, 500);
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
