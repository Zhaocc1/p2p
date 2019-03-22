<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>动力金融网-CFCA认证的互联网金融公司</title>
    <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="../js/trafficStatistics.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/style.css" />
    <link rel="stylesheet" type="text/css" href="../css/share.css" />
    <link rel="stylesheet" type="text/css" href="../css/main.css" />

</head>

<body>
<div id="header">
    <jsp:include page="../commons/header.jsp"/>
</div>

<!--散标投资 begin-->
<div id="sbtz" class="invest-details">

    <!--页中begin-->
    <div class="mainBox pro-details-body">
        <div class="homeWap clearfix" id="huacengPar">
            <div class="pro-details-left">

                <!-- 产品详情start -->
                <div class="pro-info-details">
                    <div class="pro-name">
                        <h2><span>${loanInfo.productName}</span>(${loanInfo.productNo})</h2>
                    </div>
                    <div class="pro-info">
                        <ul class="clearfix">
                            <li class="info-1">
                                <p>历史年化利率</p>
                                <h3>${loanInfo.rate}<span>%</span></h3>
                                <div class="info-bt">
                                    <span>本产品采用普通利率</span>
                                </div>
                            </li>
                            <li class="info-2">
                                <p>募集金额(元)</p>
                                <h3>${loanInfo.productMoney}</h3>
                                <div class="info-bt">
                                    <span>
                                        <c:choose>

                                            <c:when test="${loanInfo.leftProductMoney eq 0}">
                                                已满标
                                            </c:when>
                                            <c:otherwise>
                                                募集中 | 剩余募集金额 ${loanInfo.leftProductMoney}元
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </li>

                            <li class="info-3">
                                <p>投资周期</p>

                                <c:choose>
                                    <c:when test="${loanInfo.productType eq 0}">
                                        <h3>${loanInfo.cycle}<span>天</span></h3>
                                    </c:when>

                                    <c:otherwise>
                                        <h3>${loanInfo.cycle}<span>个月</span></h3>
                                    </c:otherwise>
                                </c:choose>

                                <div class="info-bt"><span></span></div>
                            </li>
                        </ul>
                    </div>

                    <dl class="pro-syfs">
                        <dt><span>收益获取方式</span></dt>
                        <dd><span>收益返还：</span>到期还本付息</dd>
                    </dl>
                </div>
                <!-- 产品详情end -->

                <!-- 投资记录start -->
                <div class="pro-details-cnt">
                    <ul class="tabNav clearfix">
                        <li><a id="one3" href="javascript:void(0);" class="s">投资记录</a></li>
                    </ul>

                    <div class="invest-record" id="con_one_3" style="display:block">
                        <div class="vertical-side">投资列表</div>
                        <dl class="record-list">
                            <dt>
                                <span class="record-num">序号</span><span class="invest-user">投资人</span><span class="invest-money">投资金额(元)</span><span class="invest-time">投资时间</span>
                            </dt>

                            <c:choose>
                                <c:when test="${empty bidInfoList}">
                                    <dd style="text-align:center;">该产品暂时还没有人投资，赶快去投资吧~</dd>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${bidInfoList}" var="bidInfo" varStatus="index">
                                        <dd>
                                            <span class="record-num">${index.count}</span>
                                            <span class="invest-user">
                                                    ${fn:substring(bidInfo.user.phone,0,3)}******${fn:substring(bidInfo.user.phone,9,11)}
                                            </span>
                                            <span class="invest-money">${bidInfo.bidMoney}</span>
                                            <span class="invest-time">
                                                <fmt:formatDate value="${bidInfo.bidTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                            </span>
                                        </dd>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
                        </dl>
                    </div>
                </div>
                <!-- 投资记录end -->
            </div>

            <!--页面右侧begin-->
            <div class="pro-details-right">
                <div class="right-calculator" id="huaceng">
                    <div class="calculator-cnt">
                        <h2>立即投资</h2>
                        <dl class="profits-mode">
                            <dt>收益获取方式</dt>
                            <dd class="clearfix"><span id="fanhuan"><em>到期还本付息</em></span></dd>
                        </dl>
                        <dl class="usable">
                            <dt>我的账户可用</dt>
                            <dd>资金(元)：

                                <c:choose>
                                    <c:when test="${empty user}">
                                        <span style="font-size:18px;color:#ff6161;vertical-align:bottom;"><a href="${pageContext.request.contextPath}/login.jsp">请登录</a></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="font-size:18px;color:#ff6161;vertical-align:bottom;">${financeAccount.availableMoney}</span>
                                    </c:otherwise>
                                </c:choose>

                            </dd>
                        </dl>
                        <div class="expect-box">
                            <div class="expect-money">预计本息收入(元)：<span id="shouyi" class="money"></span><span class="prompt" style="display:block;">请在下方输入投资金额</span></div>
                            <input type="text" id="bidMoney" name="bidMoney" onblur="checkMoney();" placeholder="请输入投资金额，应为100元的整倍数" maxlength="9"/>
                            <div class="max-invest-money"></div>
                        </div>
                        <div class="invest-btn">
                            <a id="investNow" href="javascript:void(0)" class="btn-1" onclick="invest();">立即投资</a>
                        </div>
                        <input type="hidden" id="loanId" name="loanId" value="${loanInfo.id}"/>
                    </div>
                </div>
            </div>
            <!--页面右侧end-->
        </div>
    </div>
    <!--页中end-->

</div>
<!--散标投资 end-->

<!--遮罩层-->
<div class="dialog-overlay" id="dialog-overlay1" style="display:none;"></div>

<!--投资成功浮层start-->
<div class="layer-body failureSuccess failurePayment" id="failurePayment" style="display:none;width:500px;height:100px;top:75%;">
    <a class="layer-close" href="javascript:closeit();"></a>
    <div style="background:#f2f2f2; line-height:105px;text-align:center;"><font style="font-size:25px;">投资成功</font></div>
</div>
<!--投资成功浮层end-->

<!--页脚start-->
<jsp:include page="../commons/footer.jsp"/>
<!--页脚end-->

<script type="text/javascript">
    function closeit() {
        $("#failurePayment").hide();
        $("#dialog-overlay1").hide();
        window.location.href="${pageContext.request.contextPath}/loan/myCenter";
    }

    //检查输入金额是否合理并计算到期本息收入
    function checkMoney() {
        var bidMoney = $.trim($("#bidMoney").val());
        var productType = "${loanInfo.productType}";
        var rate = "${loanInfo.rate}";
        var cycle = "${loanInfo.cycle}";
        var expectedIncome = "";


        if ("" == bidMoney){

            $(".max-invest-money").html("请输入投资金额");
            return false;

        }else if (isNaN(bidMoney)){

            $(".max-invest-money").html("请输入正确的投资金额");
            return false;

        } else if(bidMoney <= 0){

            $(".max-invest-money").html("请输入合理的投资金额");
            return false;

        }else if( bidMoney % 100 != 0){

            $(".max-invest-money").html("投资金额应为100的整数倍");
            return false;

        }else{
            $(".max-invest-money").html("");
            if (0 == productType){

                expectedIncome = bidMoney * (1 + rate / 100 / 365 * cycle);

            }else{

                expectedIncome = bidMoney *(1 +  rate / 100 / 12 * cycle );

            }

            expectedIncome = Math.round(expectedIncome*Math.pow(10,2))/100;
            $("#shouyi").html(expectedIncome);
            return true;

        }

    }

    //投资按钮：判断投资金额，投资
    function invest() {

        var bidMoney = $.trim($("#bidMoney").val());
        var loanId = $("#loanId").val();
        var user = "${user}";
        var name = "${user.name}";

        var availableMoney = "${financeAccount.availableMoney}";
        var leftProductMoney = "${loanInfo.leftProductMoney}";
        var bidMinLimit = "${loanInfo.bidMinLimit}";
        var bidMaxLimit ="${loanInfo.bidMaxLimit}";

        if (checkMoney()){

            //判断是否登录
            if ("" == user) {
                if (confirm("您尚未登录，是否立即登录？")) {
                    window.location.href = "${pageContext.request.contextPath}/login.jsp";
                }

                //判断是否实名认证
            }else if ("" == name){
                if (confirm("您尚未进行实名认证，是否立即认证？")){
                    window.location.href = "${pageContext.request.contextPath}/realName.jsp"
                }

                //判断投资金额是否高于可用金额
            }else if (parseFloat(bidMoney) > parseFloat(availableMoney)){

                $(".max-invest-money").html("可用金额不足");

            //判断投资金额是否低于剩余可投资金额
            }else if (parseFloat(bidMoney) > parseFloat(leftProductMoney)){

                $(".max-invest-money").html("剩余可投资金额不足");

            //判断投资金额是否高于最低投资金额
            }else if (parseFloat(bidMoney) < parseFloat(bidMinLimit)){

                $(".max-invest-money").html("投资金额不能低于"+bidMinLimit +"元");

            //判断投资金额是否低于最高投资限额
            }else if (parseFloat(bidMoney) > parseFloat(bidMaxLimit)){

                $(".max-invest-money").html("投资金额不能高于"+bidMaxLimit+"元");

            //向后台发送投资请求
            }else{

                $.ajax({

                    url:"${pageContext.request.contextPath}/loan/invest",
                    type:"post",
                    data:{
                       "bidMoney":bidMoney,
                       "loanId":loanId
                    },
                    success:function(message){

                        if ("OK" == message){

                            $("#failurePayment").show();
                            $("#dialog-overlay1").show();

                        } else{

                            $(".max-invest-money").html("系统繁忙，请稍后重试");

                        }

                    },
                    error:function(){
                        alert("11111111");
                        $(".max-invest-money").html("系统繁忙，请稍后重试...");
                    }

                 })

            }

        }


    }
</script>
</body>
</html>