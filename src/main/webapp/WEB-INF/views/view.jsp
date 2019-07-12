<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PCF 현행화</title>
<style>
table.type02 {
    border-collapse: separate;
    border-spacing: 0;
    text-align: left;
    line-height: 1.5;
    width: 80%;
    border-top: 1px solid #ccc;
    border-left: 1px solid #ccc;
  margin : 20px 10px;
}
table.type02 th {
    width: 150px;
    padding: 10px;
    font-weight: bold;
    vertical-align: top;
    border-right: 1px solid #ccc;
    border-bottom: 1px solid #ccc;
    border-top: 1px solid #fff;
    border-left: 1px solid #fff;
    background: #eee;
}
table.type02 td {
    width: 350px;
    padding: 10px;
    vertical-align: top;
    border-right: 1px solid #ccc;
    border-bottom: 1px solid #ccc;
}


body {
	width: 100%;
	margin: 0 auto;
}
</style>

</head>
<body>
<table class="type02">
	<tr>
		<th>Env</th>
		<th>Org</th>
		<th>Space</th>
		<th>App Num</th>
		<th>Instance Num</th>
	</tr>
	<c:forEach var="pcfVo" items="${records.pcfEnvResourceList}">
		<tr>
			<td rowspan="${fn:length(pcfVo.envResult)+ 2 + pcfVo.envTotalSpaceNum}">${pcfVo.env}</td>
		</tr>
		<c:forEach var="pcfOrg" items="${pcfVo.envResult}">
			<tr>
				<td rowspan="${fn:length(pcfOrg.space)+1}">${pcfOrg.org}</td>
			</tr>
			<c:forEach var="pcfSpace" items="${pcfOrg.space}">
			<tr>
				<td>${pcfSpace.space}</td>
				<td>${pcfSpace.appNums}</td>
				<td>${pcfSpace.instanceNums}</td>
			</tr>
		    </c:forEach>
		</c:forEach>
			<tr style="background: #ffb9b9">
			    <td colspan="2">${pcfVo.env} App/Instance 수  소계</td>
			    <td>${pcfVo.envTotalAppNum}</td>
			    <td>${pcfVo.envTotalInstanceNum}</td>
		    </tr>
	</c:forEach>
		<tr style="background: #7e7ba9">
		    <td colspan="3" style="color: #fff">모든 Env App/Instance 수 합계</td>
		    <td style="color: #fff">${records.allApplicationSumNum}</td>
		    <td style="color: #fff">${records.allInstanceSumNum}</td>
	    </tr>
</table>
</body>
</html>