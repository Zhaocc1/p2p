<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/2 0002
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>web工程向pay工程传递参数</title>
</head>
<body>

    <form action="${p2p_pay_alipay_url}" method="post" >
        <input type="hidden" id="out_trade_no" name="out_trade_no" value="${out_trade_no}">
        <input type="hidden" id="body" name="body" value="${body}">
        <input type="hidden" id="total_amount" name="total_amount" value="${total_amount}">
        <input type="hidden" id="subject" name="subject" value="${subject}">
    </form>
    <script>document.forms[0].submit();</script>
</body>
</html>
