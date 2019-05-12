package com.leyou.page.mq;

import com.leyou.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cq
 * @create 2018-08-01 16:13
 * @copy alibaba
 */
@Component
public class ItemListenerwwww {

    @Autowired
    private PageService pageService;

    //坚挺增加修改
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ly.page.create.queue", durable = "true"),
            exchange = @Exchange(
                    name = "ly.item.exchange",
                    type = ExchangeTypes.TOPIC,
                    //忽略声明时的一些问题避免重复声明
                    ignoreDeclarationExceptions = "true"),
            key = {"item.insert", "item.update"}

    ))
//监听增加
    public void listenCreate(Long spuId) {
        if (spuId != null) {

        pageService.createHtml(spuId);

        }
    }
    //监听删除
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ly.page.delete.queue", durable = "true"),
            exchange = @Exchange(
                    name = "ly.item.exchange", type = ExchangeTypes.TOPIC,
                    //忽略声明时的一些问题避免重复声明
                    ignoreDeclarationExceptions = "true"),
            key = "item.delete"

    ))
    //监听删除
    public void listrDelete(Long spuId){
            if (spuId != null) {

                pageService.deleteHtml(spuId);

            }

        }

    }

