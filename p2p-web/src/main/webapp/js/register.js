


//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});

//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}


//验证手机号
function phoneCheck(){
	
	
	var phone = $.trim($("#phone").val());
	//alert("phone");
    var flag = true;

	if("" == phone){

	    showError("phone","手机号不能为空");
	    return false;

    }else if (!/^1[1-9]\d{9}$/.test(phone)){

	    showError("phone","请输入11位的手机号行吗？");
        return false;

	}else{

	    //showSuccess("phone");

	    //去后台验证手机号是否已经注册过
	    $.ajax({

            url:"user/phoneCheck",
            type:"get",
            data:"phone="+phone,
            async:false,
            success:function (message) {

                if ("OK" == message){

                    showSuccess("phone");

                }else{

                    showError("phone",message);
                    flag = false;

                }

            },
            error:function () {

                showError("phone","系统繁忙，请稍后重试...");
                flag = false;

            }

        })

        return flag;

    }

}

//验证登录密码
function loginPasswordCheck() {

	var loginPassword = $.trim($("#loginPassword").val());
	//alert(loginPassword);
	
	if ("" == loginPassword){
		
		showError("loginPassword","请输入登录密码");
		return false;

	} else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)){

		showError("loginPassword","密码字符只可使用数字和大小写英文字母");
		return false;

	} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){

		showError("loginPassword","密码应同时包含英文或数字");
		return false;

	}else if (loginPassword.length < 6 || loginPassword.length > 20){

		showError("loginPassword","密码长度应在6-20位");
		return false;

	} else{

		showSuccess("loginPassword");
		return true;

	}

}

//确认两次密码输入是否一致
function replayLoginPasswordCheck(){

	var loginPassword = $.trim($("#loginPassword").val());
	var replayLoginPassword = $.trim($("#replayLoginPassword").val());

	if (loginPassword != replayLoginPassword){

		showError("replayLoginPassword","两次密码不一致");
		return false;

	}

	showSuccess("replayLoginPassword");
	return true;

}


//验证图片验证码
function captchaCheck() {

	var captcha = $.trim($("#captcha").val());
	var flag = true;

	if ("" == captcha) {

		showError("captcha","请输入验证码");
		flag = false;
	}else{

		$.ajax({
			url:"user/captchaCheck",
			type:"get",
			data:"captcha="+captcha,
			async:false,
			success:function (message) {

				if (!message){

					showError("captcha","验证码输入错误");
					flag = false;

				}else{

					showSuccess("captcha");

				}



			},
			error:function (message) {

				showError("captcha","系统繁忙，请稍后重试...");
				flag = false;
			}
		})

	}

	return flag;

}

//注册
function register() {

	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());

	if(phoneCheck() && loginPasswordCheck() && replayLoginPasswordCheck() && captchaCheck()){

		//文本框中放入密码的密文
		$("#loginPassword").val($.md5(loginPassword));
		$("#replayLoginPassword").val($.md5(loginPassword));


		//去后台新建用户及账户
		$.ajax({
			url:"user/register",
			type:"post",
			data:{
				"phone":phone,
				"loginPassword":$.md5(loginPassword)
			},
			success:function(data){

				if ("OK" == data.message){

					window.location.href = "realName.jsp";

				}else{

					alert(data.message);

				}

			},
			error:function () {

				alert("系统繁忙，请稍后重试...");

			}
		})

	}
}
