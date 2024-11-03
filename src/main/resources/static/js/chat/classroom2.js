const socket = new WebSocket("ws://192.168.219.110:8080/chat");
const messageTimers = {}; // 각 좌석별 타이머를 저장할 객체
let currentSeatNum = null;
// 10개의 좌석 생성
for (let i = 0; i < 10; i++) {
    const seat = $('<div>').addClass('user-seat').attr('id', 'user-seat-' + i);

    // 닉네임과 아바타 이미지 요소 생성
    const nickname = $('<div>').addClass('user-nickname').text("");
    const img = $('<img>').addClass('user-avatar');
    const bubble = $('<div>').addClass('user-bubble').text(""); // 초기 말풍선은 빈 텍스트

    // 좌석 클릭 시 자리 변경 이벤트 추가
    seat.on('click', () => changeSeat(i)); // 클릭 시 changeSeat 함수 호출

    // 닉네임과 이미지 추가
    seat.append(bubble);
    seat.append(nickname);
    seat.append(img);
    $('.background').append(seat);
}
// 엔터키 감지 함수
function handleKeyPress(event) {
    if (event.key === "Enter") { // 엔터키를 감지
        send(); // 엔터키가 눌리면 send() 함수 호출
    }
}

// `chat-input`에 포커스가 있을 때만 `Enter` 키 감지
const chatInput = $('.chat-input');
chatInput.on('focus', () => {
    chatInput.on('keydown', handleKeyPress); // 포커스될 때 keydown 이벤트 추가
});
chatInput.on('blur', () => {
    chatInput.off('keydown', handleKeyPress); // 포커스가 해제될 때 keydown 이벤트 제거
});

socket.onmessage = (event) => {
    let message = JSON.parse(event.data);

    if (message.type === 'seat') {
        const data = JSON.parse(message.message);
        console.log(data);
        for (let i = 0; i < data.length; i++) {
            if(data[i] !== null) {
            if (data[i].id === userId) {
                console.log(currentSeatNum);
                if(currentSeatNum !== null) {
                    const bubble = $('#user-seat-' + currentSeatNum + ' .user-bubble');
                    bubble.css('opacity',0);
                }
                $('#user-seat-' + i + ' .user-nickname').text(data[i].nickname);
                currentSeatNum = i;
            } else {
                $('#user-seat-' + i + ' .user-nickname').text(data[i].nickname);
            }
            } else {
                $('#user-seat-' + i + ' .user-nickname').text('');
            }
        }
    } else if(message.type === 'chat') {
        // 말풍선에 새로운 메시지 설정
        const data = JSON.parse(message.message);
        const bubble = $('#user-seat-' + data.id + ' .user-bubble');
        bubble.text(data.message).css('opacity', 1);

        // 기존 타이머가 있으면 취소
        if (messageTimers[data.id]) {
            clearTimeout(messageTimers[data.id]);
        }

        // 새 타이머 설정하여 3초 후 말풍선 숨김
        messageTimers[data.id] = setTimeout(() => {
            bubble.css('opacity', 0);
        }, 3000);

    } else if (message.type === 'error') {
        console.log(message.message);
        alert(message.message);
    } else if (message.type === 'emoticon') {
        console.log(message.message);
        const data = JSON.parse(message.message);
        const bubble = $('#user-seat-' + data.id + ' .user-bubble');
        // 이모티콘 이미지 설정
        bubble.html(`<img src="${data.message}" alt="emoticon" class="bubble-emoticon">`);
        bubble.css('opacity', 1);

        // 기존 타이머가 있으면 취소
        if (messageTimers[data.id]) {
            clearTimeout(messageTimers[data.id]);
        }

        // 새 타이머 설정하여 3초 후 말풍선 숨김
        messageTimers[data.id] = setTimeout(() => {
            bubble.css('opacity', 0);
        }, 3000);
    }
}

function changeSeat(num) {
    let message = JSON.stringify({
        type: 'seat',
        message: num
    });
    console.log(num);
    socket.send(message);
}

function send() {
    let chatInput = $('.chat-input');
    let chat = chatInput.val();
    console.log(chat);
    if(chat !== ''){
        let message = JSON.stringify({
            type: 'chat',
            message: chat
        });
        chatInput.val('');
        socket.send(message);
    }
}

socket.onclose = (event) => {
    console.log("WebSocket closed:", event);
};

socket.onerror = (error) => {
    console.error("WebSocket error:", error);
};

// 이모티콘 창 토글 함수
function toggleEmoticonBox() {
    $('.emoticon-box').toggle(); // 이모티콘 박스의 가시성을 토글
}

// 이모티콘 추가 함수
function sendEmoticon(url) {
    const message = JSON.stringify({
        type: 'emoticon',
        message: url
    });
    socket.send(message);
    $('.emoticon-box').hide(); // 이모티콘 창 숨김
}
