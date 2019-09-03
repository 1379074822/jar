$(function () {
    //动态加载一级分类列表
    $.ajax({
        type: "GET", //请求类型
        url: "/system/goods/categoryList.json", //请求url
        data: {pid: null}, //请求参数
        dataType: "json",  //ajax接口(请求url)返回的数据类型
        success: function (data) {
            $("#parentId").html();
            var options = "<option value=\""+0+"\">无上级分类</option>";
            for (var i = 0; i < data.length; i++) {
                options += "<option value=\"" + data[i].id + "\">" + data[i].categoryName + "</option>";
            }
            $("#parentId").html(options);
        },
        error: function (data) {
            alert("动态加载一级列表失败!");
        }
    });

    $("#back").click(function () {
        window.location.href="categoryList";
    });
});