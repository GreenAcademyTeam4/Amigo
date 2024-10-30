
// 글자수 표시
$(document).ready(function() {
    const $textarea = $('#comment');
    const $charCount = $('#charCount');
    const maxLength = 200; // 최대 글자 수

    // 텍스트 영역의 입력 이벤트를 감지하여 글자 수를 업데이트
    $textarea.on('input', function() {
        let textLength = $(this).val().length;

        // 최대 글자 수를 초과하면, 초과한 부분을 제거
        // 만약 글자 수가 maxLength를 초과하면, val() 값을 최대 글자 수만큼 잘라서 다시 설정
        if (textLength > maxLength) {
            $(this).val($(this).val().substring(0, maxLength));
            textLength = maxLength;
        }

        // 현재 글자 수를 표시
        $charCount.text(textLength);
    });
});


function submitRefuseReason() {
    const formData = {
        id: parseInt($('input[name="id"]').val(), 10),
        chargeHistoryId: parseInt($('input[name="chargeHistoryId"]').val(), 10),
        refundRefuseReason: $('#comment').val() // 텍스트 입력 값
    };


    $.ajax({
        type: "POST",
        url: "/pay/createRefuseReason", // 컨트롤러 메서드와 일치해야 함
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function(response) {
            alert("반려 사유가 성공적으로 저장되었습니다.");
            window.opener.updateRefuseStatus(formData.id, formData.chargeHistoryId, formData.refundRefuseReason); // 부모 창의 상태 업데이트 함수 호출
            window.close(); // 창 닫기
        },
        error: function(xhr, status, error) {
            alert("반려 사유 저장 실패: " + error);
        }
    });
}