package com.baidu.fsg.uid.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baidu.fsg.uid.ApplicationContextProvider;
import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;

@Configuration
public class UidGeneratorConfig {

  @Bean
  public UidGenerator uidGenerator(WorkerIdAssigner workerIdAssigner) {
    CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
    cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner);
    return cachedUidGenerator;
  }
  
  @Bean
  ApplicationContextProvider applicationContextProvider(ApplicationContext applicationContext) {
    ApplicationContextProvider applicationContextProvider = new ApplicationContextProvider();
    applicationContextProvider.setApplicationContext(applicationContext);
    return applicationContextProvider;
  }
}
