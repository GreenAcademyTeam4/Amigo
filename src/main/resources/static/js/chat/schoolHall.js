$(document).ready(function() {
    $('#submit-selection').on('click', function() {
        const selectedGrade = $('#gradeSelect').val();
        const selectedClass = $('#classSelect').val();

        if (!selectedGrade || !selectedClass) {
            alert("학년과 반을 모두 선택해주세요.");
            return;
        }

        const requestData = {
            grade: selectedGrade,
            class: selectedClass
        };

        $.ajax({
            url: '/enterClass',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function(data) {
                screen.html(data);  // 화면에 응답 데이터 표시
            },
            error: function(error) {
                console.error("Error submitting selection:", error);
            }
        });
    });
});
