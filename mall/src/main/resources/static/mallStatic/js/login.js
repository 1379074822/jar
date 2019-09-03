$(function() {
	//提交时验证
	$("#loginSubmit").click(function(){
        var usernameVal = $("#account").val();
        var passwordVal = $("#pwd").val();
        var codeVal = $("#verify").val();
        if (usernameVal == "" || passwordVal == "") {
            layer.msg("请输入用户名或密码！",{icon:0,time:1500});
        }else if (codeVal == "") {
            layer.msg("验证码不能为空！",{icon:2,time:1500});
        }else {
			$.ajax({
				type:"GET",
				url:"/user/login",
				data:{account:usernameVal,password:passwordVal,verify:codeVal},
				dataType:"json",
				success:function (data) {
					if(data.result == "验证码输入错误"){
                        layer.msg("验证码输入错误,请重新输入!",{icon:2,time:1500});
					}else if(data.result == "null"){
                        layer.msg("您输入的用户名或密码为空,请重新输入!",{icon:0,time:1500});
                    } else if(data.result == "forbidden"){
                        layer.msg("您的账户涉嫌违规，已被管理员禁用！",{icon:3,time:1500});
                    }else if(data.result == "error"){
                        layer.msg("该用户不存在，请去注册吧!",{icon:4,time:1500});
					} else if(data.result == "密码错误"){
                        layer.msg("密码错误，请重新输入！",{icon:2,time:1500});
					}else {
                        top.location.href="/mall/index";
					}
                },
                error:function (data) {
                    layer.msg("登录失败，请重新登录!",{icon:2,time:1500});
                }
			})
        }
	})
})