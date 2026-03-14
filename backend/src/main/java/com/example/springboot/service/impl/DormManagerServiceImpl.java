package com.example.springboot.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.PasswordUtil;
import com.example.springboot.entity.DormManager;
import com.example.springboot.mapper.DormManagerMapper;
import com.example.springboot.service.DormManagerService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Service
public class DormManagerServiceImpl extends ServiceImpl<DormManagerMapper, DormManager> implements DormManagerService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private DormManagerMapper dormManagerMapper;

    /**
     * 宿管登录
     */
    @Override
    public DormManager dormManagerLogin(String username, String password) {
        QueryWrapper<DormManager> qw = new QueryWrapper<>();
        qw.eq("username", username);
        DormManager dormManager = dormManagerMapper.selectOne(qw);

        // 使用BCrypt验证密码
        if (dormManager != null && PasswordUtil.matches(password, dormManager.getPassword())) {
            return dormManager;
        } else {
            return null;
        }
    }

    /**
     * 宿管新增
     */
    @Override
    public int addNewDormManager(DormManager dormManager) {
        // 加密密码后再存储
        String encodedPassword = PasswordUtil.encode(dormManager.getPassword());
        dormManager.setPassword(encodedPassword);
        int insert = dormManagerMapper.insert(dormManager);
        return insert;
    }

    /**
     * 宿管查找
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<DormManager> qw = new QueryWrapper<>();
        qw.like("name", search);
        Page dormManagerPage = dormManagerMapper.selectPage(page, qw);
        return dormManagerPage;
    }

    /**
     * 宿管信息更新
     */
    @Override
    public int updateNewDormManager(DormManager dormManager) {
        // 如果密码不为空且不是加密后的密码，则加密
        if (dormManager.getPassword() != null && !dormManager.getPassword().startsWith("$2a$")) {
            String encodedPassword = PasswordUtil.encode(dormManager.getPassword());
            dormManager.setPassword(encodedPassword);
        }
        int i = dormManagerMapper.updateById(dormManager);
        return i;
    }

    /**
     * 宿管删除
     */
    @Override
    public int deleteDormManager(String username) {
        int i = dormManagerMapper.deleteById(username);
        return i;
    }


}
