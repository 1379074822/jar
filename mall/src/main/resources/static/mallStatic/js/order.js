$(function () {
    $(".cancelOrder").click(function () {
        var obj = $(this);
        if (confirm("你确定要取消订单【" + obj.attr("code") + "】吗？")) {
            switchState(obj.attr("cancel"));
        }
    });
    $(".confirm").click(function () {
        var obj = $(this);
        if (confirm("你确定要收货吗？")) {
            switchState(obj.attr("oid"));
        }
    });

    $("#back").click(function () {
        window.location.href="/mall/myOrder?status="+0;
    });

    $(".submitComment").click(function () {
        var ids = commentIds();
        var comments = goodsComments();
        var orderCode = $(".orderCode").val();
        if(ids == null || ids == ""){
            layer.msg("你未评论任务商品，无法提交!",{icon:2,time:1500});
        }else {
            ajaxComment(ids,comments,orderCode);
        }
    });

})
function switchState(id) {
    $.ajax({
        type:"GET",
        url:"switchOrder",
        data:{id:id},
        dataType:"json",
        success:function(data){
            if(data.result == "true"){//删除成功：移除删除行
                alert("操作成功!")
                window.location.href = "/mall/myOrder?status="+0;
            }else{  //删除失败
                alert("对不起，取消订单【"+obj.attr("code")+"】失败");
            }
        },
        error:function(data){
            alert("对不起，取消失败");
        }
    });
}

function commentIds() {
    var ids = '';
    var obj = document.getElementsByClassName("goodsComment");
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].value != '' && obj[i].value != null) { //如果选中，将value添加到数组s中
            ids += obj[i].id + ',';
        }
    }
    return ids;
}

function goodsComments() {
    var comments = new Array();
    var obj = document.getElementsByClassName("goodsComment");
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].value != "" &&  obj[i].value != null) { //如果选中，将value添加到数组s中
            comments.push(obj[i].value);
        }
    }
    return comments;
}

function ajaxComment(ids,comments,orderCode) {
    $.ajax({
        type:"GET",
        url:"/mall/submitComments",
        data:{ids:ids,comments:comments,orderCode:orderCode},
        dataType:"json",
        traditional: true,
        success:function (data) {
            if(data.result == "true"){
                window.location.href="/mall/myOrder?status="+0;
            }else if(data.result == "false"){
                layer.msg("评论失败!",{icon:2,time:1500});
            }else {
                layer.msg("你未评论任何商品，请重新评论!",{icon:2,time:1500});
            }
        },
        error:function (data) {
            alert("操作失败!");
        }
    });
}

function getOrderList(that,status) {
    /*
    if (that != '') {
        $(that).parent().parent().each(function () {
            $(this).find('.link').removeClass('on')
        });
        $(that).addClass('on');
    }*/
    window.location.href="/mall/myOrder?status="+status;
}