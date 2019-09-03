$("#queryOneCategoryId").change(function(){
    var queryOneCategoryId = $("#queryOneCategoryId").val();
    if(queryOneCategoryId != '' && queryOneCategoryId != null){
        $.ajax({
            type:"GET",//请求类型
            url:"/system/goods/categoryList.json",//请求的url
            data:{pid:queryOneCategoryId},//请求参数
            dataType:"json",//ajax接口（请求url）返回的数据类型
            success:function(data){//data：返回数据（json对象）
                $("#queryTwoCategoryId").html("");
                var options = "<option value=\"\">---全部---</option>";
                for(var i = 0; i < data.length; i++){
                    options += "<option value=\""+data[i].id+"\">"+data[i].categoryName+"</option>";
                }
                $("#queryTwoCategoryId").html(options);
            },
            error:function(data){//当访问时候，404，500 等非200的错误状态码
                alert("加载二级分类失败！");
            }
        });
    }else{
        $("#queryTwoCategoryId").html("");
        var options = "<option value=\"\">---全部---</option>";
        $("#queryTwoCategoryId").html(options);
    }
});

$(function () {
    var oneCategoryId =$("#queryOneCategoryId").val();
    if(oneCategoryId != '' && oneCategoryId != null){
        $.ajax({
            type:"GET",//请求类型
            url:"/system/goods/categoryList.json",//请求的url
            data:{pid:oneCategoryId},//请求参数
            dataType:"json",//ajax接口（请求url）返回的数据类型
            success:function(data){//data：返回数据（json对象）
                $("#queryTwoCategoryId").html("");
                var options = "<option value=\"\">---全部---</option>";
                for(var i = 0; i < data.length; i++){
                    options += "<option value=\""+data[i].id+"\">"+data[i].categoryName+"</option>";
                }
                $("#queryTwoCategoryId").html(options);
            },
            error:function(data){//当访问时候，404，500 等非200的错误状态码
                alert("加载二级分类失败！");
            }
        });
    }else{
        $("#queryTwoCategoryId").html("");
        var options = "<option value=\"\">---全部---</option>";
        $("#queryTwoCategoryId").html(options);
    }
});

//查看单体商品信息
$(".viewGoods").on("click",function(){
    var obj = $(this);
    window.location.href="goodsView/"+ obj.attr("goodsId");
});

//修改商品单体信息
$(".modifyGoods").on("click",function () {
    var obj = $(this);
    var state = obj.attr("state");
    if(state == "2" || state == "3"){
        window.location.href = "goodsModify/" + obj.attr("goodsId");
    }else{
        alert("该商品的状态为：【"+obj.attr("stateName")+"】,不能修改！");
    }
});

//删除商品信息
$(".deleteGoods").on("click",function() {
    var obj = $(this);
    var state = obj.attr("state");
    if(state == "2" || state == "3"){
        if(confirm("你确定要删除商品【"+obj.attr("goodsName")+"】吗？")){
            $.ajax({
                type:"GET",
                url:"delGoods.json",
                data:{id:obj.attr("goodsId")},
                dataType:"json",
                success:function(data){
                    if(data.result == "true"){//删除成功：移除删除行
                        alert("删除成功");
                        //obj.parents("tr").remove();
                        window.location.href = "/system/goods/list";
                    }else if(data.result == "false"){//删除失败
                        alert("对不起，删除商品【"+obj.attr("goodsName")+"】失败");
                    }else if(data.result == "error"){
                        alert("对不起，商品【"+obj.attr("goodsName")+"】不存在");
                    }
                },
                error:function(data){
                    alert("对不起，删除失败");
                }
            });
        }
    }else {
        alert("该商品的状态为：【"+obj.attr("stateName")+"】,不能删除！");
    }
});

//上、下架商品
$(".onSale").on("click",function() {
    var obj = $(this);
    var state = obj.attr("state");
    var id = obj.attr("goodsId");
    if(state == "2" || state == "3"){
        if(confirm("你确定要上架商品【"+obj.attr("goodsName")+"】吗？")){
            saleSwitchAjax(state,id,obj);
        }
    }else {
        if(confirm("你确定要下架商品【"+obj.attr("goodsName")+"】吗？")){
            saleSwitchAjax(state,id,obj);
        }
    }
});
function saleSwitchAjax(state,id,obj) {
    $.ajax({
        type:"GET",
        url:"sale.json",
        data:{state:state,id:id},
        dataType:"json",
        success:function(data){
            if(data.result == "true"){    //上、下架成功
                if(state == "2" || state == "3"){
                    alert("上架成功");
                    window.location.href = "/system/goods/list";
                }else {
                    alert("下架成功");
                    window.location.href = "/system/goods/list";
                }
            }else if(data.result == "false"){//上、下架失败
                if(state == "2" || state == "3"){
                    alert("对不起，上架商品【"+obj.attr("goodsName")+"】失败");
                }else {
                    alert("对不起，下架商品【"+obj.attr("goodsName")+"】失败");
                }
            }else {
                alert("对不起，商品【"+obj.attr("goodsName")+"】不存在");
            }
        },
        error:function(data){
            alert("对不起，操作失败");
        }
    });
}
$(".deleteGoodsLog").click(function () {
    var obj = $(this);
    if(confirm("你确定要删除会员登录日志信息吗？")){
        $.ajax({
            type:"GET",
            url:"deleteLog",
            data:{id:obj.attr("goodsLogId")},
            dataType:"json",
            success:function(data){
                if(data.result == "true"){//删除成功
                    alert("删除成功");
                    window.location.href = "/system/goods/logList";
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
$(".modifyStock").click(function () {
    var obj = $(this);
    var stock = obj.attr("stock");
    var goodId = obj.attr("goodsId");
    $("#goodsId").val(goodId);
    $("#stock").val(stock);
});