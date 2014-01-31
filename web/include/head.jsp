<%-- 
    Document   : head
    Created on : Jan 5, 2014, 4:59:57 PM
    Author     : paolo
--%>

<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.css">
<link rel="stylesheet" href="/media/css/jquery.dataTables.css">
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.js"></script>
<script src="/media/js/jquery.dataTables.min.js"></script>
<script src="/media/js/utils.js"></script>
<script>
    $(document).bind("mobileinit", function(){
        $.mobile.page.prototype.options.domCache = false;
    });
</script>
