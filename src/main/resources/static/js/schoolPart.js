$(document).ready(function () {
    let selectedElement = null;
    let originalColor = '';

    $('.school-item').on('click', function () {
        const id = $(this).data('id');
        const currentPart = $(this).find('.school-part');

        if (selectedElement) {
            $(selectedElement).find('.school-part').css('background-color', originalColor);
        }


        originalColor = currentPart.css('background-color');
        currentPart.css('background-color', '#D3E5FF');
        selectedElement = this;

        $.ajax({
            url: `/change/${id}`,
            method: 'POST',
            data: { id: id },
            success: function (response) {
                $('.screen-area').html(response);
            },
            error: function (xhr, status, error) {
                console.error('에러 발생:', error);
            }
        });
    });
});
$('.school-part').each(function (index) {
    switch (index % 5) {
        case 0:
            $(this).css('background-color', '#EC9CB5');
            break;
        case 1:
            $(this).css('background-color', '#FFD3B4');
        case 2:
            $(this).css('background-color', '#FFF3B0');
            break;
        case 3:
            $(this).css('background-color', '#A0E7E5');
            break;
        case 4:
            $(this).css('background-color', '#B4A0E5');
            break;
    }
});