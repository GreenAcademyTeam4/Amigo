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
});
