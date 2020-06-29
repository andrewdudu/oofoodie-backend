package com.oofoodie.backend.helper;

import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class GetRedisData {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        String key = "user-" + user.getUsername();
        redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES);
    }

    public Mono<User> getUser(String username) {
        String key = "user-" + username;

        if (redisTemplate.hasKey(key)) {
            return Mono.just((User) Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
        }

        return userRepository.findByUsername(username)
                .doOnNext(user -> redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES));
    }
}
