package space.atnibam.ai.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import space.atnibam.ai.config.OpenAIConfig;
import space.atnibam.ai.enums.OpenAiApiType;
import space.atnibam.ai.model.dto.MessageDTO;
import space.atnibam.ai.service.AIService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.atnibam.ai.constant.AiServiceConstant.*;

/**
 * @ClassName: AIServiceImpl
 * @Description: AI服务实现类
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-12 21:18
 **/
@Service
@Slf4j
@EnableAsync
public class AIServiceImpl implements AIService {

    @Resource
    private OpenAIConfig openAIConfig;

    /**
     * 使用指定的GPT模型和消息列表与GPT进行交互
     *
     * @param model       GPT模型名称
     * @param messagesDTO 消息列表
     * @return 交互结果
     * @throws IOException 如果发生I/O错误
     */
    @Override
    public String generateText(String model, List<MessageDTO> messagesDTO) throws IOException {
        // 创建Gson对象，用于对JSON数据进行操作
        Gson gson = new Gson();

        // 创建参数映射表
        Map<String, Object> params = new HashMap<>();
        params.put(MODEL_KEY, model);
        params.put(MESSAGES_KEY, messagesDTO);

        // 将参数转换为JSON格式的字符串
        String json = gson.toJson(params);

        // 创建用于发送http请求的实体，包含了刚才生成的json字符串以及内容类型信息
        HttpEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

        // 发送POST请求，并获取返回结果
        String result = sendPost(OpenAiApiType.CHAT_COMPLETIONS.getPath(), stringEntity);

        // 解析返回的结果字符串为Json对象
        JsonObject resultJson = gson.fromJson(result, JsonObject.class);

        // 获取"choices" Json数组
        JsonArray choices = resultJson.getAsJsonArray(CHOICES_KEY);

        // 检查"choices"是否为空或者不存在
        if (choices != null && choices.size() > 0) {
            // 获取最后一个"choice"元素
            JsonObject lastChoice = choices.get(choices.size() - 1).getAsJsonObject();
            // 获取"message" Json对象
            JsonObject message = lastChoice.getAsJsonObject(MESSAGE_KEY);
            // 获取并返回"content"字段的值
            return message.get(CONTENT_KEY).getAsString();
        }

        // 如果没有找到"choices"或者"choices"为空，则抛出异常或返回相应提示
        throw new IllegalStateException("No choices found in the response");
    }

    /**
     * 使用指定的GPT模型、消息和提示词与GPT进行交互
     *
     * @param model    GPT模型名称
     * @param messages 消息
     * @param prompt   提示词
     * @return 交互结果
     */
    @Override
    public String generateText(String model, String messages, String prompt) throws IOException {
        // 替换提示词中的{{input}}占位符为messages
        messages = prompt.replace(INPUT_PLACEHOLDER, messages);

        // 创建一个MessageVO对象，设置role和content
        List<MessageDTO> messagesDTO = new ArrayList<>();
        messagesDTO.add(new MessageDTO(USER_ROLE, messages));

        // 调用generateText方法发送给GPT模型
        return generateText(model, messagesDTO);
    }

    /**
     * 发送POST请求
     *
     * @param apiPath API路径
     * @param entity  请求体
     * @return 响应内容
     */
    private String sendPost(String apiPath, HttpEntity entity) throws IOException {
        try (CloseableHttpClient httpClient = createHttpClientWithoutCookies()) {
            // 创建 post 请求
            HttpPost httpPost = new HttpPost(openAIConfig.getBaseUrl() + apiPath);

            // 设置请求体和头信息
            httpPost.setEntity(entity);
            httpPost.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + openAIConfig.getApiKey());

            // 如果实体是StringEntity，则设置Content-Type为application/json
            if (entity instanceof StringEntity) {
                httpPost.setHeader(CONTENT_TYPE, JSON_CONTENT_TYPE);
            }

            // 执行请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS_STATUS) {
                    // 返回响应内容
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new IOException("Unexpected response status: " + response.getStatusLine().getStatusCode());
                }
            }
        }
    }

    /**
     * 创建一个不使用Cookie的CloseableHttpClient实例。
     *
     * @return 返回一个不管理Cookie的HttpClient实例
     */
    private CloseableHttpClient createHttpClientWithoutCookies() {
        return HttpClientBuilder.create()
                // 禁用 Cookie 管理
                .disableCookieManagement()
                .build();
    }

}
