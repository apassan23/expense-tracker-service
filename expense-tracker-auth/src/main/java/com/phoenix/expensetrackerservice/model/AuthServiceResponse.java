package com.phoenix.expensetrackerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthServiceResponse implements Serializable {
    private String name;
    private Collection<CustomGrantedAuthority> authorities;
    private Boolean isAuthenticated;
    private Boolean isCredentialsNonExpired;
    private boolean isAccountNonExpired;
}
