package org.blezant.perfumeecommerceapp.auth.models;


import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal  implements UserDetails {

    private final RegisterEntity registerEntity;
    public  UserPrincipal(RegisterEntity registerEntity){
        this.registerEntity=registerEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(registerEntity.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return registerEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return registerEntity.getUsername();
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
        return true;
    }
}
