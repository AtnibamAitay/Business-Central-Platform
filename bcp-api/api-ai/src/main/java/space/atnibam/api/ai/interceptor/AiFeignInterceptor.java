package space.atnibam.api.ai.interceptor;

import cn.dev33.satoken.same.SaSameUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @InterfaceName: AiFeignInterceptor
 * @Description: feign拦截器, 在feign请求发出之前，加入一些操作
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-03 10:53
 **/
@Component
public class AiFeignInterceptor implements RequestInterceptor {
    /**
     * 在请求发出之前，加入一些操作，为 Feign 的 RCP调用 添加请求头Same-Token
     *
     * @param requestTemplate 请求模板
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
    }
}
