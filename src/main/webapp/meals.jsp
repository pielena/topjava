<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal List</title>
    <style>
        .normal {color: green;}
        .exceeded {color: red;}
    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>Meal list</h3>
    <hr>
    <table border="1", cellpadding="8", cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        </thead>
        <c:forEach var="mealTo" items="${meals}">
            <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${mealTo.excess ? 'exceeded' : 'normal'}">
                <td>
                    <%=TimeUtil.toString(mealTo.getDateTime())%>
                </td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
