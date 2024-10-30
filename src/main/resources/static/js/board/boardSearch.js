  document.addEventListener('DOMContentLoaded', function () {
    const totalPages = {{totalPages}};
    const paginationContainer = document.getElementById('pagination');
    const urlParams = new URLSearchParams(window.location.search);
    const currentPage = parseInt(urlParams.get('page')) || 0; // 현재 페이지를 계산
    const keyword = encodeURIComponent('{{keyword}}');
    const searchType = encodeURIComponent('{{searchType}}');

    for (let i = 0; i < totalPages; i++) {
        let li = document.createElement('li');
        li.className = 'page-item';

        // 현재 페이지를 활성화 상태로 표시
        if (i === currentPage) {
            li.classList.add('active');
        }

        let a = document.createElement('a');
        a.className = 'page-link';
        a.href = `?page=${i}&size=4&keyword=${keyword}&searchType=${searchType}`;
        a.textContent = i + 1;

        li.appendChild(a);
        paginationContainer.appendChild(li);
    }
});