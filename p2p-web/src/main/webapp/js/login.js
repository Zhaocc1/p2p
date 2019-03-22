var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

//初始化登录页：获取三个参数
function initLoginPage() {

	$.ajax({
		url:"loan/initLoginPage",
		type:"get",
		success:function(jsonObject){

			$("#historicalAverageRate").html(jsonObject.historicalAverageRate);
			$("#userCount").html(jsonObject.userCount);
			$("#bidMoney").html(jsonObject.bidMoney);

		}
	})

}

//手机号格式验证
function phoneCheck() {

	var phone = $.trim($("#phone").val());

	if ("" == phone){

		$("#showId").html("请输入手机号");
        return false;

	}else if(!/^1[1-9]\d{9}$/.test(phone)){

		$("#showId").html("手机号格式不正确");
        return false;

	}else{

		$("#showId").html("");
        return true;

	}
}

//密码输入验证
function loginPasswordCheck() {
	var loginPassword = $.trim($("#loginPassword").val());
	if ("" == loginPassword){

		$("#showId").html("请输入密码");
		return false;

	}else{

		$("#showId").html("");
		return true;

	}

}

//短信验证码验证
function checkMessageCode(){

	var messageCode = $.trim($("#messageCode").val());
	if (""==messageCode){

		$("#showId").html("请输入验证码");
		return false;

	}

    $("#showId").html("");
    return true;


}


//获取短信验证码
function getMessageCode(){

	var phone = $.trim($("#phone").val());

	var _this=$(this);

	if ("" != phone) {
		$.ajax({
			url: "loan/messageCode",
			type: "get",
			data: "phone=" + phone,
			success: function (jsonObject) {
				if (jsonObject.errorMessage == "OK") {
					alert("您手机收到的短信验证码是:" + jsonObject.messageCode);
					if (!$(this).hasClass("on")) {
						$.leftTime(60, function (d) {
							if (d.status) {
								_this.addClass("on");
								_this.html((d.s == "00" ? "60" : d.s) + "秒后重新获取");
							} else {
								_this.removeClass("on");
								_this.html("获取验证码");
							}
						});
					}
				} else {
					showError("message", "请稍后重试...");
				}

			}
		});

	} else {
		$("#showId").html("请输入手机号码");
	}

}


//用户登录
function login() {

    var phone = $.trim($("#phone").val());
    var loginPassword = $.trim($("#loginPassword").val());
    var messageCode = $.trim($("#messageCode").val());

    //alert(phoneCheck()&&loginPasswordCheck()&&checkMessageCode());

    if(phoneCheck()&&loginPasswordCheck()&&checkMessageCode()){


        //将密码转换为密文,放入密码框
        loginPassword = $.md5(loginPassword);
        $("#loginPassword").val(loginPassword);


        $.ajax({
            url:"user/login",
            type:"post",
            data:{
                "phone":phone,
                "loginPassword":loginPassword,
                "messageCode":messageCode
            },
            success:function(jsonObject){

                //登录成功跳转到上一个页面
                if ("OK"==jsonObject.errorMessage){

                    window.location.href=referrer;

                }else{

                    $("#showId").html(jsonObject.errorMessage);

                }

            },
            error:function () {
                $("#showId").html("系统繁忙，请稍后重试...");
            }
        })

    }



}