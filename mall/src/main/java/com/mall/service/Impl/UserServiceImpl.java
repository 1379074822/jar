package com.mall.service.Impl;

import com.mall.mapper.ActivityMapper;
import com.mall.mapper.UserLogMapper;
import com.mall.mapper.UserMapper;
import com.mall.mapper.UserPointMapper;
import com.mall.pojo.Activity;
import com.mall.pojo.User;
import com.mall.pojo.UserLog;
import com.mall.pojo.UserPoint;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "user",cacheManager = "userCacheManager")
@Service
public class UserServiceImpl implements UserService {

    /**
     * 会员mapper
     */
    @Autowired
    private UserMapper userMapper;
    /**
     * 会员积分mapper
     */
    @Autowired
    private UserPointMapper userPointMapper;
    /**
     * 积分活动mapper
     */
    @Autowired
    private ActivityMapper activityMapper;
    /**
     * 会员操作日志mapper
     */
    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public User getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    @Override
    public List<User> getUserList(String queryAccountName, String queryNickName, Integer state,Integer currentPageNo, Integer pageSize)throws Exception{
        return userMapper.getUserList(queryAccountName,queryNickName,state,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getUserCount(String queryAccountName, String queryNickName,Integer state)throws Exception{
        return userMapper.getUserCount(queryAccountName,queryNickName,state);
    }

    @Override
    public boolean insertUser(User user) {
        boolean flag = false;
        if(userMapper.insert(user)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    @Cacheable(value = "user",key = "#id")
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    @CachePut(value = "user",key = "#result.id")
    public User updateUser(User user) {
        userMapper.update(user);
        User u = userMapper.selectById(user.getId());
        return u;
    }

    @Override
    public boolean updateState(Integer state ,Integer id)throws Exception{
        boolean flag = false;
        if(userMapper.updateState(state,id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Activity getActivity() {
        return activityMapper.getActivity();
    }

    @Override
    public boolean updateActivity(Activity activity) {
        boolean flag = false;
        if(activityMapper.update(activity)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getPointCount(Integer id) throws Exception {
        return userPointMapper.getPointCount(id);
    }
    @Override
    public boolean insertPoint(UserPoint userPoint) {
        boolean flag = false;
        if(userPointMapper.insert(userPoint)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<UserPoint> getPointList(Integer userId,Integer currentPageNo, Integer pageSize) throws Exception {
        return userPointMapper.getPointList(userId,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public boolean insertLog(UserLog userLog) throws Exception {
        boolean flag = false;
        if(userLogMapper.insert(userLog)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteLog(Integer id) throws Exception {
        boolean flag = false;
        if(userLogMapper.delete(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<UserLog> getLogList(String account, String createTime, Integer currentPage, Integer pageSize) throws Exception {
        return userLogMapper.getLogList(account,createTime,(currentPage-1)*pageSize,pageSize);
    }

    @Override
    public int getLogCount(String account, String createTime) throws Exception {
        return userLogMapper.getLogCounts(account,createTime);
    }


}
