//存放主要交互逻辑js代码

/**
 * 使用模块化javascript
 */
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return "/seckill/time/now";
        },
        exposer: function (seckillId) {
            return "/seckill/" + seckillId + "/exposer";
        },
        execution: function (seckillId, md5) {
            return "/seckill/" + seckillId + "/" + md5 + "/execution";
        }

    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录，计时交互
            //规划系列交互流程
            //在cookies中查找手机号
            var userPhone = $.cookie("userPhone");

            if (!seckill.validatePhone(userPhone)) {
                console.log("为填写手机号码");
                var userPhoneModal = $("#userPhoneModal");
                /*
                modal的选项：
                backdrop(boolean、或者String'static')指定一个静态场景，
                当用户点击模态框外部时不会关闭模态框
                keyboard(boolean):当按下escape键时关闭模态框，设置为false时则按键失效
                show(boolean):当初始化时显示模态框
                 */
                userPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //精致位置关闭
                    keyboard: false //关闭键盘事件
                });
                /*
                $.cookie(名称,值,[option])
                 [option]参数说明：
                 expires:有限日期，可以是一个整数或一个日期(单位：天).默认关闭浏览器则丢失
                 path:cookie值保存的路径，默认与创建页路径一致
                  domin:cookie域名属性，默认与创建页域名一样。这个地方要相当注意，跨域的概念，如果要主域名二级域名有效则要设置　　".xxx.com"
                   secrue:一个布尔值，表示传输cookie值时，是否需要一个安全协议。
                 */
                $("#userPhoneBtn").click(function () {
                    console.log("提交手机号码按钮被点击");
                    var inputPhone = $("#userPhoneKey").val(); //获得输入的手机号码
                    console.log("inputPhone" + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //把手机号码写入cookie
                        $.cookie('userPhone', inputPhone, {
                            //expires : 7,//有效日期，默认为关闭浏览器则失效
                            path: '/seckill'
                        })
                        window.location.reload();
                    } else {
                        $("#userPhoneMessage").hide().html("<label class='label label-danger'>手机号码错误</label>").show(1000);
                    }
                });

            } else {
                console.log("在cookie中找到了手机号码");
                //已经登录了就开始进行计时交互
                var startTime = params['startTime'];
                var endTime = params['endTime'];
                var seckillId = params['seckillId'];
                console.log("开始秒杀时间：" + startTime + "  时间格式：" + startTime.time);
                console.log("结束秒杀时间：" + endTime);
                $.get(seckill.URL.now(), function (result) {
                    if (result && result['success']) {
                        var nowTime = result['data'];
                        console.log("服务器当前时间：" + nowTime);
                        seckill.countdown(seckillId, nowTime, startTime, endTime);
                    } else {
                        console.log("结果：" + result);
                    }
                });
            }
            ;
        }
    },
    handlerSeckill: function (seckillId, mode) {
        //获得秒杀地址
        mode.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.debug("开始进行秒杀地址获取");
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //在回调函数中，执行交互流程

            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    console.log("有秒杀地址接口");
                    //开启秒杀，获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("秒杀的地址为：" + killUrl);
                    //绑定一次点击事件,使用one，防止用户连续点击按钮，连续发送按钮请求
                    $("#killBtn").one('click', function () {
                        console.log("开始进行秒杀，按钮被禁用");
                        //执行秒杀请求，先禁用按钮
                        $(this).addClass("disabled");
                        //发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            console.info(result);
                            var killResult = result['data'];
                            console.log(killResult['seckillId']);
                            var state = killResult['state'];
                            var stateInfo = killResult['stateInfo'];
                            console.log("秒杀状态：" + stateInfo);
                            //显示秒杀结果
                            mode.html('<span class="label label-success">' + stateInfo + '</span>');
                        });
                    });
                    mode.show();
                } else {
                    console.warn("还没有暴露秒杀地址接口，无法进行秒杀");
                    //未开启秒杀
                    var now = exposer['now']; //
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log("result:" + result);
            }
        });

    },
    //倒计时交互
    countdown: function (seckillId, nowTime, startTime, endTime) {
        console.log("秒杀的商品ID:" + seckillId + ",服务器当前时间：" + nowTime + ",开始秒杀的时间:" + startTime + ",结束秒杀的时间" + endTime);
        var seckillBox = $("#seckill-box");
        //获取时间戳进行实践的比较
        nowTime = new Date(nowTime).valueOf();
        startTime = new Date(startTime).valueOf();
        endTime = new Date(endTime).valueOf();
        console.log("转换后的Date类型当前时间戳" + nowTime);
        console.log("转换后的Date类型开始时间戳" + startTime);
        console.log("转换后的Date类型结束时间戳" + endTime);
        if (nowTime < endTime && nowTime > startTime) {
            console.log("秒杀可以开始，时间条件符合");
            seckill.handlerSeckill(seckillId, seckillBox);
        }
        else if (nowTime > endTime) {
            console.log("秒杀时间已经结束");
            seckillBox.html("秒杀结束");
        } else if (nowTime < startTime) {
            console.log("秒杀还没有开始");
            //秒杀未开启,计时事件绑定
            var killTime = new Date(startTime + 1000);
            console.log(killTime);
            console.log("开始计时效果");
            seckillBox.countdown(killTime, function (event) {
                //事件格式
                var format = event.strftime('秒杀倒计时：%D天  %H时 %M分 %S秒');
                console.log(format);
                seckillBox.html(format);
                /*时间完成后回调事件*/
            }).on('finish.countdown', function () {
                console.log("准备执行回调，获取秒杀地址，执行秒杀");
                console.log("倒计时结束");
                seckill.handlerSeckill(seckillId, seckillBox);
            });
        }
    }
}