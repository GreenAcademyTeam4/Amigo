
let selectedPaymentMethod = null;

function selectPaymentMethod(method) {
    if(selectedPaymentMethod != null){
        $('#selectedPaymentMethod');
    }
    selectedPaymentMethod = method;
    $('#selectedPaymentMethod');
}

// SDK 초기화(TossPayments())
// 클라이언트 키를 파라미터로 넣으면 상점의 정보 확인 가능
const clientKey = "test_ck_DnyRpQWGrNqpzLg1DZKOVKwv1M9E";
const tossPayments = TossPayments(clientKey); // API 개별 연동 키
const customerKey = generateRandomString();
const payment = tossPayments.payment({customerKey});
//let phoneNumber = "{{phoneNumber}}";
//console.log(phoneNumber);


async function requestPayment(point){
    let amount = {
        currency: "KRW",
        value: point
    };
    switch (selectedPaymentMethod){
        case "CARD":
            await payment.requestPayment({
                method: "CARD", // 카드 및 간편 결제
                amount,
                orderId: generateRandomString(),
                orderName: "포인트 충전" + point + "원",
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
                orderName: "포인트 충전 " + point + "원",
                //customerMobilePhone: phoneNumber,
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
                orderName: "포인트 충전" + point + "원",
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