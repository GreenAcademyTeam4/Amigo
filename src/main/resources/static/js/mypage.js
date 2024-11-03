$(document).ready(function() {

    const inventory = $('#my-inventory'); // 올바르게 선언

    inventory.on('click', function() {
        fetch("/my-page/inventory")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('등교하기 중 오류 발생:', error);
            });
    });

    // 친구 관리
    $('#my-friend').on('click', function() {
        fetch("/my-page/friend-list")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('친구 관리 페이지 로드 중 오류 발생:', error);
            });
    });

    // 포인트 충전 내역
    $('#my-chargeHistory').on('click', function() {
        fetch("/pay/paymentList")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('충전 내역 페이지 로드 중 오류 발생:', error);
            });
    });

    // 비밀번호 변경
    $('#my-passwordChange').on('click', function() {
        fetch("/my-page/pwdcheck")
            .then(response => response.text())
            .then(data => {
                screen.html(data);
            })
            .catch(error => {
                console.error('비밀번호 변경 페이지 로드 중 오류 발생:', error);
            });
    });
});
