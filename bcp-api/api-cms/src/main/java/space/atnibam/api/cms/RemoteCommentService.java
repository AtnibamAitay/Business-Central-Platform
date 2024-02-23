package space.atnibam.api.cms;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.atnibam.common.core.domain.R;

/**
 * @InterfaceName: RemoteCommentService
 * @Description: 远程评论服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-03 10:53
 **/
@FeignClient(value = "modules-cms", contextId = "comment", url = "http://local.atnibam.space:9021")
public interface RemoteCommentService {

    /**
     * 根据对象ID获取评论树
     * 这个端点允许你获取特定对象的评论树列表
     *
     * @param objectId 需要获取评论的对象的ID
     * @param pageNum  需要获取的页码，默认值为1
     * @param pageSize 每一页的大小，默认值为10
     * @return 返回构建的评论树
     */
    @GetMapping("/api/comments/nested")
    R getNestedCommentsByObjectId(
            @ApiParam(value = "需要获取评论的对象的ID", required = true)
            @RequestParam Integer objectId,
            @ApiParam(value = "需要检索的评论的页数", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "要检索的评论每一页的大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "对象类型")
            @RequestParam String objectType);

    /**
     * 根据对象ID获取评论（不含子评论）
     *
     * @param objectId 需要获取评论的对象的ID
     * @param pageNum  需要获取的页码，默认值为1
     * @param pageSize 每一页的大小，默认值为10
     * @return 返回对象id的顶层评论
     */
    @GetMapping("/api/comments/top/level")
    R getTopLevelCommentsByObjectId(
            @ApiParam(value = "需要获取评论的对象的ID。", required = true)
            @RequestParam Integer objectId,
            @ApiParam(value = "需要检索的评论的页数。", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "要检索的评论每一页的大小。", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "对象类型")
            @RequestParam String objectType);

    @GetMapping("/api/comments/average-grade")
    R getAverageGrade(@RequestParam int objectId, @RequestParam String objectType);
}