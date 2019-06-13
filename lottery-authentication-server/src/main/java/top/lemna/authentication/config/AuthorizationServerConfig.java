package top.lemna.authentication.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private RedisConnectionFactory redisConnectionFactory;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public RedisTokenStore tokenStore() {
    return new RedisTokenStore(redisConnectionFactory);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security//
        .tokenKeyAccess("permitAll()")//
        .checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {


    // 配置两个客户端，一个用于password认证一个用于client认证
    clients.inMemory()//
        .withClient("ios2")
        // .resourceIds(Utils.RESOURCEIDS.ORDER)
        .authorizedGrantTypes("client_credentials", "refresh_token")//
        .scopes("all")//
        .authorities("oauth2")//
        .secret(passwordEncoder.encode("ios2"))//
        .and()//
        .withClient("ios")//
        // .resourceIds(Utils.RESOURCEIDS.ORDER)
        .authorizedGrantTypes("password", "refresh_token")//
        .scopes("all")//
        .authorities("oauth2")//
        .secret(passwordEncoder.encode("ios"));

  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints//
        .authenticationManager(authenticationManager)//
        .tokenStore(tokenStore());

  }

}
