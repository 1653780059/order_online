package com.example.order_online.pojo.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Classname LoginDetails
 * @Description 用户信息封装
 * @Version 1.0.0
 * @Date 2022/9/19 16:27
 * @Created by 16537
 */
@Data
public class LoginDetails implements UserDetails, Serializable {
    private TbUser user;
    private List<String> permission;
    private List<GrantedAuthority> authorities;
    private Boolean root;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities== null) {
            authorities = new ArrayList<>();
            permission.forEach((per)->{
                authorities.add(new SimpleGrantedAuthority(per));
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnable().equals(1);
    }
}
