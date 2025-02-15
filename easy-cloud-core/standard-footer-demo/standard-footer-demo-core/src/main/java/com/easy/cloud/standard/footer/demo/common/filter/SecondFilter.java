package com.easy.cloud.standard.footer.demo.common.filter;

import com.easy.cloud.common.threadlocal.manager.ThreadLocalManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author daiqi
 * @create 2019-06-22 23:25
 */
@Slf4j
public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("SecondFilter -------------------before");
        log.info("获取到的数据为：" + ThreadLocalManager.get("name"));
        chain.doFilter(request, response);
        log.info("SecondFilter -------------------end");
        log.info("获取到的数据为：" + ThreadLocalManager.get("name"));
    }

    @Override
    public void destroy() {

    }
}
