$(function () {
   var Level1 = $("#Level1").val();
   var Level2 = $("#Level2").val();

    //动态加载一级分类列表
    loadCategoryLevel(null,Level1,"oneCategoryId");
    //动态加载二级分类列表
    loadCategoryLevel(Level1,Level2,"twoCategoryId");

    //联动效果：动态加载二级分类列表
    $("#oneCategoryId").change(function(){
        var categoryLevel1 = $("#oneCategoryId").val();
        if(categoryLevel1 != '' && categoryLevel1 != null){
            loadCategoryLevel(categoryLevel1,Level2,"twoCategoryId");
        }else{
            $("#twoCategoryId").html("");
            var options = "<option value=\"\">--请选择--</option>";
            $("#twoCategoryId").html(options);
        }
    });

    $("#back").on("click",function(){
        window.location.href = "/system/goods/list";
    });

    var logoPicPath = $("#logoPicPath").val();
    var id = $("#goodsId").val();
    if(logoPicPath == null || logoPicPath == "" ){
        $("#uploadFile").show();
    }else {
        $("#logoFile").show();
    }
});
function loadCategoryLevel(pid,level,categoryLevel) {
    $.ajax({
        type:"GET",//请求类型
        url:"/system/goods/categoryList.json",//请求的url
        data:{pid:pid},//请求参数
        dataType:"json",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            $("#"+categoryLevel).html("");
            var options = "<option value=\"\">--请选择--</option>";
            for(var i = 0; i < data.length; i++){
                if(level != null && level != undefined && data[i].id == level ){
                    options += "<option selected=\"selected\" value=\""+data[i].id+"\" >"+data[i].categoryName+"</option>";
                }else{
                    options += "<option value=\""+data[i].id+"\">"+data[i].categoryName+"</option>";
                }
            }
            $("#"+categoryLevel).html(options);
        },
        error:function(data){//当访问时候，404，500 等非200的错误状态码
            alert("加载分类列表失败！");
        }
    })
}

function delFile(){
    var id = $("#goodsId").val();
    $.ajax({
        type:"GET",//请求类型
        url:"/system/goods/delFile.json",//请求的url
        data:{id:id},//请求参数
        dataType:"json",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            if(data.result == "true"){
                alert("删除成功！");
                //window.location.href="/system/goods/goodsModify/"+id;
                $("#uploadFile").show();
                $("#logoFile").html('');
            }else if(data.result == "false"){
                alert("删除失败！");
            }
        },
        error:function(data){//当访问时候，404，500 等非200的错误状态码
            alert("请求错误！");
        }
    });
}