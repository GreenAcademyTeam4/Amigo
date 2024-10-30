$(document).ready(function() {
    const enter = $('.enter');
    const post = $('.post');
    const screen = $('.screen-area');
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

    mypage.on('click', function() {
        fetch("/my-page/")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('마이 페이지 로딩 중 오류 발생:', error);
            });
    });
});
