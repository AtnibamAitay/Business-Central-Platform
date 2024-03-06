package space.atnibam.api.ai.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: MessageVO
 * @Description: 定义了一个消息类，包含角色和内容两个字段
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-13 09:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    @ApiModelProperty(value = "角色名称")
    private String role;

    @ApiModelProperty(value = "消息内容")
    private String content;
}
