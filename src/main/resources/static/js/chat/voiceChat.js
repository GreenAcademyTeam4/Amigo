(function(global) {
    let localStream;
    let remoteStream = new MediaStream();
    let peerConnection;
    let cameraEnabled = true;
    let microphoneEnabled = true;
    tryCall = true;
    const screen = $('.screen-area');
    const servers = {
        iceServers: [
            { urls: ['stun:stun1.l.google.com:19302', 'stun:stun2.l.google.com:19302'] }
        ]
    };

    const voiceSocket = new WebSocket("ws://192.168.0.113:8080/signaling");

    global.friendId = friendId; // friendId를 전역 스코프에 설정

    voiceSocket.onopen = () => {
        voiceSocket.send(JSON.stringify({
            type: "roomId",
            message: friendId
        }));
    };

    window.addEventListener('beforeunload', () => {
        if (voiceSocket.readyState === WebSocket.OPEN) {
            voiceSocket.send(JSON.stringify({
                type: "out",
                message: friendId
            }));
            voiceSocket.close();
        }
    });

    voiceSocket.onmessage = async (event) => {
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

            document.querySelector(".remote-loading").style.opacity = 0;
            document.querySelector(".remote-loading").style.display = "none";
            document.querySelector(".remote-profile").style.display = "none";
        };

        // 상대방의 연결 상태 변화를 감지하는 이벤트 리스너
        peerConnection.oniceconnectionstatechange = () => {
            if (peerConnection.iceConnectionState === "disconnected" ||
                peerConnection.iceConnectionState === "failed" ||
                peerConnection.iceConnectionState === "closed") {

                // 상대방이 나갔을 때 실행할 코드
                alert("상대방이 나갔습니다");
                global.endCall(); // 통화 종료 함수 호출
            }
        };

        peerConnection.onicecandidate = async (event) => {
            if (event.candidate) {
                voiceSocket.send(JSON.stringify({ type: 'ice-candidate', candidate: event.candidate }));
            }
        };

        let offer = await peerConnection.createOffer();
        await peerConnection.setLocalDescription(offer);
        voiceSocket.send(JSON.stringify({ type: 'offer', sdp: offer.sdp }));
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

            document.querySelector(".remote-loading").style.opacity = 0;
            document.querySelector(".remote-loading").style.display = "none";
            document.querySelector(".remote-profile").style.display = "none";
        };

        peerConnection.onicecandidate = async (event) => {
            if (event.candidate) {
                voiceSocket.send(JSON.stringify({ type: 'ice-candidate', candidate: event.candidate }));
            }
        };

        await peerConnection.setRemoteDescription(new RTCSessionDescription(offer));
        let answer = await peerConnection.createAnswer();
        await peerConnection.setLocalDescription(answer);
        voiceSocket.send(JSON.stringify({ type: 'answer', sdp: answer.sdp }));
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
    global.toggleCamera = function() {
        cameraEnabled = !cameraEnabled;
        localStream.getVideoTracks()[0].enabled = cameraEnabled;
        document.querySelector(".user-profile").style.display = cameraEnabled ? "none" : "block";
    };

    global.toggleMicrophone = function() {
        microphoneEnabled = !microphoneEnabled;
        localStream.getAudioTracks()[0].enabled = microphoneEnabled;
    };

    global.endCall = function() {
        callStatus = false;
        tryCall = false;
        voiceSocket.send(JSON.stringify({
            type: "out",
            message: friendId
        }));
        voiceSocket.close();
        peerConnection.close();
        document.querySelector(".user-video").srcObject = null;
        document.querySelector(".remote-video").srcObject = null;
        fetch("/board/list")
            .then(response => response.text())
            .then(data => {
                screen.empty().html(data);
            })
            .catch(error => {
                console.error('등교하기 중 오류 발생:', error);
            });
    };
    init();
})(window);
