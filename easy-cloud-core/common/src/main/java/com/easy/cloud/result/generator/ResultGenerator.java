package com.easy.cloud.result.generator;

import com.easy.cloud.basic.errorcode.BaseErrorCodeEnum;
import com.easy.cloud.basic.util.BaseUtil;
import com.easy.cloud.exception.BusinessException;
import com.easy.cloud.exception.dto.BaseBusinessExceptionDTO;
import com.easy.cloud.no.generator.NoGenerator;
import com.easy.cloud.result.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Result对象生产者
 *
 * @author daiqi
 * @create 2019-07-03 21:25
 */
@Data
@Accessors(chain = true)
public class ResultGenerator {
    /**
     * result的msgNo默认前缀
     */
    private static final String PREFIX_DEFAULT = "RMN";
    /** 异常的msgNo默认前缀 */
    private static final String EXCEPTION_PREFIX_DEFAULT = "EMN";
    /**
     * result的msgNo默认后缀长度
     */
    private static final int SUFFIX_COUNT_DEFAULT = 6;

    private boolean exceptionDetail;
    /**
     * 是否响应异常对象
     */
    private boolean rspException;

    /**
     * 正常响应的result的编号配置
     */
    private ResultNoConfig noConfig = new ResultNoConfig(PREFIX_DEFAULT, SUFFIX_COUNT_DEFAULT);

    /**
     * 异常响应的result的编号配置
     */
    private ResultNoConfig exceptionNoConfig = new ResultNoConfig(EXCEPTION_PREFIX_DEFAULT, SUFFIX_COUNT_DEFAULT);

    /**
     * 编号生成器
     */
    private NoGenerator noGenerator;
    /**
     * 异常result编号生成器
     */
    private NoGenerator exceptionNoGenerator;

    public Result generateResult(Object object) {
        Result result = Result.newInstance(object);
        if (BaseUtil.isNotNull(noGenerator)) {
            result.setMsgNo(noGenerator.generateNoCore(noConfig.getPrefix(), noConfig.getSuffixCount()));
        }
        return result;
    }


    /**
     * <p>
     * 生成异常Result类
     * </p>
     *
     * @param path      : String : 请求的路径
     * @param exception : Exception : 异常对象
     * @return com.easy.cloud.result.Result
     * @author daiqi
     * @date 2018/11/22 10:12
     */
    public Result generateExceptionResult(String path, Exception exception) {
        Result result = Result.newInstance(null);
        // 构建业务系统异常数据传输对象
        if (rspException) {
            buildBusinessExceptionDTO(path, exception, result);
        }
        // 构建code和message
        buildCodeAndMessage(exception, result);
        // 构建msgNo
        buildMsgNo(result);
        return result;
    }

    protected void buildBusinessExceptionDTO(String path, Exception exception, Result result) {
        BaseBusinessExceptionDTO businessExceptionDTO = BaseBusinessExceptionDTO.newInstance()
                .build(path, exception, exceptionDetail);
        result.setData(businessExceptionDTO);
    }

    protected void buildMsgNo(Result result) {
        if (BaseUtil.isNotNull(noGenerator)) {
            result.setMsgNo(exceptionNoGenerator.generateNoCore(exceptionNoConfig.getPrefix(), exceptionNoConfig.getSuffixCount()));
        }
    }

    /**
     * <p>
     * 根据异常构建result的code和message
     * </p>
     *
     * @param exception : 异常
     * @param result    : result
     * @return void
     * @author daiqi
     * @date 2019/7/3 22:48
     */
    protected void buildCodeAndMessage(Exception exception, Result result) {
        if (exception instanceof BusinessException) {
            result.setCode(((BusinessException) exception).getCode());
            result.setMessage(exception.getMessage());
        } else if (exception instanceof RuntimeException) {
            result.setCode(BaseErrorCodeEnum.RUNTIME_EXCEPTION.getCode());
            result.setMessage(BaseErrorCodeEnum.RUNTIME_EXCEPTION.getMessage());
        } else {
            result.setCode(BaseErrorCodeEnum.SYS_EXCEPTION.getCode());
            result.setMessage(BaseErrorCodeEnum.SYS_EXCEPTION.getMessage());
        }
    }

    @Accessors
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResultNoConfig {

        /**
         * 编号前缀
         */
        private String prefix = PREFIX_DEFAULT;
        /**
         * 编号后缀数量
         */
        private int suffixCount = SUFFIX_COUNT_DEFAULT;
    }
}
