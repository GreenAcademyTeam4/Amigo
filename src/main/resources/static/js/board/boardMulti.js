

    // 좋아요 버튼 클릭 시 작동하는 스크립트
    function toggleLike() {
        const boardId = {{board.id}};
            const likeIcon = document.getElementById('like-icon');
            const likeCountSpan = document.getElementById('like-count');

            const isLiked = likeIcon.classList.contains('liked');
            const url = `/board/like/${boardId}`;
            const method = isLiked ? 'DELETE' : 'POST';

            fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // Update like count
                        likeCountSpan.textContent = data.likeCount;

                        if (isLiked) {
                            likeIcon.classList.remove('liked');
                            likeIcon.src = '/image/board/empty.png';
                        } else {
                            likeIcon.classList.add('liked');
                            likeIcon.src = '/image/board/full.png';
                        }
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }

        // 댓글 "수정" 버튼 클릭 시 작동하는 함수
        function showEditForm(commentId) {
            const editForm = document.getElementById(`edit-form-${commentId}`);
            const contentElement = document.getElementById(`content-${commentId}`);

            if (editForm && contentElement) {
                editForm.style.display = 'block';
                contentElement.style.display = 'none';
            } else {
                console.error("Edit form or content element not found for comment ID:", commentId);
            }
        }

        // 댓글 "수정 완료" 버튼 클릭 시 비동기 처리 함수
        function updateComment(commentId) {
            const updatedContent = document.getElementById(`edit-content-${commentId}`).value;

            fetch(`/board/reply/update/${commentId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ content: updatedContent })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('댓글이 성공적으로 수정되었습니다.');
                        document.getElementById(`content-${commentId}`).innerText = updatedContent;
                        document.getElementById(`edit-form-${commentId}`).style.display = 'none';
                        document.getElementById(`content-${commentId}`).style.display = 'block';
                    } else {
                        alert('댓글 수정에 실패했습니다.');
                    }
                });
        }

        // 댓글 삭제 아이콘 클릭 시 비동기 처리 함수
         function confirmDeleteComment(commentId) {
        // confirm 창으로 삭제 확인
        const userConfirmed = confirm("댓글을 삭제하시겠습니까?");
        if (userConfirmed) {
            deleteComment(commentId);  // 예를 누른 경우에만 삭제 함수 호출
        }
    }

    function deleteComment(commentId) {
        fetch(`/board/reply/delete/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('댓글이 성공적으로 삭제되었습니다.');
                    const commentElement = document.getElementById(`comment-${commentId}`);
                    if (commentElement) {
                        commentElement.remove();
                    }
                } else {
                    alert('댓글 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 오류로 댓글 삭제에 실패했습니다.');
            });
    }


// 서버에서 받은 Mustache 변수들을 JavaScript 변수로 할당
window.totalPages = {{totalPages}};
window.initialPage = {{currentPage}};

// 비동기 페이지 이동 함수
function fetchBoardPage(page) {
    fetch(`/board/detail/{{board.id}}?page=${page}&size=5`)
        .then(response => response.text())
        .then(html => {
            document.querySelector('.board-container').innerHTML = html;
            updatePagination(page);
        })
        .catch(error => console.error('페이지 로딩 중 오류 발생:', error));
}

// 페이지네이션 버튼 생성 함수
function updatePagination(currentPage) {
    console.log('함수 발동');
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
    console.log('페이지 네이션');
    updatePagination(initialPage);
});




(function() {
    let screen = $('.screen-area');
    // 폼 제출 함수
    function submitPost() {
        // 에디터 내용을 <textarea>와 동기화
        if (oEditors.length > 0 && oEditors[0]) {
            oEditors[0].exec("UPDATE_CONTENTS_FIELD", []);
        } else {
            console.error("에디터가 초기화되지 않았습니다.");
            return;
        }

        let content = $("#editorText").val();

        if (content.trim() === '') {
            alert("내용을 입력해주세요.");
            oEditors[0].exec("FOCUS");
            return;
        } else {
            console.log(content);
            // 폼을 제출
            screenCreate('/board/insert', 'boardForm');
        }
    }

    // 게시판에서 <a> 태그를 처리하는 함수
    function screenChanger(data) {
    console.log("화면 전환!~!");
        fetch(data)
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('게시판 로딩 중 오류 발생:', error);
            });
    }

    // 게시글 삭제 함수
    function screenDelete(data) {

        console.log("screenChange!!!");
        // 삭제 확인 창
                const userConfirmed = confirm("게시글을 정말로 삭제하시겠습니까?");

        if(userConfirmed) { fetch(data, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.text())
        .then(data => {
            screen.html(data);
        })
        .catch(error => {
            console.error('게시판 삭제 중 오류 발생:', error);
        });
    }
   }

    // 폼 데이터를 전송하는 함수
    function screenCreate(url, formElementId) {
        console.log("screenCreate!!!");

        // formElementId를 사용하여 폼 요소를 가져옵니다.
        const formElement = document.getElementById(formElementId);

        // FormData 객체를 생성하여 폼 데이터를 추가합니다.
        const formData = new FormData(formElement);

        // fetch를 사용하여 multipart/form-data로 전송합니다.
        fetch(url, {
            method: 'POST',
            body: formData // FormData 객체를 요청 본문으로 설정합니다.
        })
        .then(response => response.text())
        .then(data => {
            // 응답 데이터를 화면에 표시합니다.
            screen.html(data);
        })
        .catch(error => {
            console.error('요청 중 오류 발생:', error);
        });
    }

    // 댓글 등록 함수
    function submitComment(event, boardId) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        // 댓글 내용 가져오기
        const commentContent = document.getElementById("commentContent").value;
        console.log("댓글 전송 내용 : " + commentContent)

        if (!commentContent.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        // FormData 객체 생성 및 데이터 추가
        const formData = new FormData();
        formData.append('boardId', boardId);
        formData.append('content', commentContent);

        // fetch를 사용해 FormData 전송
        fetch('/board/comment', {
            method: 'POST',
            body: formData // FormData 객체를 전송
        })
        .then(response => {
            if (response.ok) {
                addCommentToPage(commentContent); // 페이지에 새 댓글 추가
                document.getElementById('commentContent').value = ''; // 입력 필드 초기화
            } else {
                throw new Error('댓글 등록에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('게시판 댓글 오류 발생:', error);
        });
    }

    // 페이지에 새 댓글을 추가하는 함수
    function addCommentToPage(content) {
        const commentSection = document.querySelector('.comments');

        const newComment = document.createElement('div');
        newComment.className = 'comment';
        newComment.innerHTML = `
            <div class="comment-header">
                <p><strong>작성자:</strong> 나</p>
                <p><small>작성일자: 지금</small></p>
            </div>
            <div class="comment-body">
                <p>${content}</p>
            </div>
            <div class="comment-actions">
                <button onclick="confirmDeleteComment(-1)" class="btn btn-danger btn-sm">🗑</button>
                <button onclick="showEditForm(-1)" class="btn btn-link btn-sm">수정</button>
            </div>
            <hr>
        `;

        commentSection.prepend(newComment); // 새 댓글을 최상단에 추가
    }

    // 폼 수정 제출 함수
    function modifyPost(url, formElementId) {
        console.log("screenUpdate!!!");

        // formElementId를 사용하여 폼 요소를 가져옵니다.
        const formElement = document.getElementById(formElementId);

        // FormData 객체를 생성하여 폼 데이터를 추가합니다.
        const formData = new FormData(formElement);

        // fetch를 사용하여 multipart/form-data로 전송합니다.
        fetch(url, {
            method: 'POST',
            body: formData // FormData 객체를 요청 본문으로 설정합니다.
        })
        .then(response => response.text())
        .then(data => {
            // 응답 데이터를 화면에 표시합니다.
            screen.html(data);
        })
        .catch(error => {
            console.error('요청 중 오류 발생:', error);
        });
    }







    // 함수를 전역으로 노출
    window.submitPost = submitPost;
    window.screenChanger = screenChanger;
    window.screenDelete = screenDelete;
    window.screenCreate = screenCreate;
    window.submitComment = submitComment;
    window.modifyPost = modifyPost;




})();