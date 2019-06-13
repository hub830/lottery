package top.lemna.authentication.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DomainUserDetailsService implements UserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    String password = passwordEncoder.encode("123456");
    return new User(username, password, true, true, true, true,getAuthorities(username));
  }

  /** 获取用户的角色权限,为了降低实验的难度，这里去掉了根据用户名获取角色的步骤 */
  private Collection<GrantedAuthority> getAuthorities(String username) {
    List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
    if(username.equals("admin")) {
      authList.add(new SimpleGrantedAuthority("query-admin"));
    }
    authList.add(new SimpleGrantedAuthority("query-user"));
    return authList;
  }

}