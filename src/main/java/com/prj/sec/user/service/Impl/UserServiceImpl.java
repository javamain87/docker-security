package com.prj.sec.user.service.Impl;

import com.prj.sec.common.utils.CommonUtils;
import com.prj.sec.common.utils.JwtTokenProvider;
import com.prj.sec.user.entity.Users;
import com.prj.sec.user.repository.UserRepository;
import com.prj.sec.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommonUtils commonUtils;

    @Override
    public void join(Users users, HttpServletRequest request, HttpServletResponse response) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        userRepository.save(users);
    }

    @Override
    public Users login(Users users, HttpServletRequest request, HttpServletResponse response) {
        Users info = userRepository.findByEmail(users.getEmail());
        if(!ObjectUtils.isEmpty(info)) {
            if(request.getHeader("Authorization") != null) {
                String token = request.getHeader("Authorization");
                boolean result = jwtTokenProvider.validateToken(token);
            } else {
                String token = jwtTokenProvider.createToken(users.getEmail(), commonUtils.getAuthorities(users));
                response.setHeader("Authorization", token);
            }
            return info;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    @Override
    public Users findByUserEmail(String username) {
        return null;
    }
}

