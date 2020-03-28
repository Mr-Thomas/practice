package com.tj.practice.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/15 14:17
 */
@Slf4j
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis分布式上锁的lua脚本
     */
    private static String LOCK_LUA_SCIPT;

    /**
     * redis分布式释放锁的lua脚本
     */
    private static String UNLOCK_LUA_SCIPT;

    /**
     * 操作成功
     */
    public static final long SUCCESS_RESULT = 1;

    /**
     *  锁已经被释放
     */
    public static final long LOCK_ALREADY_RELEASED = 2;

    /**
     * 释放的不是自己的锁
     */
    public static final long LOCK_NOT_OWN = 3;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('setnx', KEYS[1], ARGV[1]) == 1) then ");
        sb.append(" redis.call('pexpire',KEYS[1],ARGV[2]);");
        sb.append(" return 1; ");
        sb.append("end;");
        sb.append("return 0;");
        LOCK_LUA_SCIPT = sb.toString();

        sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 0) then ");
        sb.append("return 2;");
        sb.append("end;");
        sb.append("if (redis.call('get', KEYS[1]) == ARGV[1]) then");
        sb.append(" redis.call('del', KEYS[1]);");
        sb.append(" return 1; ");
        sb.append("end;");
        sb.append("return 3;");

        UNLOCK_LUA_SCIPT = sb.toString();

    }

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public RedisUtil() {
        log.info("RedisUtil init Success");
    }

    /**
     * @param channel
     * @param message
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

    public Object getObjectFromTopic(Message message) {
        RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
        Object channel = serializer.deserialize(message.getChannel());
        Object messageStr = serializer.deserialize(message.getBody());
        return messageStr;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public void set(final String key, final Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
    }

    /**
     * 写入hahs缓存
     *
     * @param key
     * @param value
     */
    public void hSet(final String hashkey, final String key, final Object value) {
        redisTemplate.boundHashOps(hashkey).put(key, value);
    }

    /**
     * 写入hash缓存
     *
     * @param key
     * @param value
     */
    public void hSet(final String hashKey, final String key, final Object value, long time, TimeUnit timeUnit) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(hashKey);
        ops.put(key, value);
        ops.expire(time, timeUnit);
    }

    public Object hGet(final String hashkey, final String key) {
        return redisTemplate.boundHashOps(hashkey).get(key);
    }

    public boolean hExists(final String hashkey, final String key) {
        return redisTemplate.boundHashOps(hashkey).hasKey(key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value.toString());
    }


    /**
     * 读取缓存
     *
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, Class<T> clazz) {
        return (T) redisTemplate.boundValueOps(key).get();
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object getObj(final String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 删除，根据key精确匹配
     *
     * @param key
     */
    public void del(final String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    public void del(final List<String> key) {
        redisTemplate.delete(key);
    }

    public long hDel(final String hashKey, final String... key) {
        try {
            redisTemplate.opsForHash().delete(hashKey,key);
        } catch (Exception e) {
            log.error("hDel exception,hashKey=" + hashKey, e);
        }
        return -1;
    }





    /**
     * 批量删除，根据key模糊匹配
     *
     * @param pattern
     */
    public void delpn(final String... pattern) {
        for (String kp : pattern) {
            redisTemplate.delete(redisTemplate.keys(kp + "*"));
        }
    }

    /**
     * key是否存在
     *
     * @param key
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Created on 2017年8月22日
     * 从左侧放入list结构
     *
     * @param key
     * @param obj
     * @author:[ljb]
     */
    public void addToList(String key, Object obj) {
        ListOperations opsForList = redisTemplate.opsForList();
        Long size = opsForList.size(key);
        opsForList.leftPush(key, obj);
    }

    /**
     * Created on 2017年8月22日
     * <p>
     * Description:[取出集合中所有数据]
     * </p>
     *
     * @param key
     * @return
     * @author:[ljb]
     */
    public List<Object> getList(String key) {
        ListOperations opsForList = redisTemplate.opsForList();
        Long size = opsForList.size(key);
        List<Object> range = opsForList.range(key, 0, size - 1);
        return range;
    }

    /**
     * 从list右侧弹出一个元素
     *
     * @param key
     * @return
     */
    public Object getRightFromList(String key) {
        ListOperations opsForList = redisTemplate.opsForList();
        Object object = opsForList.rightPop(key);
        return object;
    }


    /**
     * 锁的默认超时时间为500毫秒
     *
     * @param key
     */
    public void Lock(String key) {
        String absentKey = key + "_absent";
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(absentKey, "1");
        if (!ifAbsent) {
            int i = 0;
            while (i < 500) {
                if (!exists(absentKey)) {
                    Boolean tryAgain = redisTemplate.opsForValue().setIfAbsent(absentKey, "1");
                    if (tryAgain) {
                        break;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        } else {
            redisTemplate.expire(absentKey, 10000, TimeUnit.MILLISECONDS);
        }

    }

    public void UnLock(String key) {
        String absentKey = key + "_absent";
        redisTemplate.delete(absentKey);
    }


    /**
     * Created on 2019年11月15日 10:43:48
     * <p>Description:[获取redis分布式锁]</p>
     * @author:[wangyunliang]
     * @param: key 锁名称
     * @param: requestId 请求id
     * @param: expireTime 锁的超时时间 毫秒为单位
     * @param: maxWaitTime 获取锁的最大等待时间
     * @return:
     */
    public boolean lock(String key, String requestId, long expireTime, TimeUnit expireTimeUnit, int maxWaitTime, TimeUnit waitTimeUnit) {
        long current = System.currentTimeMillis();
        long lExpireTime = expireTimeUnit.toMillis(expireTime);
        long lWaitTime = waitTimeUnit.toMillis(maxWaitTime);
        while (true) {
            if (tryLock(key, requestId, lExpireTime)) {
                return true;
            }
            if (System.currentTimeMillis() - current > lWaitTime) {
                // 时间到了 还未获取到锁
                return false;
            }
        }
    }

    /**
     * Created on 2019年11月15日 18:50:49
     * <p>Description:[尝试获取一把锁，其value为requestId，有效期为expireTime]</p>
     * @author:[wangyunliang]
     * @param: key
     * @param: requestId
     * @param: expireTime
     * @return:
     */
    private boolean tryLock(String key, String requestId, long expireTime) {
        List<String> keys = new ArrayList<>(1);
        keys.add(key);
        Long result = (Long) redisTemplate.execute(RedisScript.of(LOCK_LUA_SCIPT, Long.class), keys, requestId, expireTime);
        return result == null ? false : result == SUCCESS_RESULT;
    }

    /**
     * Created on 2019年11月15日 10:44:45
     * <p>Description:[获取redis分布式锁，获取等待时间为0]</p>
     * @author:[wangyunliang]
     * @param: key 锁名称
     * @param: requestId 请求id
     * @param: expireTime 锁的超时时间
     * @return:
     */
    public boolean lock(String key, String requestId, long expireTime){
        return lock(key,requestId, expireTime,TimeUnit.MILLISECONDS, 0, TimeUnit.MILLISECONDS);
    }

    public boolean unlock(String key, String requestId) {
        List<String> keys = new ArrayList<>(1);
        keys.add(key);
        long result = (long) redisTemplate.execute(RedisScript.of(UNLOCK_LUA_SCIPT, Long.class), keys, requestId);
        if (result == LOCK_ALREADY_RELEASED) {
            log.error("锁对应的key已经不存在，key={},requestId={}", key, requestId);
        } else if (result == LOCK_NOT_OWN) {
            log.error("释放锁时使用的requestId与请求锁时的不一致，key={},requestId={}", key, requestId);
        }
        return result != LOCK_NOT_OWN;
    }

    /**
     * Created on 2017年8月22日
     * <p>
     * Description:[判断集合中是否已经存在此值]
     * </p>
     *
     * @param key
     * @param value
     * @return
     * @author:[ljb]
     */
    public boolean isListMember(String key, Object value) {
        List<Object> list = getList(key);
        if (list.contains(value)) {
            return true;
        } else {
            return false;
        }
    }

    public Map getHashMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void remove(final String... keys) {
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    public void setHashKey(final String hashKey, Object var2, Object var3) {
        if (var2 instanceof String) {
            redisTemplate.opsForHash().put(hashKey, var2, var3);
        } else {
            redisTemplate.opsForHash().put(hashKey, var2.toString(), var3);
        }
    }

    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("redis set error:key-{}",key);
        }
        return result;
    }

    public void setHashMap(String key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }


    public Long increment(String key) {
        Long l = redisTemplate.boundValueOps(key).increment(1);
        if (l != null) {
            return l;
        }
        return -1L;
    }


    public Long increment(String key, long liveTime, long delta) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.addAndGet(delta);
        //初始设置过期时间
        if (liveTime > 0) {
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }

        return increment;
    }

    /**
     * 设置 String 类型 key-value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 获取 String 类型 key-value
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (毫秒单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,毫秒单位
     */
    public void setForTimeMS(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,分钟单位
     */
    public void setForTimeMIN(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }


    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param key
     * @param value
     * @param time  过期时间,分钟单位
     */
    public void setForTimeCustom(String key, String value, long time, TimeUnit type) {
        redisTemplate.opsForValue().set(key, value, time, type);
    }

    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并添加
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return (String) redisTemplate.opsForValue().getAndSet(key, value);
    }


    /**
     * 批量添加 key-value (重复的键会覆盖)
     *
     * @param keyAndValue
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param keyAndValue
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key
     * @param number
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key
     * @param number
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }


    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @param type
     * @return
     */
    public boolean expire(String key, long time, TimeUnit type) {
        return redisTemplate.boundValueOps(key).expire(time, type);
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key
     * @return
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }


    /**
     * 获取指定key 的过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.boundValueOps(key).getExpire();
    }

    /**
     * 修改 key
     *
     * @param key
     * @return
     */
    public void rename(String key, String newKey) {
        redisTemplate.boundValueOps(key).rename(newKey);
    }

    /**
     * 删除 key-value
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    //hash操作

    /**
     * 添加 Hash 键值对
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void put(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量添加 hash 的 键值对
     * 有则覆盖,没有则添加
     *
     * @param key
     * @param map
     */
    public void putAll(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 添加 hash 键值对. 不存在的时候才添加
     *
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public boolean putIfAbsent(String key, String hashKey, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }


    /**
     * 删除指定 hash 的 HashKey
     *
     * @param key
     * @param hashKeys
     * @return 删除成功的 数量
     */
    public Long delete(String key, String... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }


    /**
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public Long increment(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public Double increment(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取指定 key 下的 hashkey
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object getHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }


    /**
     * 获取 key 下的 所有  hashkey 和 value
     *
     * @param key
     * @return
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashkey
     *
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取 key 下的 所有 hashkey 字段名
     *
     * @param key
     * @return
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }


    /**
     * 获取指定 hash 下面的 键值对 数量
     *
     * @param key
     * @return
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //List 操作

    /**
     * 指定 list 从左入栈
     *
     * @param key
     * @return 当前队列的长度
     */
    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 指定 list 从左出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     *
     * @param key
     * @return 出栈的值
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从左边依次入栈
     * 导入顺序按照 Collection 顺序
     * 如: a b c => c b a
     *
     * @param key
     * @param values
     * @return
     */
    public Long leftPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 指定 list 从右入栈
     *
     * @param key
     * @return 当前队列的长度
     */
    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 指定 list 从右出栈
     * 如果列表没有元素,会堵塞到列表一直有元素或者超时为止
     *
     * @param key
     * @return 出栈的值
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从右边依次入栈
     * 导入顺序按照 Collection 顺序
     * 如: a b c => a b c
     *
     * @param key
     * @param values
     * @return
     */
    public Long rightPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }


    /**
     * 根据下标获取值
     *
     * @param key
     * @param index
     * @return
     */
    public Object popIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }


    /**
     * 获取列表指定长度
     *
     * @param key
     * @param index
     * @return
     */
    public Long listSize(String key, long index) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 获取列表 指定范围内的所有值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 删除 key 中 值为 value 的 count 个数.
     *
     * @param key
     * @param count
     * @param value
     * @return 成功删除的个数
     */
    public Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 删除 列表 [start,end] 以外的所有元素
     *
     * @param key
     * @param start
     * @param end
     */
    public void listTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);

    }

    /**
     * 将 key 右出栈,并左入栈到 key2
     *
     * @param key  右出栈的列表
     * @param key2 左入栈的列表
     * @return 操作的值
     */
    public Object rightPopAndLeftPush(String key, String key2) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key, key2);

    }

    //set 操作  无序不重复集合

    /**
     * 添加 set 元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long add(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key
     * @param otherkey
     * @return
     */
    public Set<Object> difference(String key, String otherkey) {
        return redisTemplate.opsForSet().difference(key, otherkey);
    }


    /**
     * 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中
     *
     * @param key
     * @param otherkey
     * @param newKey
     * @return 返回差集的数量
     */
    public Long differenceAndStore(String key, String otherkey, String newKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);
    }


    /**
     * 删除一个或多个集合中的指定值
     *
     * @param key
     * @param values
     * @return 成功删除数量
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机移除一个元素,并返回出来
     *
     * @param key
     * @return
     */
    public Object randomSetPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机获取一个元素
     *
     * @param key
     * @return
     */
    public Object randomSet(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取指定数量的元素,同一个元素可能会选中两次
     *
     * @param key
     * @param count
     * @return
     */
    public List<Object> randomSet(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
     *
     * @param key
     * @param count
     * @return
     */
    public Set<Object> randomSetDistinct(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 将 key 中的 value 转入到 destKey 中
     *
     * @param key
     * @param value
     * @param destKey
     * @return 返回成功与否
     */
    public boolean moveSet(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 无序集合的大小
     *
     * @param key
     * @return
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断 set 集合中 是否有 value
     *
     * @param key
     * @param value
     * @return
     */
    public boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回 key 和 othere 的并集
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> unionSet(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }


    /**
     * 将 key 与 otherKey 的并集,保存到 destKey 中
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return destKey 数量
     */
    public Long unionAndStoreSet(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }


    /**
     * 返回集合中所有元素
     *
     * @param key
     * @return
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    //Zset 根据 socre 排序   不重复 每个元素附加一个 socre  double类型的属性(double 可以重复)

    /**
     * 添加 ZSet 元素
     *
     * @param key
     * @param value
     * @param score
     */
    public boolean add(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加 Zset <br>
     * Set<TypedTuple<Object>> tuples = new HashSet<>();<br>
     * TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("zset-5",9.6);<br>
     * tuples.add(objectTypedTuple1);
     *
     * @param key
     * @param tuples
     * @return
     */
    public Long batchAddZset(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * Zset 删除一个或多个元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long removeZset(String key, String... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Double incrementScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     *
     * @param key
     * @param value
     * @return
     */
    public Long rank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     *
     * @param key
     * @param value
     * @return
     */
    public Long reverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> range(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数)
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合)
     *
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count  输出指定元素数量
     * @return
     */
    public Set<Object> rangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合)
     *
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count  输出指定元素数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> reverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数)
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数)
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合)
     *
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count  输出指定元素数量
     * @return
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合)
     *
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count  输出指定元素数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 返回指定分数区间 [min,max] 的元素个数
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long countZSet(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 返回 zset 集合数量
     *
     * @param key
     * @return
     */
    public long sizeZset(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取指定成员的 score 值
     *
     * @param key
     * @param value
     * @return
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除指定索引位置的成员,其中成员分数按( 从小到大 )
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long removeRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long removeRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long unionAndStoreZset(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long unionAndStoreZset(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * key 和 otherKey 两个集合的交集,保存在 destKey 集合中
     *
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long intersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key 和 otherKeys 多个集合的交集,保存在 destKey 集合中
     *
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long intersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    public List hMultiHashGet(final String hashkey, Set keys) {
        return redisTemplate.boundHashOps(hashkey).multiGet(keys);
    }

    public long hashIncrement(final String hashKey, Object o){
        return redisTemplate.opsForHash().increment(hashKey, o.toString(), 1);
    }

    public long hashIncrementBy(final String hashKey, Object o, long delta){
        return redisTemplate.opsForHash().increment(hashKey, o.toString(), delta);
    }

    public void addZSet(final String zsetKey, long l){
        redisTemplate.opsForZSet().add(zsetKey, l, l);
    }

    public long getZSetSize(final String zsetKey){
        return redisTemplate.opsForZSet().size(zsetKey);
    }

    public Set<Object> getZSetByRange(final String zsetKey, int start, int end){
        return redisTemplate.opsForZSet().range(zsetKey, start, end);
    }

    public Long removeZSet(final String zsetKey, int start, int end){
        return redisTemplate.opsForZSet().removeRange(zsetKey, start, end);
    }

    public void deleteHashKey(String h, Object o) {
        redisTemplate.opsForHash().delete(h, o);
    }

    public void setForIncrement(String key,Object value){
        redisTemplate.opsForValue().set(key, value);
    }

}
