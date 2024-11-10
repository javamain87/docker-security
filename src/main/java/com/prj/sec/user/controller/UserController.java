package com.prj.sec.user.controller;

import com.prj.sec.user.entity.Users;
import com.prj.sec.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class UserController {

    private final UserService userService;

    @PostMapping("join")
    public void join(@RequestBody Users users, HttpServletRequest request, HttpServletResponse response) {
        userService.join(users,request,response);
    }

    @PostMapping("login")
    public ResponseEntity<Users> select(@RequestBody Users users, HttpServletRequest request, HttpServletResponse response) {
        Users userInfo = userService.login(users, request, response);
        return ResponseEntity.ok(userInfo);
    }
}
