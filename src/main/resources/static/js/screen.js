$(document).ready(function () {
    const enter = $('#enter');
    const post = $('#post');
    const screen = $('#screen-area');
    const school = $('#other-school');

    // Mustache 템플릿 가져오기
    const template = $('#template').html();

    // 학교 바꾸기 기능 추가
    school.each(function (index, element) {
        $(element).on('click', function () {
            const schoolId = $(this).data('id');
            fetch("http://localhost:8080/main/changeSchool?id=" + schoolId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(data => {
                    const rendered = Mustache.render(template, { content: data });
                    screen.html(rendered);
                })
                .catch(error => console.error('Error:', error));
        });
    });

    // 학교로 들어가는 이벤트 추가
    enter.on('click', function () {
        fetch("http://localhost:8080/main/school")
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                const rendered = Mustache.render(template, { content: data });
                screen.html(rendered);
            })
            .catch(error => console.error('Error:', error));
    });

    // 게시판으로 들어가는 이벤트 추가
    post.on('click', function () {
        fetch("/board/list")
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                const rendered = Mustache.render(template, { content: data });
                screen.html(rendered);
            })
            .catch(error => console.error('Error:', error));
    });

    // 처음에는 게시판을 띄우도록 설정
    fetch("/board/list")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            const rendered = Mustache.render(template, { content: data });
            screen.html(rendered);
        })
        .catch(error => console.error('Error:', error));
});