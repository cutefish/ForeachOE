<%@page import="ors.pdcl.jetty.Worker"%>
<html>
    <body><% out.println(Worker.getCounter()); %>
    </body>
</html>
