package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.PasswordUtil;
import com.example.springboot.entity.Admin;
import com.example.springboot.mapper.AdminMapper;
import com.example.springboot.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private AdminMapper adminMapper;

    /**
     * 管理员登录
     */
    @Override
    public Admin adminLogin(String username, String password) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Admin admin = adminMapper.selectOne(qw);

        // 使用BCrypt验证密码
        if (admin != null && PasswordUtil.matches(password, admin.getPassword())) {
            return admin;
        } else {
            return null;
        }
    }

    /**
     * 管理员信息更新
     */
    @Override
    public int updateAdmin(Admin admin) {
        // 如果密码不为空且不是加密后的密码，则加密
        if (admin.getPassword() != null && !admin.getPassword().startsWith("$2a$")) {
            String encodedPassword = PasswordUtil.encode(admin.getPassword());
            admin.setPassword(encodedPassword);
        }
        int i = adminMapper.updateById(admin);
        return i;
    }

}
