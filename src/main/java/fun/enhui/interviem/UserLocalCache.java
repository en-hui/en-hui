package fun.enhui.interviem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 胡恩会
 * @date 2021/2/7 12:19
 */
public class UserLocalCache {
    private Map<String, UserInfo> userCache = new HashMap<>();

    public class UserInfo {
        private String userId;

        private String userName;

        private String cardNo;

        private LocalDateTime lastUpdateTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public LocalDateTime getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }

    /**
     * 初始化用户信息缓存
     *
     * @author 胡恩会
     * @date 2021/2/7 12:21
     **/
    public void initUserInfoCache() {
        List<UserInfo> users = new ArrayList<>();
        // users = userMapper.listUser();
        for (UserInfo user : users) {
            userCache.put(user.getUserId(), user);
        }
    }

    /**
     * 从缓存中获取用户，如果缓存中数据超过半小时，则更新缓存
     *
     * @param id
     * @return fun.enhui.interviem.UserLocalCache.UserInfo
     * @author 胡恩会
     * @date 2021/2/7 12:33
     **/
    public UserInfo getUserInfoFromCacheById(String id) {
        UserInfo userInfo = userCache.get(id);
        LocalDateTime lastUpdateTime = userInfo.getLastUpdateTime();
        // 如果上次更新日期+半小时 在当前日期前面，说明要更新
        if (lastUpdateTime.plusMinutes(30).isBefore(LocalDateTime.now())) {
            this.updateUserInfoCache(id);
        }
        return userInfo;
    }

    /**
     * 更新缓存中的数据
     *
     * @param id
     * @return void
     * @author 胡恩会
     * @date 2021/2/7 12:36
     **/
    public void updateUserInfoCache(String id) {
        UserInfo user = new UserInfo();
        // 伪代码，重新查一次用户信息,更新上次更新时间，放进缓存
        // user = userMapper.getUserById(id);
        user.setLastUpdateTime(LocalDateTime.now());
        userCache.put(id, user);
    }
}
