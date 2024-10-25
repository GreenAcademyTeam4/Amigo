let localStream;
let remoteStream;

let init = async () => {
    localStream = await navigator.mediaDevices.getUserMedia({video:true, audio:true});
    $(".user-video").srcObject = localStream;
}

let createOffer = async () => {
    peerConnection = new RTCPeerConnection();

    remoteStream = new MediaStream();

    let offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);
}

init ();