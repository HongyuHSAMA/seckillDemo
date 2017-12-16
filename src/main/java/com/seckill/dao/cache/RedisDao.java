package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.seckill.entity.Seckill;

import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * redis缓存数据的操作，set、get（序列化和反序列化）
 */
public class RedisDao {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    //用来实现连接redis客户端的池子
    private final JedisPool jedisPool;
    //通过对象的类构建对应的scheme
    private Schema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    //构造函数，用于spring的bean构造函数注入
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 根据id，从redis中获取反序列化后的value数据
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(long seckillId) {
        //redis操作业务逻辑
        //此处getResource和数据库连接池中类似，获取其中的一条连接
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "seckill:" + seckillId;
            //并没有实现内部序列化操作
            //get -> byte[] 字节数组 ->反序列化 > Object(Seckill)
            //采用自定义的方式序列化
            //缓存获取到
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                //字节数组不为空，创建一个可以用来存放反序列化的空对象
                Seckill seckill = schema.newMessage();
                //反序列化
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                //seckill被反序列化
                return seckill;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据id，将以特定的key格式以及序列化后的value，存入redis中
     * @param seckill
     * @return
     */
    public String putSeckill(Seckill seckill) {
        //set Object(Seckill) ->序列化 -> byte{}
        Jedis jedis = jedisPool.getResource();
        String key = "seckill:" + seckill.getSeckillId();
        LinkedBuffer linkedBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, linkedBuffer);
        //超时缓存
        int timeout = 60 * 60;
        return jedis.setex(key.getBytes(), timeout, bytes);
    }

}
