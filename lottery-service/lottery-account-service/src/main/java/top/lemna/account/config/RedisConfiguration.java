package top.lemna.account.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {


  /**
   * retemplate相关配置
   * 
   * @param factory
   * @return
   */
  @Bean
  public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {

    RedisTemplate<?, ?> template = new RedisTemplate<>();
    // 配置连接工厂
    template.setConnectionFactory(factory);

    FastJsonRedisSerializer<?> serializer = new FastJsonRedisSerializer<>(Object.class);

    // 值采用json序列化
    template.setValueSerializer(serializer);
    // 使用StringRedisSerializer来序列化和反序列化redis的key值
    template.setKeySerializer(new StringRedisSerializer());

    // 设置hash key 和value序列化模式
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();

    return template;
  }


}
