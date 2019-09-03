function submitPay() {

	var r = confirm("是否支付？");
	var id = $(".orderId").val();
	console.log("id是" + id);
	if (r == true) {
		window.location.href = "/mall/paySuccess?id=" + id;
	}
	else {
		window.location.href = "/mall/payFailure?id=" + id;
	}
}



