<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@include file="common/tag.jsp" %>

<html lang="zh-CN">
<head>
    <%@include file="common/head.jsp" %>
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>秒杀详情页面</title>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
    <script type="text/javascript"
            src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
    <!-- <script type="text/javascript"
        src="https://cdn.bootcss.com/bootstrap-modal/2.2.6/css/bootstrap-modal-bs3patch.min.css"></script>
    </head> -->
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body ">
            <h2 class="text-danger">
                <!-- 显示time图标 -->
                <span class="glyphicon glyphicon-time"></span>
                <!-- 显示倒计时 -->
                <span class="glyphicon" id="seckill-box">开始秒杀</span>
            </h2>

        </div>
    </div>
</div>
<!-- 登录弹出层，输入电话 -->
<div id="userPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
        </div>

        <div class="modal-body">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2">
                    <input type="text" name="userPhone" id="userPhoneKey"
                           placeholder="填写手机号码" class="form-control">
                </div>
            </div>
        </div>

        <div class="modal-footer">
            <span id="userPhoneMessage" class="glyphicon"></span>
            <button type="button" id="userPhoneBtn" class="btn btn-success">
                <span class="glyphicon glyphicon-phone"></span> 提交
            </button>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=path%>/js/seckill.js"></script>

<script type="text/javascript">
    //采用jquery自动加载$(function(){});
    //初始化init，init内部存在着秒杀的逻辑
    $(function () {
        //详情页初始化
        seckill.detail.init({
            //采用jstl获取需要的数据,作为init函数的参数
            //在函数内部获取参数的形式为: param['seckillId']
            seckillId: ${seckill.seckillId},
            startTime: ${seckill.startTime.time},//毫秒
            endTime: ${seckill.endTime.time}

        });
    });

</script>

</html>