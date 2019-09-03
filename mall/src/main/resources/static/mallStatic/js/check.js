$(function() {
    //检查昵称是否为空
    function testNickName() {
        var nickName = $("#nickName").val();
        if (nickName == "") {
            layer.msg("昵称不能为空!",{icon:2,time:1500});
            return false;
        } else {
            return true;
        }
    }
    //设置密码只含字母数字而且不少于6位数
    function testPwd() {
        var reg = /^[a-zA-Z0-9]{6,18}$/;
        var value = $("#password").val();
        if (!(reg.test(value))) {
            layer.msg("请重新输入6位以上的密码！",{icon:2,time:1500});
            $("#password").val("");
            return false;
        } else {
            return true;
        }
        if (value == "") {
            layer.msg("密码不能为空！！",{icon:2,time:1500});
            return false;
        }
    }

    //验证邮箱是否合法邮箱
    function testEmail() {
        var reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.)+(com)$|(net)$|(cn)$/;
        var value = $("#email").val();
        if (!(reg.test(value))) {
            layer.msg("请重新输入正确邮箱！！",{icon:2,time:1500});
            $("#email").val("");
            return false;
        } else {
            return true;
        }
        if (value == "") {
            layer.msg("邮箱不能为空！！",{icon:2,time:1500});
            return false;
        }
    }

    //验证手机号是否合法
    function testPhone() {
        var reg = /^1[34578]\d{9}$/;
        var value = $("#phone").val();
        if (!(reg.test(value))) {
            layer.msg("请输入正确的手机号码！！",{icon:2,time:1500});
            $("#phone").val("");
            return false;
        } else {
            return true;
        }
        if (value == "") {
            layer.msg("手机号不能为空！！",{icon:2,time:1500});
            return false;
        }
    }

    //验证密码是否一致
    function testConpwd() {
        var pwd = $("#password").val();
        var con_pwd = $("#con_password").val();
        if (pwd != con_pwd) {
            layer.msg("您输入的密码不一致！请重新输入！",{icon:2,time:1500});
            $("#con_password").val("");
            return false;
        } else {
            return true;
        }
    }

    //验证验证码是否为空
    function testCode() {
        var code = $("#code").val();
        if (code == "") {
            layer.msg("验证码为空，请输入！！",{icon:2,time:1500});
            return false;
        }else {
            return true;
		}
    }


    //检查该用户是否已经被注册
    function checkUser() {
        var account = $("#account").val();
        if (account == "") {
            layer.msg("请输入用户账户名！！",{icon:2,time:1500});
            return false;
        } else {
            $.ajax({
                type: "GET",
                url: "/user/isExitUser",
                data: {account: account},
                dataType:"json",
                async: false,
                success: function (r) {
                    if (r.result == "false") {
                        layer.msg("该账户可使用！",{icon:1,time:1500});
                        return true;
                    } else {
                        layer.msg("该用户已注册，请重新输入！",{icon:2,time:1500});
                        return false;
                    }
                }
            })
            return function (r) {};
        }
    }

    function checkUsers(){
        var account = $("#account").val();
        $.ajax({
            type: "GET",
            url: "/user/isExitUser",
            data: {account: account},
            dataType:"json",
            async: false,
            success: function (r) {
                if (r.result == "false") {
                    return true;
                } else {
                    layer.msg("该用户已注册，请重新输入！",{icon:2,time:1500});
                    return false;
                }
            }
        })
        return function (r) {};
    }
    //当input失去焦点执行
    $("#account").blur(function () {
        checkUser();
    })
    $("#nickName").blur(function () {
        testNickName();
    })
    $("#password").blur(function () {
        testPwd();
    })
    $("#email").blur(function () {
        testEmail();
    })
    $("#phone").blur(function () {
        testPhone();
    })
    $("#con_password").blur(function () {
        testConpwd();
    })
    $("#code").bind(function () {
        testCode();
    })

    $("#submitReg").click(function () {
        if (checkUsers() && testPwd() && testNickName() && testConpwd() && testEmail() && testCode() && testPhone()) {
            $(".reg_info form").attr("onsubmit", "return true");
            submitForm();
        }else if (!checkUsers() || !testPwd() || !testConpwd()){
            layer.msg("用户名或密码未输入或输入错误,请重新输入!",{icon:2,time:1500});
        }else if(!testNickName()){
            layer.msg("昵称未输入，请重新输入!",{icon:2,time:1500});
        }else if(!testEmail()){
            layer.msg("邮箱为空或输入错误，请重新输入!",{icon:2,time:1500});
        }else if(!testCode()){
            layer.msg("验证码为空或输入错误，请重新输入!",{icon:2,time:1500});
        }else {
            layer.msg("手机号输入为空或格式错误，请重新输入!",{icon:2,time:1500});
        }
    })


	//提交注册表单,保存信息至数据库
	function submitForm() {
        $.ajax({
            type: "GET",
            url: "/user/register",
            data: $("#regForm").serialize(),
            dataType:"json",
            success: function (data) {
                if(data.result == "true"){
                    alert("恭喜您注册成功，赶紧去登录吧!");
                    top.location.href = "/mall/login";
                }else if (data.result == "false") {
                    $("#imgVerify").click();
                    layer.msg("验证码错误，请重新输入！",{icon:2,time:1500});
                }else if (data.result == "error"){
                    layer.msg("您注册失败，请重新输入吧！",{icon:2,time:1500});
                }
            }
        });
    }

    //提交修改信息表单，保存信息至数据库

})

