let localStream;
let remoteStream = new MediaStream();  // 초기화
const socket = new WebSocket("ws://localhost:8080/signaling");

const servers = {
    iceServers: [
        { urls: ['stun:192.168.0.129:3478'] }  // STUN 서버 설정
    ]
};

const peerConnection = new RTCPeerConnection(servers);

// 로컬 미디어 스트림을 가져와서 화면에 표시하고, peerConnection에 추가
let init = async () => {
    localStream = await navigator.mediaDevices.getUserMedia({video: true, audio: true});
    $(".user-video").srcObject = localStream;

    localStream.getTracks().forEach(track => {
        peerConnection.addTrack(track, localStream);
    });
};

// Offer 생성 및 전송
let createOffer = async () => {
    let offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);

    console.log("Offer:", offer);
    socket.send(JSON.stringify({
        type: 'offer',
        sdp: peerConnection.localDescription
    }));
};

// ICE 후보가 생성될 때마다 WebSocket을 통해 서버로 전송
peerConnection.onicecandidate = (event) => {
    if (event.candidate) {
        socket.send(JSON.stringify({
            type: 'candidate',
            candidate: event.candidate
        }));
    }
};

// 상대방으로부터 수신한 미디어 스트림을 처리
peerConnection.ontrack = (event) => {
    remoteStream.addTrack(event.track);
    $(".remote-video").srcObject = remoteStream;
};

// WebSocket을 통한 시그널링 메시지 처리
socket.onmessage = async (message) => {
    let data = JSON.parse(message.data);
    console.log(data);
    if (data.type == 'answer') {
        await peerConnection.setRemoteDescription(new RTCSessionDescription(data.sdp));
    } else if (data.type == 'candidate') {
        await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
    } else if (data.type == 'offer') {
        await peerConnection.setRemoteDescription(new RTCSessionDescription(data.sdp));

        let answer = await peerConnection.createAnswer();
        await peerConnection.setLocalDescription(answer);

        socket.send(JSON.stringify({
            type: 'answer',
            sdp: peerConnection.localDescription
        }));
    }
};

// WebSocket 연결이 완료된 후 Offer 생성
socket.onopen = () => {
    createOffer();
};

// 초기화
init();
