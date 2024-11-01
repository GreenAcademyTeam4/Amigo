document.addEventListener('DOMContentLoaded', function () {
    const paginationContainer = document.getElementById('pagination-container');
    const size = 4; // 페이지당 항목 수
    let currentPage = 1; // 초기 페이지를 1로 설정

    function renderPagination() {
        paginationContainer.innerHTML = ''; // 기존 버튼 초기화

        for (let i = 1; i <= totalPages; i++) {
            let button = document.createElement('button');
            button.className = 'page-btn';
            button.textContent = i;
            button.style.margin = '0 5px';
            button.disabled = i === currentPage; // 현재 페이지 버튼 비활성화

            button.addEventListener('click', () => {
                currentPage = i;
                loadPage(currentPage);
            });

            paginationContainer.appendChild(button);
        }
    }

    function loadPage(page) {
        const offset = (page - 1) * size;
        fetch(`/board/list?offset=${offset}&size=${size}`)
            .then(response => response.text())

            .then(html => {
                document.querySelector('.board-container').innerHTML = html;
                renderPagination(); // 페이지네이션 업데이트
            })
            .catch(error => console.error('Error loading page:', error));
    }

    // 초기 페이지네이션 렌더링
    renderPagination();
});

 // 폼 제출 시 페이지 리로드 없이 검색 수행
    document.getElementById("searchForm").addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 제출 방지

        // 폼 데이터 생성
        const formData = new FormData(this);
        const searchType = formData.get("searchType");
        const keyword = formData.get("keyword");

        // 검색 요청 보내기
        fetch(`/board/search?searchType=${searchType}&keyword=${keyword}`, {
            method: 'GET'
        })
        .then(response => response.text()) // 텍스트 형식으로 응답 받기
        .then(html => {
            // 결과 영역 업데이트
            document.getElementById("searchResults").innerHTML = html;
        })
        .catch(error => console.error('검색 요청 중 오류:', error));
    });

    // 닫기 버튼 클릭 시 원래 목록을 로드
    function loadList() {
        fetch('/board/list')
        .then(response => response.text())
        .then(html => {
            document.getElementById("searchResults").innerHTML = html;
        })
        .catch(error => console.error('목록 로딩 중 오류:', error));
    }