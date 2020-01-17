package com.carpool.service;

import com.carpool.dao.ApiUserLevelMapper;
import com.carpool.dao.ApiUserMapper;
import com.carpool.entity.SmsLogVo;
import com.carpool.entity.UserLevelVo;
import com.carpool.entity.UserVo;
import com.carpool.utils.RRException;
import com.carpool.validator.Assert;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ApiUserService {
    @Autowired
    private ApiUserMapper userDao;
    @Autowired
    private ApiUserLevelMapper userLevelDao;

    public UserVo queryObject(Long userId) {
        return userDao.queryObject(userId);
    }
    
    public UserVo queryByIMUserId(String imUserId) {
        return userDao.queryByIMUserId(imUserId);
    }

    public UserVo queryByOpenId(String openId) {
        return userDao.queryByOpenId(openId);
    }

    public List<UserVo> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    public void save(String mobile, String password) {
        UserVo user = new UserVo();
        user.setMobile(mobile);
        user.setUsername(mobile);
        user.setPassword(DigestUtils.sha256Hex(password));
        user.setRegister_time(new Date());
        userDao.save(user);
    }
    
    public UserVo saveByUserName(UserVo userVo) {
    	userVo.setPassword(DigestUtils.sha256Hex(userVo.getPassword()));
    	userVo.setRegister_time(new Date());
        userDao.save(userVo);
        
        return userVo;
    }
    
    public UserVo saveByMobile(UserVo userVo) {
    	userVo.setUsername(userVo.getMobile());
    	userVo.setPassword(DigestUtils.sha256Hex(userVo.getPassword()));
    	userVo.setRegister_time(new Date());
        userDao.save(userVo);
        
        return userVo;
    }
    
    public UserVo saveByEmail(UserVo userVo) {
    	userVo.setUsername(userVo.getEmail());
    	userVo.setPassword(DigestUtils.sha256Hex(userVo.getPassword()));
    	userVo.setRegister_time(new Date());
        userDao.save(userVo);
        
        return userVo;
    }

    public void save(UserVo userVo) {
        userDao.save(userVo);
    }

    public void update(UserVo user) {
        userDao.update(user);
    }

    public void delete(Long userId) {
        userDao.delete(userId);
    }

    public void deleteBatch(Long[] userIds) {
        userDao.deleteBatch(userIds);
    }

    public UserVo queryByMobile(String mobile) {
        return userDao.queryByMobile(mobile);
    }
    
    public UserVo queryByUserName(String userName) {
        return userDao.queryByUserName(userName);
    }
    
    public UserVo queryByUserNameMove(String userName) {
        return userDao.queryByUserNameMove(userName);
    }
    
    public UserVo queryByEmail(String email) {
        return userDao.queryByEmail(email);
    }

    public UserVo login(String mobile, String password) {
        UserVo user = queryByMobile(mobile);
        //Assert.isNull(user, "手机号或密码错误");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new RRException("手机号或密码错误");
        }

        return user;
    }
    
    public UserVo loginByEmail(String email, String password) {
        UserVo user = queryByEmail(email);
        //Assert.isNull(user, "");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new RRException("手机号或密码错误");
        }

        return user;
    }

    public SmsLogVo querySmsCodeByUserId(Long user_id) {
        return userDao.querySmsCodeByUserId(user_id);
    }


    public int saveSmsCodeLog(SmsLogVo smsLogVo) {
        return userDao.saveSmsCodeLog(smsLogVo);
    }

    public String getUserLevel(UserVo loginUser) {
        String result = "普通用户";
        UserLevelVo userLevelVo = userLevelDao.queryObject(loginUser.getUser_level_id());
        if (null != userLevelVo) {
            result = userLevelVo.getName();
        }
        return result;
    }
}
