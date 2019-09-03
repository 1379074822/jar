$(".viewUser").on("click",function(){
    var obj = $(this);
    window.location.href="userView/"+ obj.attr("userId");
});

$("#back").on("click",function(){
    window.location.href = "/system/user/list";
});

$("#backGoods").on("click",function(){
    window.location.href = "/system/goods/list";
});
$(".prohibitUser").on("click",function() {
    var obj = $(this);
    var id = obj.attr("userId");
    var state = obj.attr("state");
    if (confirm("你确定要将会员【" + obj.attr("account") + "】加入黑名单？")) {
        operationUser(id,state);
    }
});
$(".enableUser").on("click",function () {
    var obj = $(this);
    var id = obj.attr("userId");
    var state = obj.attr("state");
    if (confirm("你确定要解封会员【" + obj.attr("account") + "】？")){
        operationUser(id,state);
    }
});

$(".deleteUserLog").click(function () {
    var obj = $(this);
    if(confirm("你确定要删除会员登录日志信息吗？")){
        $.ajax({
            type:"GET",
            url:"deleteLog",
            data:{id:obj.attr("userLogId")},
            dataType:"json",
            success:function(data){
                if(data.result == "true"){//删除成功
                    alert("删除成功");
                    window.location.href = "/system/user/logList";
                }else {//删除失败
                    alert("对不起，删除失败");
                }
            },
            error:function(data){
                alert("对不起，删除失败");
            }
        });
    }
});
function operationUser(id,state) {
    $.ajax({
        type: "GET",
        url: "operationUser",
        data: {id: id,state:state},
        dataType: "json",
        success: function (data) {
            if (data.result == "true") {//删除成功：移除删除行
                alert("操作成功");
                window.location.href="list";
            } else if (data.result == "false") {//删除失败
                alert("对不起，操作失败");
            } else {
                alert("对不起，会员不存在");
            }
        },
        error: function (data) {
            alert("对不起，操作失败");
        }
    });
}