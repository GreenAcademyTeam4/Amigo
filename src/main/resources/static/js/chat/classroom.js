let socket = new WebSocket("ws://localhost:8080/chat");

function send() {
    const message = $('#message').val();
    socket.send(message);
}

// 실시간, 통화중 상태(메시지 입력 시 event 인식해서 함수 실행)
socket.onmessage = function (event){
    const message = event.data; //  서버로부터 받은 데이터(input 태그에서 받은 data)
    console.log(message);
    $('#message').val("");
    $('.server').text(message);
}