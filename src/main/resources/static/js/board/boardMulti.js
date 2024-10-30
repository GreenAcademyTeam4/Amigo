const screen = $('#screen-area');

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


// 게시판에서 <a> 태그들은 모두 이거 사용할 거 같음
function screenChanger(data) {
fetch(data)
     .then(response => response.text())
             .then(data => {
                 screen.html(data);
            })
             .catch(error => {
                 console.error('게시판 로딩 중 오류 발생:', error);
            });
}

function screenDelete(data) {
    console.log("screenChange!!!");
    fetch(data, {
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