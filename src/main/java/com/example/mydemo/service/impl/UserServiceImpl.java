package com.example.mydemo.service.impl;

import com.example.mydemo.common.service.impl.BaseServiceImpl;
import com.example.mydemo.pojo.User;
import com.example.mydemo.mapper.UserMapper;
import com.example.mydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "java.lang.Exception")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
}
