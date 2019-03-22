<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/2 0002
  Time: 19:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>pay向p2p返回同步请求参数</title>
</head>
<body>
    <form action="${pay_p2p_return_url}" method="post">
        <input type="hidden" name="signVerified" value="${signVerified}">
        <c:if test="${not empty params}">
            <c:forEach items="${params}" var="p">
                <input type="hidden" name="${p.key}" value="${p.value}" >
            </c:forEach>
        </c:if>
    </form>
    <script>document.forms[0].submit()</script>
</body>
</html>
