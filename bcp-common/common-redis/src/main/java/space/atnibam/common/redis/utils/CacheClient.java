package space.atnibam.common.redis.utils;

import space.atnibam.common.redis.model.domain.RedisData;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static space.atnibam.common.redis.constant.CacheConstants.*;

/**
 * @ClassName: CacheClient
 * @Description: Redis 工具类
 * @Author: AtnibamAitay
 * @CreateTime: 2023-09-09 22:05
 **/
@Slf4j
@Component
public class CacheClient {

    /**
     * 用于重建缓存的线程池
     */
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    /**
     * StringRedisTemplate是Spring Data Redis模块的一个类，用来简化Redis的操作
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 构造函数，初始化stringRedisTemplate
     */
    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 向Redis写入数据
     *
     * @param key   数据的键
     * @param value 数据的值
     * @param time  过期时间的值
     * @param unit  过期时间的单位
     */
    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 设置逻辑过期，即使数据在Redis中未过期，但是如果超过设定的逻辑过期时间，也视为过期
     *
     * @param key   数据的键
     * @param value 数据的值
     * @param time  过期时间的值
     * @param unit  过期时间的单位
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        // 创建一个RedisData对象，用于存放数据和逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        // 将RedisData对象写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 从Redis查询并返回指定键对应的列表数据。
     *
     * @param key         查询的键
     * @param elementType 列表元素类型的Class对象
     * @param <R>         返回列表的元素类型
     * @return 键对应的列表数据，如果不存在则返回null
     */
    public <R> List<R> queryList(String key, Class<R> elementType) {
        // 从Redis获取JSON字符串
        String json = stringRedisTemplate.opsForValue().get(key);
        // 检查JSON是否为空
        if (StrUtil.isNotBlank(json)) {
            // 将JSON字符串转换成List<elementType>
            return JSONUtil.toList(JSONUtil.parseArray(json), elementType);
        }
        return null;
    }

    /**
     * 通用查询缓存的方法（不涉及对数据库的查询）
     *
     * @param keyPrefix 缓存键前缀
     * @param id        缓存键后缀，一般为对象ID
     * @param type      返回对象的类型
     * @param <R>       泛型标记，表示方法返回的类型
     * @param <ID>      泛型标记，表示id的类型
     * @return 返回从缓存查找到的对象，如果没有找到则返回null
     */
    public <R, ID> R query(String keyPrefix, ID id, Class<R> type) {
        // 构造完整的缓存键
        String key = keyPrefix + id;

        // 通过StringRedisTemplate从Redis中查询对应的json字符串
        String json = stringRedisTemplate.opsForValue().get(key);

        // 检查查询到的json字符串是否不为空
        if (StrUtil.isNotBlank(json)) {
            // 将json字符串转换成RedisData对象
            RedisData redisdata = JSONUtil.toBean(json, RedisData.class);

            // 获取实际的数据对象
            Object data = redisdata.getData();

            // 如果实际的数据对象为空，则直接返回null
            if (data == null) {
                return null;
            }

            // 判断返回的数据类型是否与请求的type相符，相符则直接强转返回
            if (type.equals(data.getClass())) {
                return (R) data;
            } else {
                // 不相符则将数据对象转换为json字符串后再转换为请求的type类型并返回
                return JSONUtil.toBean(JSONUtil.toJsonStr(data), type);
            }
        }

        // 如果json字符串不为null，意味着命中了空值缓存，此时返回null
        if (json != null) {
            return null;
        }

        // 如果没有从缓存中找到json字符串，则返回null
        return null;
    }

    /**
     * 查询数据，解决缓存穿透问题。
     * 缓存穿透指查询一个一定不存在的数据，常通过禁止外部直接查询某个具体的key，或者将查询结果为空也缓存起来，来解决这个问题。
     *
     * @param keyPrefix  key的前缀
     * @param id         查询数据库时使用的id
     * @param type       返回的数据类型
     * @param dbFallback 数据库查询逻辑，由调用者提供
     * @param time       缓存数据的过期时间值
     * @param unit       缓存数据的过期时间单位
     * @param <R>        返回的数据类型
     * @param <ID>       id的类型
     * @return 查询到的数据
     */
    public <R, ID> R queryWithPassThrough(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        // 构造完整的key
        String key = keyPrefix + id;
        // 从Redis中查询数据
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断结果是否存在
        if (StrUtil.isNotBlank(json)) {
            // 如果存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        // 判断命中的是否是空值
        if (json != null) {
            // 如果是空值，返回错误信息
            return null;
        }

        // 从数据库中查询数据
        R r = dbFallback.apply(id);
        // 如果查询结果为空
        if (r == null) {
            // 将空值写入Redis，并设置过期时间，防止缓存穿透
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回错误信息
            return null;
        }
        // 如果查询结果不为空，将结果写入Redis
        this.set(key, r, time, unit);
        // 返回查询结果
        return r;
    }

    /**
     * 查询数据，解决缓存击穿问题。
     * 缓存击穿指一个热点key在某些时刻突然失效，导致大量的请求都去查询数据库，可以通过设置互斥锁或者逻辑过期解决。
     *
     * @param keyPrefix  key的前缀
     * @param id         查询数据库时使用的id
     * @param type       返回的数据类型
     * @param dbFallback 数据库查询逻辑，由调用者提供
     * @param time       缓存数据的过期时间值
     * @param unit       缓存数据的过期时间单位
     * @param <R>        返回的数据类型
     * @param <ID>       id的类型
     * @return 查询到的数据
     */
    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 从Redis中查询数据
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断结果是否存在
        if (StrUtil.isBlank(json)) {
            // 如果不存在，返回错误信息
            return null;
        }
        // 将json数据反序列化为RedisData对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        // 获取并反序列化保存在RedisData中的data数据
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        // 获取保存在RedisData中的逻辑过期时间
        LocalDateTime expireTime = redisData.getExpireTime();
        // 判断数据是否已经逻辑过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 如果未过期，直接返回结果
            return r;
        }
        // 如果已过期，需要重建缓存
        // 获取互斥锁的key
        String lockKey = LOCK_KEY + id;
        // 尝试获取互斥锁
        boolean isLock = tryLock(lockKey);
        // 判断是否获取锁成功
        if (isLock) {
            // 如果成功，开启一个新的线程来重建缓存
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库
                    R newR = dbFallback.apply(id);
                    // 将查询结果写入Redis，并设置逻辑过期时间
                    this.setWithLogicalExpire(key, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 返回原先过期的信息
        return r;
    }

    /**
     * 查询数据，解决缓存击穿问题。
     * 缓存击穿指一个热点key在某些时刻突然失效，导致大量的请求都去查询数据库，可以通过设置互斥锁或者逻辑过期解决。
     *
     * @param keyPrefix  key的前缀
     * @param id         查询数据库时使用的id
     * @param type       返回的数据类型
     * @param dbFallback 数据库查询逻辑，由调用者提供
     * @param time       缓存数据的过期时间值
     * @param unit       缓存数据的过期时间单位
     * @param <R>        返回的数据类型
     * @param <ID>       id的类型
     * @return 查询到的数据
     */
    public <R, ID> R queryWithMutex(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 从Redis中查询数据
        String cacheData = stringRedisTemplate.opsForValue().get(key);
        // 判断结果是否存在
        if (StrUtil.isNotBlank(cacheData)) {
            // 如果存在，直接返回结果
            return JSONUtil.toBean(cacheData, type);
        }
        // 判断命中的是否是空值
        if (cacheData != null) {
            // 如果是空值，返回错误信息
            return null;
        }

        // 获取互斥锁的key
        String lockKey = LOCK_KEY + id;
        R r = null;
        try {
            // 尝试获取互斥锁
            boolean isLock = tryLock(lockKey);
            // 判断是否获取成功
            if (!isLock) {
                // 如果没有获取成功，线程休眠一段时间之后重试整个查询操作
                Thread.sleep(SLEEP_TIME_IN_MILLIS);
                return queryWithMutex(keyPrefix, id, type, dbFallback, time, unit);
            }
            // 如果获取成功，根据id查询数据库
            r = dbFallback.apply(id);
            // 如果查询结果为空
            if (r == null) {
                // 将空值写入Redis，并设置过期时间，防止缓存穿透
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                // 返回错误信息
                return null;
            }
            // 如果查询结果不为空，将结果写入Redis
            this.set(key, r, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放锁
            unlock(lockKey);
        }
        // 返回查询结果
        return r;
    }

    /**
     * 尝试获取互斥锁
     *
     * @param key 锁的key
     * @return 是否成功获取到锁
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_TIMEOUT, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 尝试加锁
     *
     * @param key     锁
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=加锁成功；false=加锁失败
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", timeout, unit);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放互斥锁
     *
     * @param key 锁的key
     */
    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除单个对象
     *
     * @param key 键
     */
    public void deleteObject(final String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 使用SCAN命令安全地根据给定模式删除键
     *
     * @param pattern 键的模式，如 "userMurmurs:userId:*"
     */
    public void deleteByPattern(String pattern) {
        Set<String> keysToDelete = new HashSet<>();
        Cursor<byte[]> cursor = stringRedisTemplate.executeWithStickyConnection(connection ->
                connection.scan(ScanOptions.scanOptions().match(pattern).build())
        );
        while (cursor.hasNext()) {
            keysToDelete.add(new String(cursor.next(), StandardCharsets.UTF_8));
        }
        if (!keysToDelete.isEmpty()) {
            stringRedisTemplate.delete(keysToDelete);
        }
    }
}