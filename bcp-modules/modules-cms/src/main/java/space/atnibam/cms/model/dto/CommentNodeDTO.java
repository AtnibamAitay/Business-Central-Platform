package space.atnibam.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CommentNode
 * @Description: 评论节点类，包含评论本身以及其子评论
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-19 22:07
 **/
@Getter
@Setter
@NoArgsConstructor
public class CommentNodeDTO {
    /**
     * 评论
     */
    @ApiModelProperty("评论")
    private CommentDTO comment;

    /**
     * 子评论列表
     */
    @ApiModelProperty("子评论列表")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentNodeDTO> children = new ArrayList<>();

    /**
     * 构造函数，用于创建一个新的CommentNode实例
     *
     * @param comment 要包含在新节点中的评论
     */
    public CommentNodeDTO(CommentDTO comment) {
        this.comment = comment;
    }
}
