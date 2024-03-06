package space.atnibam.api.ai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import space.atnibam.api.ai.model.dto.MessageDTO;

import java.util.List;

/**
 * @InterfaceName: RemoteAiService
 * @Description: 远程AI服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-03-06 15:25
 **/
@FeignClient(value = "modules-ai", contextId = "ai")
public interface RemoteAiService {

    /**
     * 处理与GPT模型交互的请求（无流式输出）
     *
     * @param model       GPT模型名称
     * @param messagesDTO 消息列表
     * @return ResponseEntity 包含交互结果或错误消息
     */
    @PostMapping("/api/ai/interact-with-gpt")
    ResponseEntity<String> generateTextWithGpt(
            @RequestParam String model,
            @RequestBody List<MessageDTO> messagesDTO);
}