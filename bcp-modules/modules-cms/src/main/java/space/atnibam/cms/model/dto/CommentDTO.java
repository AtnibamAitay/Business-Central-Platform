package space.atnibam.cms.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentDTO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 评论ID
     */
    private Integer id;
    /**
     * 评论用户的信息
     */
    private CommentUserInfoDTO commentUserInfo;
    /**
     * 对象ID（文章/视频/商品/父评论）
     */
    private Integer objectId;
    /**
     * 评分
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer grade;
    /**
     * 内容
     */
    private String content;
    /**
     * 内容类型（0代表文本，1代表表情包，2代表图片）
     */
    private Integer type;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 评论用户的用户名和头像
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentUserInfoDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 用户ID-主键
         */
        private Integer userId;
        /**
         * 用户名
         */
        private String userName;
        /**
         * 头像
         */
        private String userAvatar;
    }
}