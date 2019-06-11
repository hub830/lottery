package top.lemna.data.uid.worker;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;


public class RedisWorkerIdAssigner implements WorkerIdAssigner {

  private final String REDIS_KEY = "baidu:uid:generator:workerid";

  @Autowired
  RedisTemplate<String, Object> template;

  public long assignWorkerId() {
    Long incr = getIncr(REDIS_KEY, getCurrent2TodayEndMillisTime());
    if (incr == 0) {
      incr = getIncr(REDIS_KEY, getCurrent2TodayEndMillisTime());// 从001开始
    }
    return incr;
  }

  public Long getIncr(String key, long liveTime) {
    RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, template.getConnectionFactory());
    Long increment = entityIdCounter.getAndIncrement();

    if ((null == increment || increment.longValue() == 0) && liveTime > 0) {// 初始设置过期时间
      entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);// 单位毫秒
    }
    return increment;
  }

  // 现在到今天结束的毫秒数
  public Long getCurrent2TodayEndMillisTime() {
    Calendar todayEnd = Calendar.getInstance();
    // Calendar.HOUR 12小时制
    // HOUR_OF_DAY 24小时制
    todayEnd.set(Calendar.HOUR_OF_DAY, 23);
    todayEnd.set(Calendar.MINUTE, 59);
    todayEnd.set(Calendar.SECOND, 59);
    todayEnd.set(Calendar.MILLISECOND, 999);
    return todayEnd.getTimeInMillis() - new Date().getTime();
  }
}
