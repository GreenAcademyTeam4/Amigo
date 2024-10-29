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

const socket = new WebSocket("ws://192.168.0.113:8080/signaling");

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
        document.querySelector(".user-profile").style.display = "none"; // 캠이 켜지면 프로필 숨김
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
        document.querySelector(".remote-loading").style.opacity = 0; // 상대방 들어오면 로딩 제거
        document.querySelector(".remote-profile").style.display = "none"; // 상대방 캠 켜지면 프로필 숨김
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
        document.querySelector(".remote-loading").style.opacity = 0; // 상대방 들어오면 로딩 제거
        document.querySelector(".remote-profile").style.display = "none"; // 상대방 캠 켜지면 프로필 숨김
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
