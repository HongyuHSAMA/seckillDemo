<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 检测设备终端的宽度和高度 -->
<meta charset="utf-8">

<link rel="stylesheet" href="<%=path%>/css/bootstrap.min.css"
      type="text/css">
<link rel="stylesheet" href="<%=path%>/css/bootstrap-theme.min.css"
      type="text/css">
<link rel="stylesheet" href="<%=path%>/css/style.css" type="text/css">