// 환불 신청 상태를 업데이트하는 함수 정의
function updateRefundButton(id) {
    // 해당 버튼의 텍스트 변경
    const button = $(".refund-btn");
    button.each(function () {
        const $button = $(this); // 현재 버튼을 jQuery 객체로 만듭니다.
        const buttonId = $button.data('id'); // data-id 값을 가져옵니다.
        if (buttonId == id) {
            $button.prop('disabled', true); // 버튼 비활성화
            $button.text('신청 완료'); // 텍스트 변경

            // 상태 텍스트를 변경
            const $statusCell = $button.closest('tr').find('td').eq(5); // 6번째 컬럼 (인덱스는 0부터 시작)
            if ($statusCell.length) {
                $statusCell.text('환불 요청 중'); // 상태 텍스트 변경
            }

            // 반복문 종료
            return false; // 각 버튼을 검사하다가 조건에 맞는 버튼을 찾으면 반복문을 종료합니다.
        }
    });
}


// 환불 사유 입력하는 새 창 띄우기
function handleRefundRequest(id) {
    $.ajax({
        type: "POST",
        url: "/pay/checkPoint?id=" + id,
        success: function(response) {
            alert(response); // 서버에서의 응답을 알림
            // 브라우저의 화면 크기 가져오기
            const screenWidth = window.innerWidth;
            const screenHeight = window.innerHeight;

            // 팝업 창 크기 설정
            const popupWidth = 750;
            const popupHeight = 670;

            // 중앙 위치 계산
            const popupLeft = (screenWidth - popupWidth) / 2 + window.screenX;
            const popupTop = (screenHeight - popupHeight) / 2 + window.screenY;

            // 팝업 창 띄우기 (위치 설정 포함)
            window.open("/pay/refundForm?id=" + id, "refundWindow", `width=${popupWidth},height=${popupHeight},left=${popupLeft},top=${popupTop}`);
        },
        error: function(xhr, status, error) {
            // 포인트 부족 시 에러 메시지 표시
            if (xhr.status == 400) {
                alert(xhr.responseText);
            } else {
                alert("포인트가 부족하여 환불을 신청할 수 없습니다.");
            }
        }
    });
}


$(document).ready(function () {
    // 모든 requestAt 셀을 선택
    $('#paymentList tr').each(function () {
        const $cell = $(this).find('td').eq(3);
        const originalTime = $cell.text().trim(); // 원본 텍스트 가져오기

        if (originalTime) {
            // Date 객체로 변환
            const date = new Date(originalTime);
            // 포맷팅 함수 정의
            const formattedTime = formatDate(date);
            // 포맷된 시간으로 텍스트 변경
            $cell.text(formattedTime);
        }

        // 모든 totalAmount 셀을 선택하여 금액을 포맷
        const $amountCell = $(this).find('td').eq(2);
        const originalAmount = $amountCell.text().trim(); // 원본 텍스트 가져오기

        if (originalAmount) {
            // 금액 포맷 처리
            const formattedAmount = formatAmount(originalAmount);
            $amountCell.text(formattedAmount);
        }
    });
});

// 금액을 포맷하는 함수 (1,000 단위로 쉼표 추가)
function formatAmount(amount) {
    // 숫자 변환 및 쉼표 추가하여 포맷 처리
    return parseInt(amount, 10).toLocaleString('ko-KR') + " 원";
}


// 환불 거부 시 거부 사유 상세보기 새 창 열기
function openRefuseReasonWindow(id) {
    const url = "/pay/refuseReasonDetail?id=" + id;

    // 브라우저의 화면 크기 가져오기
    const screenWidth = window.innerWidth;
    const screenHeight = window.innerHeight;

    // 팝업 창 크기 설정
    const popupWidth = 500;
    const popupHeight = 500;

    // 중앙 위치 계산
    const popupLeft = (screenWidth - popupWidth) / 2 + window.screenX;
    const popupTop = (screenHeight - popupHeight) / 2 + window.screenY;

    // 팝업 창 띄우기 (위치 설정 포함)
    window.open(url, "openRefuseReasonWindow", `width=${popupWidth},height=${popupHeight},left=${popupLeft},top=${popupTop}`);
}


// 날짜를 YY-MM-DD HH:mm:ss 형식으로 포맷하는 함수
function formatDate(date) {
    const year = String(date.getFullYear()).slice(2); // 연도에서 마지막 두 자리를 사용
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월을 2자리로 패딩
    const day = String(date.getDate()).padStart(2, '0'); // 일을 2자리로 패딩
    const hours = String(date.getHours()).padStart(2, '0'); // 시간을 2자리로 패딩
    const minutes = String(date.getMinutes()).padStart(2, '0'); // 분을 2자리로 패딩
    const seconds = String(date.getSeconds()).padStart(2, '0'); // 초를 2자리로 패딩

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}