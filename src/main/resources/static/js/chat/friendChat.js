// const socket = new WebSocket("ws://localhost:8080/friendChat");
//
// function send() {
//     // 입력한 메세지 내용 받아옴
//     const message = $('#content').val();
//     const messageJSON = JSON.stringify({
//         type: 'chat',
//         message: message
//     });
//     socket.send(messageJSON); // 메세지를 JSON 문자열로 변환 후 서버에 보냄
//     $('#content').val(''); // 채팅 입력한 후 비워줌
// }
//
// // 서버에서 메시지를 수신할 때마다 발생하는 이벤트
//     socket.onmessage = (event) => {
//         const message = JSON.parse(event.data); //JSON 문자열을 JS 객체로 변환
//         //event.data : 서버에서 받은 메시지 데이터(JSON 형식)
//
//         // 메시지 타입이 "chat"일 때만 처리
//         if (message.type === 'chat') {
//             // 새로운 div 요소 생성
//             const messageDiv = $('<div></div>')
//                 .addClass('chat-message') // 클래스 추가
//                 .text(message.message); // 메시지 내용 설정
//
//             // chat-area에 새로운 메시지 추가
//             $('.chat-area').append(messageDiv);
//         }
//     };

$(document).ready(function() {
    const socket = new WebSocket("ws://localhost:8080/friendChat");
    let currentDate = "{{currentDate}}";


    // WebSocket 연결이 성공했을 때 실행
    socket.onopen = function() {
        // JSON 형식으로 방번호를 알림
        // socket.send : 메시지 처리하는 기능
        socket.send(JSON.stringify({
            type: 'roomKey',
            message: roomId
        }));
        console.log("Connected to WebSocket server.");
        loadChatLogs(); // 페이지가 로드되면 기존 채팅 로그를 불러옵니다.
    };

    // 서버에서 메시지를 수신할 때마다 실행
    socket.onmessage = function(event) {
        const message = JSON.parse(event.data); // JSON 문자열을 JS 객체로 변환
        displayMessage(message);
    };

    // 전송 버튼 클릭 이벤트 처리
    $('#sendButton').click(function() {
        send();
    });

    // Enter 키로 메시지 전송
    $('#content').keypress(function(e) {
        if (e.which === 13) {
            send();
        }
    });

    // 메시지를 서버로 전송하는 함수
    function send() {
        const messageContent = $('#content').val();
        if (messageContent.trim() === "") return; // 빈 메시지 방지

        const messageJSON = JSON.stringify({
            type: 'chat',
            message: messageContent,
            date: currentDate // 메시지에 현재 날짜 포함
        });

        socket.send(messageJSON); // 서버에 메시지 전송
        $('#content').val(''); // 입력창 비우기
    }

    // 서버에서 받은 메시지를 화면에 표시하는 함수
    function displayMessage(message) {
        // 채팅 메시지를 수신한 경우
        if (message.type === 'chat') {
            // 메시지를 표시할 div 생성
            const messageDiv = $('<div></div>').addClass('chat-message');

            // 날짜가 함께 포함된 메시지 표시
            const dateDiv = $('<div></div>').addClass('message-date');
            dateDiv.text(message.date || ''); // 날짜가 존재하면 표시

            // 메시지 텍스트 표시
            const textDiv = $('<div></div>').addClass('message-text');
            textDiv.text(message.message);

            // 메시지와 날짜를 chat-area에 추가
            $('#chat-area').append(dateDiv).append(messageDiv.append(textDiv));
        }
    }

    // 채팅 로그를 불러오는 함수
    function loadChatLogs() {
        const roomId = getRoomId(); // URL이나 다른 방법으로 roomId를 가져옵니다.
        $.ajax({
            url: `/chat/logs/${roomId}`,
            method: 'GET',
            success: function(logs) {
                logs.forEach(log => {
                    const messageDiv = $('<div></div>').addClass('chat-message');
                    messageDiv.text(`${log.nickName}: ${log.message}`);
                    $('#chat-area').append(messageDiv);
                });
            },
            error: function(error) {
                console.error("Failed to load chat logs:", error);
            }
        });
    }

    // 채팅방 ID를 가져오는 함수 (예시로 URL에서 추출)
    function getRoomId() {
        // 예를 들어, URL이 /chat/room/1 형태일 때
        return window.location.pathname.split('/').pop();
    }
}


