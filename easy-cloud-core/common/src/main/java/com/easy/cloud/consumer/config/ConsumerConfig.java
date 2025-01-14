package com.easy.cloud.consumer.config;

import com.easy.cloud.consumer.selector.BaseConsumerLogicSelector;
import com.easy.cloud.consumer.selector.DefaultConsumerLogicSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author daiqi
 * @create 2018-08-27 19:42
 */
@Configuration
public class ConsumerConfig {
    @Bean
    @ConditionalOnMissingBean
    public BaseConsumerLogicSelector consumerLogicSelector() {
        return new DefaultConsumerLogicSelector();
    }
}
