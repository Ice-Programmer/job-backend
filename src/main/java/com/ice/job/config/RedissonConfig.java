package com.ice.job.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2023/7/7 19:36
 */
@Configuration
@Data
public class RedissonConfig {

    private Integer database = 0;

    private String host = "127.0.0.1";

    private String port = "6379";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port);

        return Redisson.create(config);
    }
}
