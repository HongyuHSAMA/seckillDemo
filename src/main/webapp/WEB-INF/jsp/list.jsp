<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@include file="common/tag.jsp" %>

<html lang="zh-CN">
<head>
    <%@include file="common/head.jsp" %>
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>秒杀列表页面</title>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.paginate.js"></script>
    <script type="text/javascript" src="<%=path%>/js/bootstrap.min.js"></script>

    <style>
        .demo {
            width: 580px;
            padding: 10px;
            margin: 10px auto;
            border: 1px solid #fff;
            background-color: #f7f7f7;
        }
    </style>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading text-center">
        <h2>秒杀列表</h2>
    </div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>名称</th>
                <th>库存</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>创建时间</th>
                <th>详情页</th>
            </tr>
            </thead>
            <tbody>
            <div id="listTable">
                <c:forEach items="${list}" var="sk" varStatus="skStatus">
                    <tr>
                        <td id="name${skStatus.index}">${sk.name}</td>
                        <td id="number${skStatus.index}">${sk.number}</td>
                        <td id="startTime${skStatus.index}"><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td id="endTime${skStatus.index}"><fmt:formatDate value="${sk.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td id="createTime${skStatus.index}"><fmt:formatDate value="${sk.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a id="seckillHref${skStatus.index}" class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">link</a></td>
                    </tr>
                </c:forEach>
            </div>
            </tbody>
        </table>
        <div id="paginationdemo" class="demo">
            <div id="demo4"></div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function () {

        $("#demo4").paginate({
            count: ${pageCount},
            start: 1,
            display: 5,
            border: false,
            text_color: '#79B5E3',
            background_color: 'none',
            text_hover_color: '#2573AF',
            background_hover_color: 'none',
            images: false,
            mouse: 'press',
            onChange: function (page) {
                //$('._current','#paginationdemo').removeClass('_current').hide();
                //$('#p'+page).addClass('_current').show();
                $('._current', '#paginationdemo').text("page" + page);

                //此处使用ajax.post方法进行访问
                $.get("/seckill/" + page + "/listPage", {}, function (result) {
                    var temp;
                    for (var i = 4; i >= 0; i--) {
                        temp = result[i];
                        if (temp != null) {
                            var startTime = new Date(temp['startTime']);
                            var endTime = new Date(temp['endTime']);
                            var createTime = new Date(temp['createTime']);
                            var seckillId = temp['seckillId'];
                            console.log(seckillId);
                            $('#name' + i).text(temp['name']);
                            $('#number' + i).text(temp['number']);
                            $('#startTime' + i).text(startTime.toLocaleString());
                            $('#endTime' + i).text(endTime.toLocaleString());
                            $('#createTime' + i).text(createTime.toLocaleString());
                            document.getElementById("seckillHref"+i).setAttribute("href", "/seckill/" + seckillId + "/detail");
                        } else {
                            $('#name' + i).text(null);
                            $('#number' + i).text(null);
                            $('#startTime' + i).text(null);
                            $('#endTime' + i).text(null);
                            $('#createTime' + i).text(null);
                        }
                    }
                });
            }
        });
    });
</script>
</body>
</html>