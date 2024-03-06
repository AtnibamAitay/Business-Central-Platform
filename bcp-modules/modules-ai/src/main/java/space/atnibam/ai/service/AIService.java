package space.atnibam.ai.service;

import space.atnibam.ai.model.dto.MessageDTO;

import java.io.IOException;
import java.util.List;

/**
 * @InterfaceName: AIService
 * @Description: AI服务接口
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-12 21:17
 **/
public interface AIService {

    /**
     * 使用指定的GPT模型和消息列表与GPT进行交互
     *
     * @param model       GPT模型名称
     * @param messagesDTO 消息列表
     * @return 交互结果
     */
    String generateText(String model, List<MessageDTO> messagesDTO) throws IOException;

    /**
     * 使用指定的GPT模型、消息和提示词与GPT进行交互
     *
     * @param model    GPT模型名称
     * @param messages 消息
     * @param prompt   提示词
     * @return 交互结果
     */
    String generateText(String model, String messages, String prompt) throws IOException;
}
