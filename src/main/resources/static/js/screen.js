$(document).ready(function() {
    const enter = $('.enter');
    const post = $('#post');
    const screen = $('#screen-area');
    const mypage = $('#my-page');
    const school = $('.other-school');
    const friends = $('.friends');

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
                console.error('게시판 로딩 중 오류 발생:', error);
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