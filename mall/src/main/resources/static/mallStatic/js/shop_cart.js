$(function() {

    var goodsName = $(".goodsName").val();
    var stock = $(".stock").val();
    if(stock > 0){
        layer.msg("商品【"+goodsName+"】库存为"+stock+"不足，请重新购买!",{icon:0,time:2000});
    }else if(goodsName != '' && goodsName != null){
        layer.msg("商品【"+goodsName+"】已下架，无法购买!",{icon:0,time:2000});
    }

    //点击全选按钮
    $("#all_tick").click(function () {
        allTick();
    });

    function allTick() {
        var ob = document.getElementById('all_tick');
        if (ob.checked) {
            $(".checkTick").prop('checked', true);
            change();
        } else {
            $(".checkTick").removeAttr('checked');
            $("#count").text(0);
            $("#total").text(0);
        }
    }

    //删除所选商品
    $("#del_sel").click(function () {
        deleteAll();
    });

    function deleteAll() {
        var ids = '';
        var obj = document.getElementsByName('check'); //选择所有name="'check'"的对象，返回数组
        //取到对象数组后，我们来循环检测它是不是被选中
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) { //如果选中，将value添加到数组s中
                ids += obj[i].value + ',';
            }
        }
        if (ids == null || ids.length == 0) {
            alert("你未选择商品!");
            return;
        }if (confirm('确认要删除选中商品吗?')) {
            $.ajax({
                type: 'GET',
                url: '/mall/delGoodsByIds',
                data: {'ids': ids},
                traditional: true,
                dataType: 'json',
                success: function (data) {
                    if (data.result == 'true') {
                        window.location.href = "/mall/viewShopCar";
                    } else {
                        alert('删除失败!');
                    }
                }
            });
        }
    }

    //点击“-”号按钮
    $(".decline").click(function () {
        var num = parseInt($(this).parent("td").find(".num").val());
        var id = $(this).parent("td").find("#shopCarId").val();
        if(num == 1){
            layer.msg("该宝贝不能再减少啦！",{icon:2,time:1500});
        }else {
            update(num - 1, id);
        }
    });
    //点击“+”号按钮
    $(".increase").click(function () {
        var num = parseInt($(this).parent("td").find(".num").val());
        var id = $(this).parent("td").find("#shopCarId").val();
        update(num + 1, id);
    });

    //自选数量
    $(".num").blur(function () {
        var id = $(this).parent("td").find("#shopCarId").val();
        var num = parseInt($(this).parent("td").find(".num").val());
        update(num,id);
    });


    $(".checkTick").click(function () {
        change();
    });

    function change() {
        var allPrice = 0;
        var allNum = 0;
        var obj = document.getElementsByName('check'); //选择所有name="'check'"的对象，返回数组
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                allNum++;
                allPrice += parseFloat(obj[i].parentNode.firstElementChild.value);
            }
        }
        $("#count").text(allNum);
        $("#total").text(allPrice);
    }

    $("#cartPay").click(function () {
        var count = $("#count").text();
        var total = $("#total").text();
        var ids = '';
        if (count > 0) {
            window.location.href = "/mall/confirmOrder?count=" + count + "&&total=" + total + "&&ids=" + checkObj(ids);
        } else {
            layer.msg("请选择商品结算!",{icon:3,time:1500});
        }
    });

    function checkObj(ids) {
        var obj = document.getElementsByName('check'); //选择所有name="'check'"的对象，返回数组
        //取到对象数组后，我们来循环检测它是不是被选中
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) { //如果选中，将value添加到数组s中
                ids += obj[i].value + ',';
            }
        }
        return ids;
    }
})
function update(num, id) {
    $.ajax({
        type: "GET",
        url: "/mall/updateNum",
        data: {num: num, id: id},
        dataType: "json",
        success: function (data) {
            if (data.result == "true") {
                window.location.href = "/mall/viewShopCar";
            } else if (data.result == "false") {
                alert("该宝贝不能再减少啦！");
                window.location.href = "/mall/viewShopCar";
            } else {
                alert("商品库存为"+data.number+"，无法增加!");
                window.location.href = "/mall/viewShopCar";
            }
        }
    });
}


