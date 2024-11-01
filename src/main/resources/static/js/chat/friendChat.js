$(document).ready(function() {

    // 기존 WebSocket 연결이 있을 경우 닫기
    if (window.socket) {
        window.socket.close();
    }
    // 새로운 WebSocket 연결 생성
    window.socket = new WebSocket("ws://localhost:8080/friendChat");

    // 디버깅: roomId 확인
    console.log("roomId (JavaScript):", roomId); // 디버깅 로그 추가

    // WebSocket 연결이 성공했을 때 실행
    window.socket.onopen = function () {
        loadChatLogs();
        window.socket.send(JSON.stringify({
            type: 'roomKey',
            message: roomId // 글로벌 roomId 사용
        }));
        console.log("Connected to WebSocket server with roomId:", roomId);
    };

    // 서버에서 메시지를 수신할 때마다 실행
    window.socket.onmessage = function (event) {
        const messageData = JSON.parse(event.data); // JSON 문자열을 JS 객체로 변환
        console.log(messageData);


        // "chat-area" 요소를 선택 (메시지가 추가될 컨테이너)
        const chatContainer = document.getElementById("chat-area");

        // 날짜 메시지일 경우 날짜와 선을 추가
        if (messageData.type === "date") {
            // 날짜와 선을 함께 추가
            const dateWrapper = document.createElement("div");
            dateWrapper.className = "chat-date-wrapper";

            const dateElement = document.createElement("div");
            dateElement.className = "chat-date";
            dateElement.textContent = messageData.date;

            dateWrapper.appendChild(dateElement);
            chatContainer.appendChild(dateWrapper);
            console.log("날짜 메시지:", messageData.date);
        }

        // 일반 메시지일 경우 메시지 추가
        if (messageData.type === "chat") {
            // 일반 메시지 처리
            const messageWrapper = document.createElement("div");
            messageWrapper.className = "message-wrapper";

            const messageElement = document.createElement("div");
            messageElement.className = "chat-message";
            messageElement.textContent = messageData.message;

            // 메시지 방향 설정 (내 메시지는 오른쪽, 상대 메시지는 왼쪽)
            if (messageData.sender === 'self') {
                console.log("내가 보낸 메세지");
                messageElement.classList.add('sent');
            } else {
                messageElement.classList.add('received');
            }

            // 타임스탬프 요소 생성
            const timestampElement = document.createElement("div");
            timestampElement.className = "timestamp";
            timestampElement.textContent = formatTimestamp(messageData.date);
            // 메시지와 타임스탬프 위치 설정
            messageWrapper.appendChild(messageElement);
            messageWrapper.appendChild(timestampElement);
            chatContainer.appendChild(messageWrapper);
            console.log("일반 메시지:", messageData.message);
        }

    };

    // 전송 버튼 클릭 이벤트 처리
    $('#sendButton').off('click').on('click', function () { // 기존 이벤트 핸들러 제거 후 새로 등록
        send();
    });

    // Enter 키로 메시지 전송
    $('#content').off('keypress').on('keypress', function (e) { // 기존 이벤트 핸들러 제거 후 새로 등록
        if (e.which === 13) {
            e.preventDefault(); // 기본 동작 방지
            send();
        }
    });

    // 메시지를 서버로 전송하는 함수
    function send() {
        console.log("Send function called");
        const messageContent = $('#content').val();
        if (messageContent.trim() === "") return; // 빈 메시지 방지

        const messageJSON = JSON.stringify({
            type: 'chat',
            message: messageContent,
            sender: 'self', // 보낸 사람을 'self'로 설정
            date: Date.now()// 현재 날짜 포함
        });

        window.socket.send(messageJSON); // 서버에 메시지 전송
        $('#content').val(''); // 입력창 비우기
    }

    // 타임스탬프 포맷 함수
    function formatTimestamp(dateString) {
        const date = new Date(dateString);
        const hours = date.getHours();
        const minutes = date.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = hours % 12 || 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        return `${formattedHours}:${formattedMinutes} ${ampm}`;
    }

    // 채팅 로그를 불러오는 함수
    function loadChatLogs() {
        console.log("로그 불러오는 중")
        fetch(`/chat/logs/${roomId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => response.json())
            .then(chatLogs => {
                chatLogs.forEach(log => {
                    displayMessage(log);
                });
                console.log("로그 불러오기 성공", chatLogs);
                // 채팅 영역 스크롤을 맨 아래로
                document.getElementById('chat-area').scrollTop = document.getElementById('chat-area').scrollHeight;
            })
            .catch(error => {
                console.error("Failed to load chat logs:", error);
            });
    }

// 화면에 메시지를 표시하는 함수
    function displayMessage(log) {
        const chatContainer = document.getElementById("chat-area");
        if(log.type === 'chat'){

        // 메시지 래퍼
        const messageWrapper = document.createElement("div");
        messageWrapper.className = "message-wrapper";

        // 메시지 컨테이너
        const messageElement = document.createElement("div");
        messageElement.className = "chat-message";
        messageElement.textContent = log.message;

        // 메시지 방향 설정 (currentUserId와 log.userId를 비교)
        if (log.userId === currentUserId) {
            messageElement.classList.add("sent");  // 내가 보낸 메시지
        } else {
            messageElement.classList.add("received");  // 상대방이 보낸 메시지
        }

        // 타임스탬프 표시
        let logTimeStamp = new Date(log.createdAt);
        const timestampElement = document.createElement("div");
        timestampElement.className = "timestamp";
        timestampElement.textContent = formatTimestamp(logTimeStamp.toISOString());

        // 메시지와 타임스탬프를 감싸는 구조
        messageWrapper.appendChild(messageElement);
        messageWrapper.appendChild(timestampElement);

        // 채팅 영역에 추가
        chatContainer.appendChild(messageWrapper);
        } else if(log.type === 'emoticon'){

        } else if(log.type === "dateLog"){
            console.log(log.createdAt);
            let logDate = new Date(log.createdAt);
            let formattedDate = logDate.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            }).replace(/. /g, '-').replace('.', '');  // "YYYY-MM-DD" 형식으로 변환
            // 날짜와 선을 함께 추가
            const dateWrapper = document.createElement("div");
            dateWrapper.className = "chat-date-wrapper";

            const dateElement = document.createElement("div");
            dateElement.className = "chat-date";
            dateElement.textContent = formattedDate;


            dateWrapper.appendChild(dateElement);
            chatContainer.appendChild(dateWrapper);
        }
    }


});
