 
$(document).ready(function () {
	  $('body').contextMenu('myMenu1', 
     {
          bindings: 
          {
            'flash': function(t) {
              self.location.reload();  
            },
            'chose': function(t) {
               $('#pointer').click();
            },
            'linkline':function(t){
               $('#path').click();
            }
          }

    }); 
});

 