<%-- 	
Marko Tallgren
a1600545
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="_style.css">
<title>VUOKRAUS</title>
</head>

<body>
<div id="wrapper">
<div id="vuokrausaika">
	<form action="UusiVuokraus" method="get" >
	VUOKRAUSAIKA: <c:out value="${vuokraus.vuokrauspvm}" /> &nbsp; - &nbsp; 
	<c:out value="${vuokraus.paattymispvm}" /> yht. <c:out value="${vali}" /> vrk
</div>
<div id="asiakastitle">
	ASIAKAS:
</div>

<div id="asiakastiedot">
	<table>
	<tr>
	<td><c:out value="${vuokraus.vuokraaja.id}" /> </td>
	<td> <c:out value="${vuokraus.vuokraaja.etunimi}" />  &nbsp;
	<c:out value="${vuokraus.vuokraaja.sukunimi}" /> </td>
	</tr>
	<tr>
	<td> </td>
	<td> <c:out value="${vuokraus.vuokraaja.osoite}" />  </td>
	</tr>
	<tr>
	<td> </td>
	<td> <c:out value="${vuokraus.vuokraaja.posti.postinro}" /> &nbsp;
	<c:out value="${vuokraus.vuokraaja.posti.postitmp}" /> </td>
	</tr>
	</table>
</div>

<div id="auto">
AUTO: 
<c:out value="${vuokraus.vuokrakohde.rekno}" />
<c:out value="${vuokraus.vuokrakohde.merkki}" />
<c:out value="${vuokraus.vuokrakohde.malli}" />
<c:out value="${vuokraus.vuokrakohde.vrkhinta}" /> eur/vrk

KOKONAISHINTA: <c:out value="${vuokraus.kokonaishinta}" /> eur
</div>

<div id="vuokramaksu">
ONKO VUOKRAUS MAKSETTU:
<input type="radio" name="maksettu" value="ON" style="min-width: 0px"><c:out value="ON"/> </input>
<input type="radio" name="maksettu" value="EI" checked style="min-width: 0px"> <c:out value="EI"/> </input>
</div>

<input type="submit" name="action" value="talleta vuokraus" />&nbsp;&nbsp;
<input type="submit" name="action" value="peruuta" />
</form>
</div>
</body>
</html>