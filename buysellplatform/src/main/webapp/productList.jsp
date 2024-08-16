<%@ page import="com.buysellplatform.model.Product" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Listing</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Product Listing</h2>
        <c:if test="${not empty products}">
            <ul>
                <c:forEach var="product" items="${products}">
                    <li>
                        <a href="product-details?id=${product.id}">
                            <img src="${product.imageUrl}" alt="${product.title}" width="100px">
                            <h3>${product.title}</h3>
                            <p>Current Bid: $${product.currentBidPrice}</p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <a href="sell.jsp"><button>I want to sell</button></a>
    </div>
</body>
</html>
