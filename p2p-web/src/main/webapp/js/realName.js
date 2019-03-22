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

//同意实名认证协议
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

//真实姓名验证
function realNameCheck(){
	
	var realName = $.trim($("#realName").val());

	if ("" == realName){

		showError("realName","请输入真实姓名");
		return false;

	}else if (!/^[\u4e00-\u9fa5]{0,}$/.test(realName)){

		showError("realName","只支持中文姓名");
		return false;

	}

	showSuccess("realName");
	return true;
	
}

//身份证号格式验证
function idCardCheck() {
	
	var idCard = $.trim($("#idCard").val());
	
	if ("" == idCard){

		showError("idCard","请输入身份证号");
		return false;

	}else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)){

		showError("idCard","请输入正确的身份证号");
		return false;

	}

	showSuccess("idCard");
	return true;
	
}

//确认身份证号验证
function idCardEequ() {
	var idCard = $.trim($("#idCard").val());
	var replayIdCard = $.trim($("#replayIdCard").val());

	if (idCard != replayIdCard){

		showError("replayIdCard","两次输入不一致");
		return false;
	}else{

		showSuccess("replayIdCard");
		return true;

	}

}





//注册：进行实名认证
function verifyRealName(){

	var realName = $.trim($("#realName").val());
	var idCard = $.trim($("#idCard").val());

	var flag = (realNameCheck() && idCardCheck() && idCardEequ() && captchaCheck());

	if(flag){

		$.ajax({
			url:"user/verifyRealName",
			type:"post",
			data:{
				"realName":realName,
				"idCard":idCard
			},
			success:function(message){

				if("OK" == message){

					window.location.href = referrer;

				}else{

					showError("captcha",message);

				}

			},
			error:function () {

				showError("captcha","系统繁忙，请稍后重试...");

			}
		})


	}



}
