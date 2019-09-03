$(function () {
    //动态加载一级分类列表
    $.ajax({
        type:"GET", //请求类型
        url: "/system/goods/categoryList.json", //请求url
        data:{pid:null}, //请求参数
        dataType:"json",  //ajax接口(请求url)返回的数据类型
        success:function (data) {
            $("#oneCategoryId").html();
            var options = "<option value=\"\">--请选择--</option>";
            for(var i=0;i<data.length;i++){
                options += "<option value=\"" + data[i].id +"\">"+data[i].categoryName+"</option>";
            }
            $("#oneCategoryId").html(options);
        },
        error:function (data) {
            alert("动态加载一级列表失败!");
        }
    });

    //动态加载二级分类列表
    $("#oneCategoryId").change(function () {
        var oneCategoryId = $("#oneCategoryId").val();
        if(oneCategoryId != null && oneCategoryId !=''){
            $.ajax({
                type:"GET",
                url:"/system/goods/categoryList.json",
                data:{pid:oneCategoryId},
                dataType:"json",
                success:function (data) {
                    $("#twoCategoryId").html("");
                    var options = "<option value=\"\">--请选择--</option>";
                    for(var i = 0;i<data.length;i++){
                        options += "<option value=\"" + data[i].id +"\">"+data[i].categoryName+"</option>";
                    }
                    $("#twoCategoryId").html(options);
                },
                error:function (data) {
                    alert("加载二级分类列表失败!")
                }
            });
        }else {
            $("#twoCategoryId").html();
            var options = "<option value=\"\">--请选择--</option>";
            $("#twoCategoryId").html(options);
        }
    });

    //返回列表页面
    $("#back").on("click",function () {
        window.location.href = "list";
    });

    //ajax后台验证--goodsName是否存在
    $("#goodsName").bind("blur",function () {
        $.ajax({
            type:"GET",
            url:"checkGoodsName",
            data:{goodsName:$("#goodsName").val()},
            dataType:"json",
            success:function (data) {
                if(data.goodsName == "error"){
                    alert("商品名称不能为空!");
                }else if(data.goodsName == "false"){
                    alert("该商品名称已存在，不能使用！");
                }else if(data.goodsName == "true"){
                    alert("该商品名称可以使用！")
                }
            },
            error:function (data) {
                alert("请求错误！");
            }
        });
    });

});