package space.atnibam.cms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.api.cms.RemoteCommentService;
import space.atnibam.cms.service.CommentService;
import space.atnibam.common.core.domain.R;

import javax.annotation.Resource;

/**
 * @ClassName: CommentController
 * @Description: 处理与评论相关的控制器。
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-20 13:54
 **/
@Api(tags = "评论接口")
@RestController
@RequestMapping("/api/comments")
public class CommentController implements RemoteCommentService {

    @Resource
    private CommentService commentService;

    /**
     * 根据对象ID获取评论树
     * 这个端点允许你获取特定对象的评论树列表
     *
     * @param objectId 需要获取评论的对象的ID
     * @param pageNum  需要获取的页码，默认值为1
     * @param pageSize 每一页的大小，默认值为10
     * @return 返回构建的评论树
     */
    @Override
    @ApiOperation(value = "通过对象ID获取评论树")
    @GetMapping("/nested")
    public R getNestedCommentsByObjectId(
            @ApiParam(value = "需要获取评论的对象的ID", required = true)
            @RequestParam Integer objectId,
            @ApiParam(value = "需要检索的评论的页数", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "要检索的评论每一页的大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "对象类型")
            @RequestParam String objectType) {
        return commentService.getNestedCommentsByObjectId(objectId, pageNum, pageSize, objectType);
    }

    /**
     * 根据对象ID获取评论（不含子评论）
     *
     * @param objectId 需要获取评论的对象的ID
     * @param pageNum  需要获取的页码，默认值为1
     * @param pageSize 每一页的大小，默认值为10
     * @return 返回对象id的顶层评论
     */
    @Override
    @ApiOperation(value = "通过对象ID获取评论（不含子评论）")
    @GetMapping("/top/level")
    public R getTopLevelCommentsByObjectId(
            @ApiParam(value = "需要获取评论的对象的ID", required = true)
            @RequestParam Integer objectId,
            @ApiParam(value = "需要检索的评论的页数", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "要检索的评论每一页的大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "对象类型")
            @RequestParam String objectType) {
        return commentService.getTopLevelCommentsByObjectId(objectId, pageNum, pageSize, objectType);
    }

    /**
     * 获取指定对象ID关联的未删除评论的平均评分.
     *
     * @param objectId 对象ID
     * @return 包含平均评分的响应，如果没有评分则返回空
     */
    @Override
    @ApiOperation(value = "获取指定对象ID关联的未删除评论的平均评分")
    @GetMapping("/average-grade")
    public R getAverageGrade(@RequestParam int objectId, @RequestParam String objectType) {
        return commentService.getAverageGradeByObjectId(objectId, objectType);
    }
}