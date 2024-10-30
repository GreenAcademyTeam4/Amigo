// 작동 안됨
 document.addEventListener('DOMContentLoaded', function () {
        // totalPages는 서버에서 Mustache로 전달된 변수
        const totalPages = {{totalPages}};
        const paginationContainer = document.getElementById('pagination');
        const urlParams = new URLSearchParams(window.location.search);
        const size = parseInt(urlParams.get('size')) || 4;
        const currentPage = parseInt(urlParams.get('offset') / size) + 1 || 1; // 현재 페이지 계산

        for (let i = 1; i <= totalPages; i++) {
            let li = document.createElement('li');
            li.className = 'page-item';

            // 현재 페이지를 활성화 상태로 표시
            if (i === currentPage) {
                li.classList.add('active');
            }

            let a = document.createElement('a');
            a.className = 'page-link';
            a.href = `?offset=${(i - 1) * size}&size=${size}`;  // offset을 계산하여 링크를 만듭니다.
            a.textContent = i;

            // 페이지 버튼 클릭 이벤트
            a.addEventListener('click', function (event) {
                event.preventDefault();

                // 모든 페이지 아이템에서 active 클래스 제거
                const allPageItems = document.querySelectorAll('.page-item');
                allPageItems.forEach(function (item) {
                    item.classList.remove('active');
                });

                // 현재 클릭한 아이템에 active 클래스 추가
                li.classList.add('active');

                // 페이지를 이동
                window.location.href = a.href;
            });

            li.appendChild(a);
            paginationContainer.appendChild(li);
        }

    });







