<%-- 	
Marko Tallgren
a1600545
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>
<script src="_script.js"></script>
<link rel="stylesheet" type="text/css" href="_style.css">
<title>VUOKRAUKSET</title>
</head>
<body>
<div id="wrapper">
	<div id ="vuokraukset">
		<div id="title">
			<h1>Autojen vuokraukset</h1>
		</div>
		<form action="VuokrausOhjelma" method="get">
		<div id="haekaikki">
			<input type="submit" name="action" value="hae kaikki vuokraukset"/>
		</div>
		<div id="haetietty">
			<input type="submit" name="action" value="hae tietyn pvm:n vuokraukset"/>
		</div>
		<div id="valitsepvm">
			<input type="text" name="date" class="date"	placeholder="Valitse päivä" spellcheck="false"/>
		</div>
	
	<div id="valitus">
		<c:if test="${tyhja==true}" >
		<c:out value="${date}" /> <c:out value=" ei löytynyt yhtään vuokrausta" />
		</c:if>
		<c:if test="${EI_LOYDY==true}" >
		<c:out value=" Tietokannassa ei ole yhtään autojen vuokrauksia" />
		</c:if>
		<c:if test="${TK_VIRHE==true}" >
		<c:out value="Tietokantaan ei nyt saada yhteyttä. Korjaamme vian tuotapikaa" />
		</c:if>
	</div>
	
		</form>
	</div>
	
	<div id="vuokrausjakirjaudu">
		<form action="UusiVuokraus" method="get" >
		<input type="submit" name="action" value="tee uusi vuokraus" /> &nbsp; &nbsp;
		<c:if test="${LISAYSONNISTUI== true}" >
		 <c:out value="Vuokrauksen lisäys onnistui" />
		</c:if>
		<c:if test="${LISAYSEPAONNISTUI== true}" >
		 <c:out value="Vuokrauksen lisäys EPÄONNISTUI" />
		</c:if>
		</form>
		<form action="kirjaudu_ulos" method="get">
		<input type="submit" name="action" value="Kirjaudu ulos" />
		</form>
	</div>
</div>

</body>
</html>