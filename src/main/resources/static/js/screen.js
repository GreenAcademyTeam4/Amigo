$(document).ready(function() {
    const enter = $('.enter');
    const post = $('#post');
    window.screen = $('.screen-area');
    const logout = $('.logout');
    const school = $('.other-school');
    const friends = $('.friends');
    const mypage = $('#my-page'); // 올바르게 선언
    window.tryCall = false;

    // 화상 채팅 버튼 활성화/비활성화 설정 함수
    function updateVoiceChatButtonState() {
        if (tryCall) {
            $('.voice-chat-btn').prop('disabled', true).css('background-color', 'gray').css('cursor', 'not-allowed');
        } else {
            $('.voice-chat-btn').prop('disabled', false).css('background-color', '').css('cursor', 'pointer');
        }
    }

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

   mypage.on('click', function() {
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

    const screen = $('.screen-area');
    $('.online-friend').on('click', function(event) {
        const $infoBox = $('.info-box');
        const id = $(this).attr('id'); // 클릭된 요소의 id 가져오기

        console.log("클릭된 친구 ID:", id); // 디버깅 로그

        // info-box 내용 설정
        $infoBox.html(`
            <button class="info-profile" id="${id}">프로필 보기</button>
            <button class="chat" id="${id}">1:1 채팅</button>
            <button class="voice-chat-btn" id="${id}">화상 채팅</button>
        `);

        // 클릭한 위치에 info-box 표시
        $infoBox.css({
            display: 'block',
            top: event.pageY + 5 + 'px',
            left: event.pageX + 5 + 'px'
        });

        // 화상 채팅 버튼 상태 업데이트
        updateVoiceChatButtonState();
    });

    // 이벤트 위임을 사용한 오프라인 친구 클릭 핸들러
    $(document).on('click', '.offline-friend', function(event) {
        console.log("오프라인 친구 클릭됨"); // 디버깅 로그
        const $infoBox = $('.info-box');
        const id = $(this).attr('id'); // 클릭된 요소의 id 가져오기

        console.log("클릭된 친구 ID:", id); // 디버깅 로그

        // info-box 내용 설정
        $infoBox.html(`
            <button class="info-profile" id="${id}">프로필 보기</button>
            <button class="chat" id="${id}">1:1 채팅</button>
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
        const id = $(this).attr("id");
        console.log("1:1 채팅 클릭, ID:", id); // 디버깅 로그
        // 1:1 채팅 버튼 클릭 이벤트
        fetch("/chat/chatRoom/" + id)
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('게시판 로딩 중 오류 발생:', error);
            });
        console.log("1:1 채팅 클릭");
    });

    $(document).on('click', '.voice-chat-btn', function() {
        if (tryCall) return; // 이미 시도 중이면 클릭 방지
        const id = $(this).attr("id");
        console.log("화상 채팅 클릭, ID:", id); // 디버깅 로그
        // 화상 채팅 버튼 클릭 이벤트
        fetch("/test/" + id)
            .then(response => response.text())
            .then(data => {
                screen.empty().html(data);
            })
            .catch(error => {
                console.error('게시판 로딩 중 오류 발생:', error);
            });
        tryCall = true;
        alarmSocket.send(JSON.stringify({
            type: 'voice',
            senderId: userId,
            receiverId: id,
            content: 'request'
        }));
        // 화상 채팅 버튼 상태 업데이트
        updateVoiceChatButtonState();
        console.log("화상 채팅 클릭");
        // 여기에 필요한 기능 추가
    });

    // 포인트 충전 새 창
    $(document).ready(function() {
        // 포인트 충전 버튼 클릭 시 결제 창 열기
        $('#charge').on('click', function() {
            const url = "/pay/pointCharge";

            // 브라우저의 화면 크기 가져오기
            const screenWidth = window.innerWidth;
            const screenHeight = window.innerHeight;

            // 팝업 창 크기 설정
            const popupWidth = 630;
            const popupHeight = 630;

            // 중앙 위치 계산
            const popupLeft = (screenWidth - popupWidth) / 2 + window.screenX;
            const popupTop = (screenHeight - popupHeight) / 2 + window.screenY;

            // 팝업 창 띄우기 (위치 설정 포함)
            window.open(url, "openPaymentWindow", `width=${popupWidth},height=${popupHeight},left=${popupLeft},top=${popupTop}`);
        });

});
