let localStream;
let remoteStream;
// 시그널링을 위한 웹소켓 서버 연결
const socket = new WebSocket("ws://localhost:8080/chat");
// ice 후보를 받아올 STUN서버 연결
const servers = {
    iceServers:[
        {
            urls:['stun:112.218.52.156:3478']
        }
    ]
}
peerConnection = new RTCPeerConnection(servers);
let init = async () => {
    localStream = await navigator.mediaDevices.getUserMedia({video:true, audio:true});
    $(".user-video").srcObject = localStream;
}

let createOffer = async () => {
    // 대화할 상대들에게 sdp와 ice후보를 전달해줄


    remoteStream = new MediaStream();

    let offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);

    console.log("offer : " + offer)
    socket.send(JSON.stringify({
        type: 'offer',
        sdp: peerConnection.localDescription
    }));
}
// ICE 후보가 생성될 때마다 호출
peerConnection.onicecandidate = (event) => {
    if (event.candidate) {
        // ICE 후보를 서버로 전송
        socket.send(JSON.stringify({
            type: 'candidate',
            candidate: event.candidate
        }));
    }
};

socket.onmessage = async (message) => {
    let data = JSON.parse(message.data);

    if (data.type == 'answer') {
        // 상대방의 SDP Answer 수신 후 설정
        await peerConnection.setRemoteDescription(new RTCSessionDescription(data.sdp));
        peerConnection.ontrack = (event) => {
            remoteStream.addTrack(event.track);
            $(".remote-video").srcObject = remoteStream;
        }
    } else if (data.type == 'candidate') {
        // 상대방의 ICE 후보를 추가
        await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
    } else if (data.type == 'offer') {
        await peerConnection.setRemoteDescription(new RTCSessionDescription(data.sdp));

        // Answer 생성
        let answer = await peerConnection.createAnswer();
        await peerConnection.setLocalDescription(answer);

        // Answer를 WebSocket을 통해 상대방에게 전송
        socket.send(JSON.stringify({
            type: 'answer',
            sdp: peerConnection.localDescription
        }));
    }
}

init ();