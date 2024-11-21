console.log("하트가 많은 게시글 입장 !!!!");
$(document).ready(function() {
    $('#searchButton').on('click', function(event) {
        console.log("일단 검색 보냄!!!!")
        event.preventDefault(); // 폼 기본 동작 막기

        const searchType = $('#searchType').val();
        const keyword = $('#keyword').val();

        if (!searchType || !keyword) {
            alert("검색 조건과 검색어를 모두 입력해주세요.");
            return;
        }

        $.ajax({
            url: '/board/search',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ searchType: searchType, keyword: keyword }),
            success: function(data) {
                screen.html(data); // 응답 데이터를 화면에 표시
            },
            error: function(error) {
                console.error("Error submitting search:", error);
            }
        });
    });
});

// 비동기 페이지 이동 함수
function fetchBoardPage(page) {
    console.log("@@@@@@@@@@@@@@@@@@@")
    fetch(`/board/list?page=${page}&size=4`)
        .then(response => response.text())
        .then(html => {
            document.querySelector('.board-container').innerHTML = html;
            updatePagination(page);
        })
        .catch(error => console.error('페이지 로딩 중 오류 발생:', error));
}
