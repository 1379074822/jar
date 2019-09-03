$(function() {
    $(".viewCategory").on("click", function () {
        var obj = $(this);
        var pid = obj.attr("cid");
        if (pid == 0) {
            window.location.href = "/system/goods/categoryList";
        } else {
            window.location.href = "/system/goods/categoryList?parent_id=" + pid;
        }
    });
    $(".editCategory").on("click", function () {
        var obj = $(this);
        var cid = obj.attr("cid");
        window.location.href = "categoryEdit?cid=" + cid;
    });
    $(".delCategory").on("click", function () {
        var obj = $(this);
        var cid = obj.attr("cid");
        var name = obj.attr("name");
        var parentId = obj.attr("parentId");
        if (parentId != null) {
            $.ajax({
                type: "GET",
                url: "checkGoods",
                data: {id: cid},
                dataType: "json",
                success: function (data) {
                    if (data.result == "true") {
                        alert("该分类下含有商品，不能删除!");
                    } else if (data.result == "false") {
                        ajaxDelete(cid, name);
                    } else {
                        ajaxDelete(cid, name);
                    }
                }
            });
        } else {
            $.ajax({
                type: "GET",
                url: "checkTwoCategory",
                data: {id: cid},
                dataType: "json",
                success: function (data) {
                    if (data.result == "true") {
                        alert("该商品分类含有二级分类,不能删除!");
                    } else if (data.result == "false") {
                        ajaxDelete(cid, name);
                    } else {
                        ajaxDelete(cid, name);
                    }
                }
            });
        }
    });

    function ajaxDelete(cid, name) {
        if (confirm("确定删除商品分类【" + name + "】吗？")) {
            $.ajax({
                type: "GET",
                url: "delCategory",
                data: {id: cid},
                dataType: "json",
                success: function (data) {
                    if (data.result == "true") {
                        alert("删除成功！");
                        window.location.href = "categoryList";
                    } else {
                        alert("删除失败!");
                    }
                }
            });
        } else {
            alert("已取消删除!");
        }
    }
})