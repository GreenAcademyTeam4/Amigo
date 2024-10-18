$(document).ready(function() {
    $('#birth').on('input', function() {
      
      if ($(this).val().length > 6) {
        $(this).val($(this).val().slice(0, 6));
      }
    });
  });

  $(document).ready(function(){
    $('#phoneNumber').on('input', function(){
      
        if ($(this).val().length > 11) {
            $(this).val($(this).val().slice(0, 11));
        }
    });
});