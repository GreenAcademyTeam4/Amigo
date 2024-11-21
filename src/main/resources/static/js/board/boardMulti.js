(function () {
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
        console.log("화면 전환");
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
        console.log("screenDelete 호출");
        const userConfirmed = confirm("게시글을 정말로 삭제하시겠습니까?");

        if (userConfirmed) {
            fetch(data, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
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
        console.log("screenCreate 호출");

        const formElement = document.getElementById(formElementId);
        const formData = new FormData(formElement);

        fetch(url, {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('요청 중 오류 발생:', error);
            });
    }

    // 댓글 등록 함수
    function submitComment(event, boardId) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        const commentContent = document.getElementById("commentContent").value;
        console.log("댓글 전송 내용:", commentContent);

        if (!commentContent.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('boardId', boardId);
        formData.append('content', commentContent);

        fetch('/board/comment', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(data => {
                screen.html(data);
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
        console.log("screenUpdate 호출");

        const formElement = document.getElementById(formElementId);
        const formData = new FormData(formElement);

        fetch(url, {
            method: 'POST',
            body: formData,
        })
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('요청 중 오류 발생:', error);
            });
    }

    // 전역 함수로 노출
    window.submitPost = submitPost;
    window.screenChanger = screenChanger;
    window.screenDelete = screenDelete;
    window.screenCreate = screenCreate;
    window.submitComment = submitComment;
    window.modifyPost = modifyPost;
})();
