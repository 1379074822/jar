$(function() {

    //检查昵称是否为空
    function testNickName1() {
        var nickName = $("#nick").val();
        if (nickName == "") {
            return false;
        } else {
            return true;
        }
    }
    //验证手机号是否合法
    function testPhone1() {
        var reg = /^1[34578]\d{9}$/;
        var value = $("#phoneNumber").val();
        if (!(reg.test(value))) {
            return false;
        } else {
            return true;
        }
        if (value == "") {
            return false;
        }
    }
    //验证邮箱是否合法邮箱
    function testEmail1() {
        var reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.)+(com)$|(net)$|(cn)$/;
        var value = $("#emailCode").val();
        if (!(reg.test(value))) {
            return false;
        } else {
            return true;
        }
        if (value == "") {
            return false;
        }
    }

    $("#nick").blur(function () {
        testNickName1();
    })
    $("#phoneNumber").blur(function () {
        testPhone1();
    })
    $("#emailCode").blur(function () {
        testEmail1();
    })

    $("#sureModify").click(function () {
        if(testEmail1() && testNickName1() && testPhone1()) {
            $("#ModifyForm").attr("onsubmit", "return true");
            //alert("您已注册成功!");
            submitModifyForm();
        }
    });
    //提交修改信息表单，保存信息至数据库
    function submitModifyForm() {
        $.ajax({
            type: "GET",
            url: "/user/modify",
            data: $("#ModifyForm").serialize(),
            dataType:"json",
            success: function (data) {
                if(data.result == "success"){
                    top.location.href = "/user/userCenter";
                }else if (data.result == "failed"){
                }
            }
        });
    }
})