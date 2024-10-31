let localStream;
let remoteStream = new MediaStream();
let peerConnection;
let cameraEnabled = true;
let microphoneEnabled = true;

const servers = {
    iceServers: [
        { urls: ['stun:stun1.l.google.com:19302', 'stun:stun2.l.google.com:19302'] }
    ]
};

const socket = new WebSocket("ws://localhost:8080/signaling");

// 처음 입장할때 상대방의 id를 키값으로 내 세션을 저장
socket.send(JSON.stringify({
    type: "roomId",
    message: friendId
}));

// 나갈때 상대방의 id로 저장해놓은 내 세션을 제거
socket.onclose = () => {
    socket.send(JSON.stringify({
        type: "out",
        message: friendId
    }));
}

// 상대방이 오퍼를 보내거나 답장이 오거나 ice후보들을 보낼때 처리
socket.onmessage = async (event) => {
    let data = JSON.parse(event.data);
    if (data.type === 'offer') {
        await handleOffer(data);
    } else if (data.type === 'answer') {
        await handleAnswer(data);
    } else if (data.type === 'ice-candidate') {
        await handleIceCandidate(data);
    }
};

let init = async () => {
    try {
        localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
        document.querySelector(".user-video").srcObject = localStream;

        // 사용자 로딩 애니메이션 제거 및 프로필 숨김
        document.querySelector(".user-loading").style.opacity = 0;
        document.querySelector(".user-loading").style.display = "none";
        document.querySelector(".user-profile").style.display = "none";

        createOffer();
    } catch (error) {
        console.error("Error accessing media devices.", error);
    }
};

let createOffer = async () => {
    peerConnection = new RTCPeerConnection(servers);
    remoteStream = new MediaStream();
    document.querySelector('.remote-video').srcObject = remoteStream;

    localStream.getTracks().forEach((track) => {
        peerConnection.addTrack(track, localStream);
    });

    peerConnection.ontrack = (event) => {
        event.streams[0].getTracks().forEach((track) => {
            remoteStream.addTrack(track);
        });

        // 상대방 로딩 애니메이션 제거 및 프로필 숨김
        document.querySelector(".remote-loading").style.opacity = 0;
        document.querySelector(".remote-loading").style.display = "none";
        document.querySelector(".remote-profile").style.display = "none";
    };

    peerConnection.onicecandidate = async (event) => {
        if (event.candidate) {
            socket.send(JSON.stringify({ type: 'ice-candidate', candidate: event.candidate }));
        }
    };

    let offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);
    socket.send(JSON.stringify({ type: 'offer', sdp: offer.sdp }));
};

let handleOffer = async (offer) => {
    peerConnection = new RTCPeerConnection(servers);
    remoteStream = new MediaStream();
    document.querySelector('.remote-video').srcObject = remoteStream;

    localStream.getTracks().forEach((track) => {
        peerConnection.addTrack(track, localStream);
    });

    peerConnection.ontrack = (event) => {
        event.streams[0].getTracks().forEach((track) => {
            remoteStream.addTrack(track);
        });

        // 상대방 로딩 애니메이션 제거 및 프로필 숨김
        document.querySelector(".remote-loading").style.opacity = 0;
        document.querySelector(".remote-loading").style.display = "none";
        document.querySelector(".remote-profile").style.display = "none";
    };

    peerConnection.onicecandidate = async (event) => {
        if (event.candidate) {
            socket.send(JSON.stringify({ type: 'ice-candidate', candidate: event.candidate }));
        }
    };

    await peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
    let answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    socket.send(JSON.stringify({ type: 'answer', sdp: answer.sdp }));
};

let handleAnswer = async (answer) => {
    await peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
};

let handleIceCandidate = async (data) => {
    if (data.candidate) {
        await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
    }
};

// 카메라 및 마이크 제어
function toggleCamera() {
    cameraEnabled = !cameraEnabled;
    localStream.getVideoTracks()[0].enabled = cameraEnabled;
    document.querySelector(".user-profile").style.display = cameraEnabled ? "none" : "block";
}

function toggleMicrophone() {
    microphoneEnabled = !microphoneEnabled;
    localStream.getAudioTracks()[0].enabled = microphoneEnabled;
}

function endCall() {
    socket.close();
    peerConnection.close();
    document.querySelector(".user-video").srcObject = null;
    document.querySelector(".remote-video").srcObject = null;
}

init();
