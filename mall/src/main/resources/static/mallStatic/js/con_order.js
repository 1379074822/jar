$(function(){
	//会员积分
	var point = $("#myPoint").text();

	//总金额
    var money = $("#money").text();
    $(".payment").text(money);
    $("#point").text(money);
	//是否满足折扣条件，满1000积分可折扣
	if(point < 1000){
        $(".pointRadio").prop('disabled',true);
        //折扣后的支付价格
        $(".payment").text(money);
	}else {
        $(".pointRadio").removeAttr('disabled');
	}

	//是否选择折扣
	$(".pointRadio").click(function () {
		var obj = document.getElementsByName('pointRadio');
		var point = $("#myPoint").text();
		var pointProportion = $(".pointProportion").val();
        //折扣价格
		var discount  = Math.floor(point/1000)*pointProportion;
		console.log(discount);
		for(var i=0;i<obj.length;i++) {
			if(obj[i].checked){
				if(obj[i].value == 'yes'){
					if(discount > money){
						var newDiscount = parseInt(parseInt(money)/pointProportion)*pointProportion;
                        $("#discount").text(newDiscount);
                        //折扣后的支付价格
                        $(".payment").text(money-newDiscount);
                        $("#point").text(money-newDiscount);
					}else {
                        $("#discount").text(discount);
                        //折扣后的支付价格
                        $(".payment").text(money-discount);
                        $("#point").text(money-discount);
					}
				}else {
                    $("#discount").text('0');
                    $(".payment").text(money);
                    $("#point").text(money);
				}
            }
        }
    });

   function receiveAddress() {
        var obj = document.getElementsByName("receiveAddress");
        var id=0;
        for(var i=0; i<obj.length; i++){
            if(obj[i].checked){     //如果选中，将value添加到数组s中
                id=obj[i].value;
                break;
            }
        }
        return id;
    }

    //点击提交订单
	$("#submitOrder").click(function () {
		var aid = 0;  //收获地址的主键id
		if(receiveAddress() != 0){
			aid = receiveAddress();
		}
		console.log("地址id是;"+aid);
		var money = $("#money").text(); //总金额(同时也是下订单可获的积分),
		var discount = $("#discount").text();  //积分折扣,实付金额可相减获得
		var ids = $(".ids").val(); //所购买商品的id集合
		console.log(ids);
		$.ajax({
			type:"GET",
			url:"/mall/submitOrder",
			data:{aid:aid,money:money,discount:discount,ids:ids},
			dataType:"json",
			success:function (data) {
				if(data.result == "true"){
					window.location.href="/mall/pay?id="+data.id;
				}else if(data.result == "false"){
                    layer.msg("收货地址为空，请选择收货地址!",{icon:2,time:1500});
				}else {
                    layer.msg("商品【"+data.goods+"】库存为"+data.stock+"，不足请重新购买!",{icon:1,time:1500})
				}
            }
		})
    });

});