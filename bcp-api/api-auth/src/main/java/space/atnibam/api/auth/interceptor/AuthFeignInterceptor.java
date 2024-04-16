package space.atnibam.api.auth.interceptor;

import cn.dev33.satoken.same.SaSameUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import static cn.dev33.satoken.same.SaSameUtil.SAME_TOKEN;

/**
 * @InterfaceName: AuthFeignInterceptor
 * @Description: feign拦截器, 在feign请求发出之前，加入一些操作
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-06 10:53
 **/
@Component
public class AuthFeignInterceptor implements RequestInterceptor {
    /**
     * 在请求发出之前，加入一些操作，为 Feign 的 RCP调用 添加请求头Same-Token
     *
     * @param requestTemplate 请求模板
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SAME_TOKEN, SaSameUtil.getToken());
    }
}