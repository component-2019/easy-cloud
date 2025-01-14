package com.easy.cloud.database.primarykey.snowflake;


import com.easy.cloud.basic.util.BaseUtil;
import com.easy.cloud.common.workspace.properties.WorkspaceProperties;
import com.easy.cloud.database.common.entity.BaseEntity;
import com.easy.cloud.database.primarykey.BaseKeyGenerator;

import java.io.Serializable;

/**
 * <p>
 * 雪花生成器
 * </p>
 *
 * @author daiqi
 * @创建时间 2018年5月8日 下午8:04:56
 */
public class KeyGeneratorForSnowflake implements BaseKeyGenerator {
    /**
     * 工作空间属性
     */
    private long workerId;
    private long dataCenterId;

    public KeyGeneratorForSnowflake(WorkspaceProperties workspaceProperties) {
        workspaceProperties.buildDefault();
        this.workerId = workspaceProperties.getWorkerId();
        this.dataCenterId = workspaceProperties.getDataCenterId();
    }

    @Override
    public Serializable generate(Object entityObj) {
        if (entityObj != null && entityObj instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entityObj;
            if (BaseUtil.isNotNull(baseEntity.getId())) {
                return baseEntity.getId();
            }
        }
        return SnowflakeIdAlgorithm.getSingleInstance(workerId, dataCenterId, entityObj.getClass()).nextId();
    }

}
