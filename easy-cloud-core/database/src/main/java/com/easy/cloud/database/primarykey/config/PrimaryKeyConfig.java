package com.easy.cloud.database.primarykey.config;

import com.easy.cloud.common.workspace.properties.WorkspaceProperties;
import com.easy.cloud.database.primarykey.BaseKeyGenerator;
import com.easy.cloud.database.primarykey.snowflake.KeyGeneratorForSnowflake;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 主键配置类
 * </p>
 *
 * @author daiqi
 * @创建时间 2018年5月12日 下午5:11:55
 */
@Configuration
@Data
public class PrimaryKeyConfig {
    @Autowired
    private WorkspaceProperties workspaceProperties;

    @Bean
    @ConditionalOnMissingBean
    public BaseKeyGenerator primaryKeyGenerator(WorkspaceProperties workspaceProperties) {
        return new KeyGeneratorForSnowflake(workspaceProperties);
    }
}
