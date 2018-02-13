<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html>
<head>
	<title>Home</title>
</head>
<script>
function callback(res){
	console.log(res)
}
	var au = new AjaxUtil("${pPath}/json",null,"get","html");
	au.send(callback);
</script>
<body>
<h1>
	Hello world!  
</h1>
${dPath}, ${rPath}, ${pPath}
<P>  The time on the server is ${serverTime}. </P>
</body>
</html>
