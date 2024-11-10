package com.prj.sec.common.service;

import com.prj.sec.common.utils.CommonUtils;
import com.prj.sec.user.entity.Users;
import com.prj.sec.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CommonUtils  commonUtils;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // repository 에서 유저 정보 가져오기
        Users usuerInfo = userRepository.findByEmail(email);
        //  Users 에다가 email , password , role 정보 setting 하기

        return new User(
                 usuerInfo.getEmail()
                ,usuerInfo.getPassword()
                ,commonUtils.getAuthorities(usuerInfo)
        );
    }
}
