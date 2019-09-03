$(function () {
    var button = document.getElementById('btnSend');
    button.click();//执行点击按钮
    var orderCookie = $(".orderId").val();
    //读取cookie
    if ($.cookie(orderCookie) != undefined && !isNaN($.cookie(orderCookie))) {  //读取到了cookie值
        var endTime = $.cookie(orderCookie);
        var now = new Date().getTime();  //当前时间戳
        var secends = parseInt((endTime - now) / 1000);
        if (secends <= 0) {
            $.cookie(orderCookie, null);
            window.location.href = "/mall/payFailure?id="+orderCookie;
        } else {
            LockButton('#btnSend', secends);
        }
    }

})
// 按钮倒计时
var LockButton = function (btnObjId, secends) {
    //1.获取当前系统时间
    //2.获取 secends 后的系统时间
    //3.用cookie保存到期时间
    //4.每次加载后获取cookie中保存的时间
    //5.用到期时间减去当前时间获取倒计时
    var orderCookie = $(".orderId").val();
    var endTime = $.cookie(orderCookie);
    if (endTime == null || endTime == undefined || endTime == 'undefined' || endTime == 'null') {
        var now = new Date().getTime();  //当前时间戳
        var endTime = secends * 1000 + now;  //结束时间戳
        $.cookie(orderCookie, endTime);  //将结束时间保存到cookie
    }
    var hours = Math.floor(secends / (60 * 60));
    var modulo = secends % (60 * 60);
    //分钟
    var minutes = Math.floor(modulo / 60);
    //秒
    var seconds = modulo % 60;
    $(btnObjId).text(hours + "小时" + minutes + "分钟" + seconds + "秒");
    var timer = setInterval(function () {
        secends--;
        var hours = Math.floor(secends / (60 * 60));
        var modulo = secends % (60 * 60);
        //分钟
        var minutes = Math.floor(modulo / 60);
        //秒
        var second = modulo % 60;
        $(btnObjId).text(hours + "小时" + minutes + "分钟" + second + "秒");
        if (secends <= 0) {
            //倒计时结束清除cookie值
            $.cookie(orderCookie, null);
            $("#btnSend").prop("disabled",true);
            window.location.href = "/mall/payFailure?id="+orderCookie;
            clearInterval(timer);
        }
    }, 1000);
};