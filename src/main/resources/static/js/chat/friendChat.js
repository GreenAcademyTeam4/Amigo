// HTML 문서가 완전히 로드된 후에 실행
$(document).ready(function() {

    // 기존 WebSocket 연결이 있을 경우 닫기
    if (window.socket) {
        window.socket.close();
    }
    // 새로운 WebSocket 인스턴스를 생성하고 서버에 연결
    window.socket = new WebSocket("ws://localhost:8080/friendChat");

    // WebSocket 연결이 성공했을 때 실행
    // 연결이 성공하면 서버로 roomKey 정보를 JSON 형식으로 전송해 사용자가 접속한 방의 ID를 서버에 알림
    window.socket.onopen = function () {
        loadChatLogs(); // 기존 채팅 로그 로드
        window.socket.send(JSON.stringify({
            type: 'roomKey',
            message: roomId // 글로벌 roomId 사용
        }));
    };

    // 서버에서 메시지를 수신할 때마다 실행
    // 서버로부터 메시지가 도착하면 onmessage 이벤트가 발생
    window.socket.onmessage = function (event) {
        // 수신된 메시지를 JSON 형식으로 파싱하고, 각 메시지 타입(date, chat, emoticon)에 따라 다르게 처리
        const messageData = JSON.parse(event.data);

        // "chat-area" 요소를 선택 (메시지가 추가될 컨테이너)
        const chatContainer = document.getElementById("chat-area");

        // 날짜 메시지일 경우 날짜 요소를 만들어서 chatContainer에 추가하여 화면에 표시
        if (messageData.type === "date") {
            // 날짜와 선을 함께 추가
            const dateWrapper = document.createElement("div");
            dateWrapper.className = "chat-date-wrapper";

            const dateElement = document.createElement("div");
            dateElement.className = "chat-date";
            dateElement.textContent = messageData.date; // 날짜

            dateWrapper.appendChild(dateElement);
            chatContainer.appendChild(dateWrapper);
        }

        // 일반 메시지일 경우 메시지 추가
        if (messageData.type === "chat") {
            // 일반 메시지 처리
            const messageWrapper = document.createElement("div");
            messageWrapper.className = "message-wrapper";

            const messageElement = document.createElement("div");
            messageElement.className = "chat-message";
            messageElement.textContent = messageData.message; // 메시지

            // 메시지를 sent(내 메시지) 또는 received(상대 메시지)로 구분하여 표시
            // 메시지 방향 설정 (내 메시지는 오른쪽, 상대 메시지는 왼쪽)
            if (messageData.sender === 'self') {
                messageElement.classList.add('sent');
            } else {
                messageElement.classList.add('received');
            }

            // 타임스탬프 요소 생성
            const timestampElement = document.createElement("div");
            timestampElement.className = "timestamp";
            timestampElement.textContent = formatTimestamp(messageData.date); // 포맷된 시간(시, 분)

            // 메시지와 타임스탬프 위치 설정
            messageWrapper.appendChild(messageElement);
            messageWrapper.appendChild(timestampElement);
            chatContainer.appendChild(messageWrapper);

        } else if(messageData.type === 'emoticon'){
            const messageWrapper = document.createElement("div");
            messageWrapper.className = "message-wrapper";

            const messageElement = document.createElement("img");
            messageElement.className = "chat-emoticon";
            messageElement.src = messageData.message; // 이모티콘

            // 메시지 방향 설정 (내 메시지는 오른쪽, 상대 메시지는 왼쪽)
            if (messageData.sender === 'self') {
                messageElement.classList.add('sent');
            } else {
                messageElement.classList.add('received');
            }

            // 타임스탬프 요소 생성
            const timestampElement = document.createElement("div");
            timestampElement.className = "timestamp";
            timestampElement.textContent = formatTimestamp(messageData.date); // 포맷된 시간(시, 분)

            // 메시지와 타임스탬프 위치 설정
            messageWrapper.appendChild(messageElement);
            messageWrapper.appendChild(timestampElement);
            chatContainer.appendChild(messageWrapper);
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
        const messageContent = $('#content').val(); // 메시지 내용
        if (messageContent.trim() === "") return; // 빈 메시지 방지

        // JSON 형식의 메시지
        const messageJSON = JSON.stringify({
            type: 'chat',
            message: messageContent,
            sender: 'self', // 보낸 사람을 'self'로 설정
            date: Date.now()// 현재 날짜
        });
        window.socket.send(messageJSON); // 서버에 메시지 전송
        $('#content').val(''); // 입력창 비우기
    }

    // 타임스탬프(12시간제) 포맷 함수
    function formatTimestamp(dateString) {
        const date = new Date(dateString);
        const hours = date.getHours(); // 시
        const minutes = date.getMinutes(); // 분
        const ampm = hours >= 12 ? 'PM' : 'AM'; // AM인지 PM인지
        const formattedHours = hours % 12 || 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        return `${formattedHours}:${formattedMinutes} ${ampm}`; // 시:분 AM or PM
    }

    // 채팅 로그를 불러오는 함수
    function loadChatLogs() {
        // 서버에서 roomId에 해당하는 채팅 기록을 가져와 화면에 표시
        fetch(`/chat/logs/${roomId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => response.json())
            .then(chatLogs => {
                chatLogs.forEach(log => {
                    // 가져온 메시지를 하나씩 displayMessage() 함수로 처리하여 채팅 영역에 표시
                    displayMessage(log);
                });
                // 채팅 영역 스크롤을 맨 아래로
                document.getElementById('chat-area').scrollTop = document.getElementById('chat-area').scrollHeight;
            })
            .catch(error => {
                console.error("대화 내용을 불러오는 데 실패하였습니다. ", error);
            });
    }

    // 화면에 메시지를 표시하는 함수
    // 채팅 기록을 log 객체로 받아와 chat-area에 추가
    function displayMessage(log) {
        const chatContainer = document.getElementById("chat-area");
        
        if(log.type === 'chat'){
        // 메시지 래퍼
        const messageWrapper = document.createElement("div");
        messageWrapper.className = "message-wrapper";

        // 메시지 컨테이너
        const messageElement = document.createElement("div");
        messageElement.className = "chat-message";
        messageElement.textContent = log.message; // 메시지 내용

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
            // 메시지 래퍼
            const messageWrapper = document.createElement("div");
            messageWrapper.className = "message-wrapper";

            // 메시지 컨테이너
            const messageElement = document.createElement("img");
            messageElement.className = "chat-emoticon";
            messageElement.src = log.message;

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

        } else if(log.type === "dateLog"){
            let logDate = new Date(log.createdAt);
            // 한국어 형식 변환하여 연도, 월, 일을 각각 숫자로 지정하며 두 자리로 표시하도록 요청
            let formattedDate = logDate.toLocaleDateString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
                // 한국어 날짜 형식에서 날짜 구분 기호를 "-"로 변환하고, 불필요한 마침표(.)를 제거
            }).replace(/. /g, '-').replace('.', '');  // "YYYY-MM-DD" 형식으로 변환
            // replace(/. /g, '-'): 마침표(.)와 공백을 찾아 하이픈(-)으로 변환
            // replace('.', ''): 위에서 처리되지 않은 마지막 마침표를 제거

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

    // 키보드 영역 외부를 클릭 시 이모티콘 박스 닫기
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.keyboard-area').length) {
            $('.emoticon-box').removeClass('show');
        }
    });

    // 이모티콘 클릭 시 이모티콘 박스 닫기
    $('.emoticon-box').on('click', '.emoticon', function() {
        $('.emoticon-box').removeClass('show');
    });
});

// 이모티콘 창 띄우기(이모티콘 박스 열고 닫는 함수)
// show 클래스를 토글하여 이모티콘 박스를 표시
function openEmoticonBox() {
    $('.emoticon-box').toggleClass('show');
}

// 이모티콘을 서버로 전송하는 함수
function sendEmoticon(data) {
    socket.send(JSON.stringify({
        type: 'emoticon',
        message: data,
        sender: "self",
        date: Date.now()
    }));
}
