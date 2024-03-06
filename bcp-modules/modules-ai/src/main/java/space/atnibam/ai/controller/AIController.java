package space.atnibam.ai.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.atnibam.ai.model.dto.MessageDTO;
import space.atnibam.ai.service.AIService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: AIController
 * @Description: AI服务控制器
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-13
 **/
@Api(value = "AI模块", tags = "AI模块")
@RestController
@RequestMapping("/api/ai")
@Slf4j
public class AIController {

    @Resource
    private AIService aiService;

    /**
     * 处理与GPT模型交互的请求（无流式输出）
     *
     * @param model       GPT模型名称
     * @param messagesDTO 消息列表
     * @return ResponseEntity 包含交互结果或错误消息
     */
    @ApiOperation("与GPT交互接口")
    @PostMapping("/interact-with-gpt")
    public ResponseEntity<String> generateTextWithGpt(
            @RequestParam String model,
            @RequestBody List<MessageDTO> messagesDTO) {

        try {
            String result = aiService.generateText(model, messagesDTO);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            // 如果发生异常，返回错误响应
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

}