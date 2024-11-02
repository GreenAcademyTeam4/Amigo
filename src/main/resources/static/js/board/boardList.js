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

// 페이지네이션 버튼 생성 함수
function updatePagination(currentPage) {
    const paginationContainer = document.getElementById('pagination-container');
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i + 1;
        button.classList.add('page-button');
        console.log('버튼 생성');

        if (i === currentPage - 1) {
            button.classList.add('active');
        }

        button.addEventListener('click', () => fetchBoardPage(i + 1));
        paginationContainer.appendChild(button);
    }
}

// 초기 페이지네이션 설정
$(document).ready(function() {

    console.log('토탈 페이지 : ',totalPages);
    console.log("페이지네이션 설정 시작");
    console.log("Total Pages:", totalPages, "Initial Page:", initialPage);
    updatePagination(initialPage);
});


//document.addEventListener('DOMContentLoaded', function () {
//        // totalPages는 서버에서 Mustache로 전달된 변수
//        const paginationContainer = document.getElementById('pagination');
//        const urlParams = new URLSearchParams(window.location.search);
//        const currentPage = parseInt(urlParams.get('offset') / 4) + 1 || 1; // 현재 페이지 계산
//
//        for (let i = 1; i <= totalPages; i++) {
//            let li = document.createElement('li');
//            li.className = 'page-item';
//
//            // 현재 페이지를 활성화 상태로 표시
//            if (i === currentPage) {
//                li.classList.add('active');
//            }
//
//            let a = document.createElement('a');
//            a.className = 'page-link';
//            a.href = `?offset=${(i - 1) * 4}&size=4`;  // offset을 계산하여 링크를 만듭니다.
//            a.textContent = i;
//
//            // 페이지 버튼 클릭 이벤트
//            a.addEventListener('click', function (event) {
//                event.preventDefault();
//
//                // 모든 페이지 아이템에서 active 클래스 제거
//                const allPageItems = document.querySelectorAll('.page-item');
//                allPageItems.forEach(function (item) {
//                    item.classList.remove('active');
//                });
//
//                // 현재 클릭한 아이템에 active 클래스 추가
//                li.classList.add('active');
//
//                // 페이지를 이동
//                window.location.href = a.href;
//            });
//
//            li.appendChild(a);
//            paginationContainer.appendChild(li);
//        }
//    });