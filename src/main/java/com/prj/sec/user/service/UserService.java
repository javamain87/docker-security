package com.prj.sec.user.service;

import com.prj.sec.user.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void join(Users users, HttpServletRequest request, HttpServletResponse response);
    Users login(Users users, HttpServletRequest request, HttpServletResponse response);
    Users findByUserEmail(String username);
}
