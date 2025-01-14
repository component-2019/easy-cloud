package com.easy.cloud.standard.exception.config;

import cn.hutool.core.util.StrUtil;
import com.easy.cloud.basic.errorcode.BaseErrorCode;
import com.easy.cloud.basic.errorcode.BaseErrorCodeEnum;
import com.easy.cloud.common.workspace.properties.WorkspaceProperties;
import com.easy.cloud.exception.properties.ExceptionProperties;
import com.easy.cloud.informer.BaseInformer;
import com.easy.cloud.informer.impl.InformerForDingding;
import com.easy.cloud.informer.impl.InformerForLog;
import com.easy.cloud.informer.properties.InformerProperties;
import com.easy.cloud.standard.exception.properties.StandardExceptionProperties;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author daiqi
 * @create 2019-03-25 13:53
 */
@Configuration
public class StandardExceptionConfig {
    @Autowired
    private StandardExceptionProperties standardExceptionProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private WorkspaceProperties workspaceProperties;

    @Bean
    @ConditionalOnMissingBean
    public InformerProperties informerProperties() {
        String defaultWebHookForDingding = standardExceptionProperties.getDefaultWebHookForDingding();
        List<BaseInformer> exceptionInformers = Lists.newArrayList();
        if (StrUtil.isNotBlank(defaultWebHookForDingding)) {
            InformerForDingding informerForDingding = new InformerForDingding(defaultWebHookForDingding);
            informerForDingding.setProfilesActive(applicationContext.getEnvironment().getActiveProfiles()[0]);
            exceptionInformers.add(informerForDingding);
        }

        exceptionInformers.add(new InformerForLog());
        return InformerProperties.builder()
                .exceptionInformers(exceptionInformers)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionProperties exceptionProperties() {
        ExceptionProperties exceptionProperties = new ExceptionProperties();
        exceptionProperties.buildNeedInformCodes(needInformCodes());
        return exceptionProperties;
    }

    private List<BaseErrorCode> needInformCodes() {
        List<BaseErrorCode> needInformCodes = Lists.newArrayList();
        needInformCodes.add(BaseErrorCodeEnum.SYS_ERROR);
        needInformCodes.add(BaseErrorCodeEnum.SYS_EXCEPTION);
        needInformCodes.add(BaseErrorCodeEnum.RUNTIME_EXCEPTION);
        return needInformCodes;
    }
}
