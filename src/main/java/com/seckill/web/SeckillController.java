package com.seckill.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeaKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;

import javax.swing.text.html.HTMLDocument;


@Controller // 类似与@Service和@Component
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分
public class SeckillController {

    //	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    public SeckillController() {

    }

    @Autowired
    private SeckillService seckillService;

    /**
     * 进入秒杀列表
     *
     * @param model 模型数据，存放秒杀商品的信息
     * @return 秒杀列表详情页面
     */
    @RequestMapping(value = "/{showPage}/list", method = RequestMethod.GET)
    public String list(@PathVariable("showPage") int showPage, Model model) {

        System.out.println("经过controler来到list界面");

        List<Seckill> seckillList = seckillService.getSeckillList(showPage);
        int pageCount = seckillService.getPageCount();
        model.addAttribute("list", seckillList);
        model.addAttribute("pageCount", pageCount);

        return "list"; // WEN-INF/list.jsp

    }

    @RequestMapping(value = "/{showPage}/listPage", method = RequestMethod.GET)
    @ResponseBody
    public List<Seckill> pageList(@PathVariable("showPage") int showPage, Model model) {

        List<Seckill> seckillList = seckillService.getSeckillList(showPage);
        Iterator iterator = seckillList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("=======================");
        model.addAttribute("list", seckillList);
        return seckillList; // WEN-INF/list.jsp

    }

    /**
     * 根据id搜索商品，展示商品
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 暴露秒杀的接口的方法
     *
     * @param seckillId
     * @return 根据用户秒杀的商品id进行业务逻辑判断，返回不同的json实体结果
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST)
    @ResponseBody // 告诉springmvc，返回的类型作为ajax输出
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {

//			logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 用户执行秒杀，在页面点击相关的秒杀连接，进入后获取对应的参数进行判断 返回响应的json实体结果，前端在进行处理
     *
     * @param seckillId 秒杀的商品，对应的秒杀时的id
     * @param md5       一个被盐值加密过的md5加密值
     * @param userPhone 参与秒杀用户的手机号码，当做账号密码使用
     * @return 参与秒杀的结果，为json数据
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") long seckillId,
                                                   @PathVariable("md5") String md5, @CookieValue(value = "userPhone", required = false) Long userPhone) {

        // 如果用户的手机号码为空，说明用户没有完成注册
        if (userPhone == null) {
            return new SeckillResult<>(false, "没有注册");
        }
        try {
            // 这里换成储存过程
            //SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            return new SeckillResult<>(true, execution);
        } catch (RepeaKillException e1) {
            // 重复秒杀
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(false, execution);
        } catch (SeckillCloseException e2) {
            // 秒杀关闭
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(false, execution);
        } catch (SeckillException e) {
            // 不能判断的异常
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(false, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Date> time() {
        Date date = new Date();
        return new SeckillResult<>(true, date);
    }
}
