
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
        fetch("http://localhost:8080/main/changeSchool?id=" + schoolId)
            .then(data => {screen.innerHTML = data;});
    });
});
// 로그아웃 이벤트 추가 (로그인 화면으로)
logout.on('click',location.href="http://localhost:8080/");
// 학교로 들어가는 이벤트 추가
enter.on('click',fetch("http://localhost:8080/main/school")
    .then(data => {
        screen.innerHTML = data;
    }));
// 게시판으로 들어가는 이벤트 추가
post.on('click',fetch("http://localhost:8080/main/post")
    .then(data => {
        screen.innerHTML = data;
    }));
// 처음에는 게시판을 띄우도록 설정
fetch("http://localhost:8080/main/post").then(data => {
    screen.innerHTML = data;
});
