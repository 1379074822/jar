$(function () {
    function testOld(){
        var pwd = $("#oldPwd").val();
        if(pwd == ""){
            $("#oldPwd").siblings("i").text("");
            return false;
        }else {
            $.ajax({
                type:"GET",
                url:"/user/checkPwd",
                data:{password:pwd},
                dataType:"json",
                success:function (data) {
                    if(data.result == "false"){
                        $("#oldPwd").siblings("i").text("密码错误,请重新输入!");
                        return false;
                    }else {
                        $("#oldPwd").siblings("i").text("密码正确!");
                        return true;
                    }
                }
            });
            return function (data) {};
        }
    }
    function testNew(){
        var reg = /^[a-zA-Z0-9]{6,18}$/;
        var value = $("#newPwd").val();
        if (!(reg.test(value))) {
            $("#newPwd").siblings("i").text("*请重新输入6位以上的密码！");
            $("#newPwd").val("");
            return false;
        } else {
            $("#newPwd").siblings("i").text("");
            return true;
        }
        if (value == "") {
            $("#newPwd").siblings("i").text("*密码不能为空！");
            return false;
        }
    }
    function testSure(){
        var pwd = $("#newPwd").val();
        var con_pwd = $("#surePwd").val();
        if (pwd != con_pwd) {
            $("#surePwd").siblings("i").text("*您输入的密码不一致！请重新输入。");
            $("#surePwd").val("");
            return false;
        } else {
            $("#surePwd").siblings("i").text("");
            return true;
        }
    }
    $("#oldPwd").blur(function () {
        testOld();
    })
    $("#newPwd").blur(function () {
        testNew();
    })
    $("#surePwd").blur(function () {
        testSure();
    })

    $("#sure").click(function () {
        if (testOld() && testNew() && testSure()) {
            $(".change_pwd form").attr("onsubmit", "return true");
            ModifyPwd();
        }else if (!testOld()){
            layer.msg("密码为空,请重新输入!",{icon:2,time:1500})
        }else if(!testNew()){
            layer.msg("新密码为空，请重新输入!",{icon:2,time:1500})
        }else {
            layer.msg("确认密码为空，请重新输入!",{icon:2,time:1500})
        }
    })

    //修改密码
    function ModifyPwd() {
        var newPwd=$("#surePwd").val();
        $.ajax({
           type:"GET",
           url:"/user/modifyPwd",
           data:{surePwd:newPwd},
           dataType:"json",
           success:function (data) {
               if(data.result == "true") {
                   alert("您已成功修改密码!去登录吧!")
                   top.location.href="/mall/login";
               }else {
                   alert("修改失败！")
               }
           }
        });
    }
})