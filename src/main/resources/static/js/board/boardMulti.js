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