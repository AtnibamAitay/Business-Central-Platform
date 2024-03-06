package space.atnibam.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: OpenAiApiType
 * @Description: OpenAI接口类型
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-13 21:50
 **/
@AllArgsConstructor
@Getter
public enum OpenAiApiType {

    /**
     * 文本生成接口
     */
    CHAT_COMPLETIONS("/v1/chat/completions"),

    /**
     * 语音转文字接口
     */
    AUDIO_TRANSCRIPTIONS("/v1/audio/transcriptions"),

    /**
     * 文本转换为音频接口
     */
    AUDIO_SPEECH("/v1/audio/speech");

    /**
     * 接口路径
     */
    private final String path;
}
