//导航栏（商品类型）
$(function (){
	//导航下拉
	function Navdropdown(){
		var toggle=true;
		if ($(".navbar .dropdown div.dropdown_menu").hasClass("hidden")) {
			toggle=false;
		}
		$(".navbar .dropdown_btn").click(function(){
			if(toggle){
			$(".navbar .dropdown_menu").addClass("hidden");
			console.log("隐藏");
			toggle=false;
			}
			else {
				$(".navbar .dropdown_menu").removeClass("hidden");
			console.log("显示");
					toggle=true;
			}
		})
	}

	var stock = $("#stock").text();
	if(stock == 0){
        $(".increase").attr("disabled","disabled");
        $(".decline").attr("disabled","disabled");
        $("#add_cart").attr("disabled","disabled");
        $("#num").attr("readonly",true);
	}

	//产品详情/购物车购买数量
	function Buy_num(){
		var num;
		$(".buy_num button.decline").click(function(){
			num = $(this).siblings("input").val();
			num--;
			if (num<1) {
				num = 1;
			}
			$(this).siblings("input").val(num);

		})
		$(".buy_num button.increase").click(function(){
			num = $(this).siblings("input").val();
			num++;
            var stock = parseInt($("#stock").text());
            console.log(stock);
            if(num > stock){
                layer.msg("已达到库存最大值!",{icon:7,time:1500});
                num = stock;
            }
			$(this).siblings("input").val(num);
		})
	}

	//输入数量
	$("#num").blur(function () {
		var stock = parseInt($("#stock").text());
		console.log(stock);
		var num = parseInt($("#num").val());
		if(num > stock){
            layer.msg("已达到库存最大值!",{icon:7,time:1500});
			num = stock;
		}
		$("#num").val(num);
    });

   //点击加入购物车进入购物车页面
    $("#add_cart").on("click",function () {
        var goodsId = $("#goodsId").val();
        var num = $("#num").val();
        window.location.href = "/mall/shopCar?goodsId=" + goodsId + "&&num=" + num;
    });

	//放大镜切换
	function Mag_switch(){
		$(".spec-list li").mouseover(function(){
			$(this).addClass("on").siblings().removeClass("on");
			var imgsrc = $(this).children("img").attr("src");
			$("#preview img").attr("src",imgsrc)
		})
	}
	Navdropdown();
	Buy_num();
	Mag_switch();

    $("#delAddress").click(function () {
        var obj=$(this);
        window.location.href="/user/delAddress?id="+obj.attr("aid");
    });

    $(".addressState").click(function () {
		var obj = document.getElementsByName("addressInfo");
        var id='';
        for(var i=0; i<obj.length; i++){
            if(obj[i].checked){     //如果选中，将value添加到数组s中
            	id=obj[i].value;
            	break;
            }
        }
        $.ajax({
			type:"GET",
			url:"/user/updateDefaultAddress",
			data:{id:id},
			dateType:"json",
			success:function (data) {
				if(data.updateResult == "true"){
					window.location.href="/mall/address";
				}else if(data.updateResult == "false"){
                    window.location.href="/mall/address";
				}else {
                    window.location.href="/mall/address";
				}
            }
		});
    });
})

$(document).ready(function () {
    $("#imgVerify").click();
});
//验证码切换
function swichCode(obj){
	obj.src="/getVerify?"+Math.random();
	console.log(obj);
}

