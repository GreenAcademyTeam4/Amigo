$(document).ready(function() {
    const mpg = $('#mypage');
    const enter = $('.enter');
    const post = $('.post');
    window.screen = $('.screen-area');
    const logout = $('.logout');
    const school = $('.other-school');
    const friends = $('.friends');


    // 학교 바꾸기 기능 추가
    school.each(function (index, element) {
        $(element).on('click', function () {
            const schoolId = $(this).data('id');
            fetch(`/main/changeSchool?id=${schoolId}`)
                .then(response => response.text())
                .then(data => {
                    screen.html(data);
                })
                .catch(error => {
                    console.error('학교 변경 중 오류 발생:', error);
                });
        });
    });

    // 로그아웃 이벤트 추가 (로그인 화면으로)
    logout.on('click', function() {
        window.location.href = "/";
    });

    // 페이지 로드 시 게시판 콘텐츠 먼저 로드
    fetch("/board/multiBoard")
        .then(response => response.text())
        .then(data => {
            screen.html(data);
        })
        .catch(error => {
            console.error('게시판 로딩 중 오류 발생:', error);
        });

    // 게시판으로 들어가는 이벤트 추가
    post.on('click', function() {
        fetch("/board/list")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('등교하기 중 오류 발생:', error);
            });
    });

    // 마이페이지 이동 이벤트 추가
    mpg.on('click', function() {
        fetch("/my-page/info")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('마이 페이지 로딩 중 오류 발생:', error);
            });
    });
});
$(document).ready(function() {
    const screen = $('.screen-area');
    $('.online-friend').on('click', function(event) {
        const $infoBox = $('.info-box');
        const id = $('.online-friend').attr('id');
        // 온라인 친구 클릭 시, 화상 채팅 버튼 포함하여 info-box 내용 설정
        $infoBox.html(`
            <div class="info-profile">프로필 보기</div>
            <div class="chat">1:1 채팅</div>
            <div class="voice-chat-btn">화상 채팅</div>
        `);

        // 클릭한 위치에 info-box 표시
        $infoBox.css({
            display: 'block',
            top: event.pageY + 5 + 'px',  // 클릭 위치에 맞게 설정
            left: event.pageX + 5 + 'px'
        });
    });

    $('.offline-friend').on('click', function(event) {
        const $infoBox = $('.info-box');
        const id = $('.offline-friend').attr('id');
        // 오프라인 친구 클릭 시, 화상 채팅 버튼 없이 info-box 내용 설정
        $infoBox.html(`
            <div class="info-profile">프로필 보기</div>
            <div class="chat">1:1 채팅</div>
        `);

        // 클릭한 위치에 info-box 표시
        $infoBox.css({
            display: 'block',
            top: event.pageY + 5 + 'px',
            left: event.pageX + 5 + 'px'
        });
    });

    // info-box 외부를 클릭하면 info-box 숨기기
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.info-box, .online-friend, .offline-friend').length) {
            $('.info-box').hide();
        }
    });

    // info-box 내부의 버튼 클릭 이벤트 추가
    $(document).on('click', '.info-profile', function() {
        // 프로필 보기 버튼 클릭 이벤트
        console.log("프로필 보기 클릭");
        // 여기에 필요한 기능 추가
    });

    $(document).on('click', '.chat', function() {
        // 1:1 채팅 버튼 클릭 이벤트
        fetch("/chat/chatroom/" + id)
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('게시판 로딩 중 오류 발생:', error);
            });
        console.log("1:1 채팅 클릭");
        // 여기에 필요한 기능 추가
    });

    $(document).on('click', '.voice-chat-btn', function() {
        fetch("/test")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('게시판 로딩 중 오류 발생:', error);
            });
        console.log("화상 채팅 클릭");
        // 여기에 필요한 기능 추가
    });
});