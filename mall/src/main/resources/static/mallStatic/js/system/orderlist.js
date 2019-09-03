$(".viewOrder").click(function () {
    var obj = $(this);
    console.log("成功进入啦:"+obj.attr("orderId"));
    window.location.href="/system/order/view?id="+obj.attr("orderId");
});
$(".deleteOrder").click(function () {
    var obj = $(this);
    if(confirm("删除后不可恢复，你确定要删除订单【"+obj.attr("orderCode")+"】吗？")){
        $.ajax({
            type:"GET",
            url:"deleteOrder",
            data:{id:obj.attr("orderId")},
            dataType:"json",
            success:function(data){
                if(data.result == "true"){//删除成功
                    alert("删除成功");
                    console.log("删除成功啦！11");
                    window.location.href = "/system/order/list";
                }else {//删除失败
                    alert("对不起，删除商品【"+obj.attr("orderCode")+"】失败");
                }
            },
            error:function(data){
                alert("对不起，删除失败");
            }
        });
    }
});

$(".deleteOrderLog").click(function () {
    var obj = $(this);
    if(confirm("你确定要删除订单日志信息吗？")){
        $.ajax({
            type:"GET",
            url:"deleteLog",
            data:{id:obj.attr("orderLogId")},
            dataType:"json",
            success:function(data){
                if(data.result == "true"){//删除成功
                    alert("删除成功");
                    window.location.href = "/system/order/logList";
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

$(".deliverOrder").click(function () {
    var obj = $(this);
    console.log("订单发货啦！！！："+obj.attr("orderId"));
    window.location.href="/system/order/deliver?id="+obj.attr("orderId");
});
$("#back").click(function () {
    window.location.href="list";
});
$("#sure").click(function () {
    var id=$("#orderId").val();
    if(confirm("是否进行发货操作？")){
        $.ajax({
            type:"GET",
            url:"deliverSave",
            data:{id:id},
            dataType:"json",
            success:function(data){
                if(data.result == "true"){//发货成功
                    alert("发货成功");
                    window.location.href = "/system/order/list";
                }else {//删除失败
                    alert("对不起，发货成功失败");
                    window.location.href = "/system/order/list";
                }
            },
            error:function(data){
                alert("对不起，发货失败");
            }
        });
    }else {
        alert("已取消发货");
    }
})
