
let selectedPaymentMethod = null;

function selectPaymentMethod(method) {
    if(selectedPaymentMethod != null){
        $('#selectedPaymentMethod');
    }
    selectedPaymentMethod = method;
    $('#selectedPaymentMethod');

    // 모든 버튼에서 selected 클래스 제거
    document.querySelectorAll('.button').forEach(button => {
        button.classList.remove('selected');
    });

    // 선택된 버튼에 selected 클래스 추가
    const selectedButton = document.getElementById(method);
    selectedButton.classList.add('selected');
}

function handlePointSelection() {
    const pointSelect = document.getElementById("point-select");
    const selectedValue = pointSelect.value;
    const pointInput = document.getElementById("point-input");

    if (selectedValue === "custom") {
        // "직접입력" 옵션을 선택하면 input 필드를 표시
        pointInput.style.display = "block";
        pointSelect.style.display = "block";
        pointInput.focus(); // 입력 필드에 포커스 설정
    } else {
        // 다른 값을 선택했을 때 input 필드를 숨기고 버튼을 업데이트
        pointInput.style.display = "none";
        updateChargeButton(parseInt(selectedValue, 10));
    }
}

function updateChargeButton(amount) {
    const pointInput = document.getElementById("point-input");
    const chargeButton = document.getElementById("chargeButton");

    let amountValue = amount || parseInt(pointInput.value, 10);

    // 입력된 값이 1 이상, 1,000,000 이하인지 확인
    if (amountValue >= 1 && amountValue <= 1000000) {
        chargeButton.disabled = false;
        chargeButton.onclick = function () {
            // 버튼을 눌렀을 때 100 단위로 나누어 떨어지는지 확인
            if (amountValue % 100 !== 0) {
                alert("충전 금액은 100 단위로 입력해주세요.");
                pointInput.value = '';
                chargeButton.disabled = true;
            } else {
                requestPayment(amountValue);
            }
        };
    } else if (amountValue < 1 || amountValue > 1000000) {
        alert("충전 금액은 1,000,000원 이하로 입력해주세요.");
        pointInput.value = '';
        chargeButton.disabled = true;
    }

    // $(document).ready(function() {
    //     $('#point-input').on('input', function() {
    //         let value = parseInt($(this).val(), 10);
    //         if (!isNaN(value) && value % 100 !== 0) {
    //             // 100단위가 아닌 금액 입력 시 경고 메시지를 표시하고 값 초기화
    //             alert("충전 금액은 100단위로 입력해 주세요.");
    //             $(this).val(''); // 입력된 값을 지움
    //         }
    //     });
    // });
}

// 기본으로 1,000원이 선택되도록 설정
window.onload = function () {
    updateChargeButton(1000);
};

// SDK 초기화(TossPayments())
// 클라이언트 키를 파라미터로 넣으면 상점의 정보 확인 가능
const clientKey = "test_ck_DnyRpQWGrNqpzLg1DZKOVKwv1M9E";
const tossPayments = TossPayments(clientKey); // API 개별 연동 키
const customerKey = generateRandomString();
const payment = tossPayments.payment({customerKey});

let phoneNumber = document.getElementById("phoneNumberHidden").value; // Hidden input의 값을 가져옴

async function requestPayment(point){

    const amountValue = parseInt(point, 10);

    if (isNaN(amountValue) || amountValue <= 0) {
        alert("충전할 금액이 올바르지 않습니다.");
        return;
    }

    if (!selectedPaymentMethod) {
        alert("결제 방법을 선택해주세요.");
        return;
    }

    // 금액 포맷
    const formattedAmount = amountValue.toLocaleString('ko-KR');


    let amount = {
        currency: "KRW",
        value: amountValue
    };

    console.log(`충전할 금액: ${formattedAmount}원`); // 포맷된 금액을 로그에 표시

    switch (selectedPaymentMethod){
        case "CARD":
            await payment.requestPayment({
                method: "CARD", // 카드 및 간편 결제
                amount,
                orderId: generateRandomString(),
                orderName: "포인트 충전" + formattedAmount + "원",
                successUrl: 'http://localhost:8080/pay/success',
                failUrl: 'http://localhost:8080/pay/fail',
                card: {
                    useEscrow: false,
                    flowMode: "DEFAULT",
                    useCardPoint: false,
                    useAppCardOnly: false,
                },
            });
            break;

        case "TRANSFER":
            await payment.requestPayment({
                method: "TRANSFER", // 계좌이체 결제
                amount,
                orderId: generateRandomString(),
                orderName: "포인트 충전 " + formattedAmount + "원",
                customerMobilePhone: phoneNumber,
                successUrl: 'http://localhost:8080/pay/success',
                failUrl: 'http://localhost:8080/pay/fail',
                transfer: {
                    cashReceipt: {
                        type: "소득공제",
                    },
                    useEscrow: false,
                },
            });
            break;

        case "MOBILE_PHONE":
            await payment.requestPayment({
                method: "MOBILE_PHONE", // 휴대폰 결제
                amount,
                orderId: generateRandomString(),
                orderName: "포인트 충전" + formattedAmount + "원",
                successUrl: 'http://localhost:8080/pay/success',
                failUrl: 'http://localhost:8080/pay/fail',
            });
            break;
    }
}

// Math.random(): 0 ~ 1 사이 무작위 난수 생성
// window.btoa : 문자열을 Base64 인코딩된 문자열로 변환
// slice(0, 20): 문자열 첫번째 인덱스 ~ 20번째 인덱스까지의 문자 추출
function generateRandomString() {
    return window.btoa(Math.random()).slice(0, 20);
}