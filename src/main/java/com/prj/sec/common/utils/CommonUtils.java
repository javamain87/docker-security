package com.prj.sec.common.utils;

import com.prj.sec.user.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonUtils {

    public Collection<? extends GrantedAuthority> getAuthorities(Users usuers) {
        String[] arrRole = usuers.getRole().split(",");
        List<String> roleList = new ArrayList<>();
        for (String role : arrRole) {
            roleList.add(role.trim());
        }
        return roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
