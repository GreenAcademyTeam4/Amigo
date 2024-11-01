// checkbox 선택 시 다른 모든 checkbox 비활성화
$(document).ready(function() {
    const $checkboxes = $('.form-check-input');

    $checkboxes.change(function() { // 체크박스의 상태가 변경될 때마다 실행되는 이벤트 리스너 추가
        if (this.checked) { // 체크박스가 체크된 상태인지 확인
            // 현재 체크된 체크박스를 제외한 모든 체크박스 비활성화
            $checkboxes.not(this).prop('disabled', true);
        } else {
            // 체크박스가 선택 해제되면, 모든 체크박스 다시 활성화
            $checkboxes.prop('disabled', false);
        }
    });

    // 환불 사유 textarea checkbox 활성화/비활성화
    // 비활성화 시 text 내용 초기화
    $('#otherReasonCheckbox').change(function() {
        const textarea = $('#comment');
        const charCount = $('#charCount');
        textarea.prop('disabled', !this.checked);
        if (!this.checked) {
            textarea.val('');
            charCount.text('0');

        }
    });
});

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

$('button[type="submit"]').on('click', function(event) {
    event.preventDefault(); // 새로고침 방지

    // 환불 사유 체크
    const $reasonCheckboxes = $('.form-check-input');
    let isChecked = false;
    let selectedValue = '';
    // 체크박스 중 하나라도 선택되어 있는지 확인
    $reasonCheckboxes.each(function() {
        if ($(this).is(':checked')) {
            isChecked = true;
            selectedValue = $(this).val(); // 선택된 체크박스의 value 가져오기
        }
    });
    if(selectedValue == "기타 사유") {
        selectedValue = $("#comment").val();
    }
    if (!isChecked) {
        alert('환불 사유를 선택해주세요.');
        return; // 폼 제출 중단
    }

    // 모든 조건이 만족되면 서버에 POST 요청 보내기
    const id = $('input[name="id"]').val();
    $.ajax({
        url: "/pay/modifyRefundStatus",
        type: "POST",
        data: {
            chargeHistoryId: id,
            refundStatus: 'request',
            cancelReason: selectedValue
        },
        success: function(response) {
            alert("환불 신청이 완료되었습니다."); // 성공적으로 환불 신청이 완료된 경우 알림창 띄우기

            if (window.opener) {
                window.opener.updateRefundButton(id); // 부모 창의 함수 호출
                window.close(); // 성공적으로 업데이트가 된 경우 창 닫기
            } else {
                alert("부모 창이 열려 있지 않습니다.");
            }
        },
        error: function() {
            alert("환불 신청 처리 중 오류가 발생했습니다.");
        }
    });
});