package space.atnibam.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import space.atnibam.cms.model.entity.Comment;

import java.util.List;

/**
 * @ClassName: CommentMapper
 * @Description: 针对表【comment】的数据库操作Mapper，包含查找根评论和子评论的操作。
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-19 21:00
 **/
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据对象ID获取未删除的主评论
     *
     * @param objectId 指定查询评论的对象ID
     * @param pageNum  指定查询评论的页码
     * @param pageSize 指定查询评论的每页大小
     * @return 返回未删除的主评论列表
     */
    @Select("SELECT * FROM comment WHERE object_id = #{objectId} AND object_type in ('0', '1', '2') AND deleted = 1 AND object_type = #{objectType} LIMIT #{pageSize} OFFSET #{pageNum}")
    List<Comment> selectRootCommentByObjectId(@Param("objectId") Integer objectId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("objectType") String objectType);

    /**
     * 根据父评论ID获取未删除的子评论
     *
     * @param parentId 指定查询评论的父评论ID
     * @return 返回未删除的子评论列表
     */
    @Select("SELECT * FROM comment WHERE object_id = #{parentId} AND object_type = '3' AND deleted = 1")
    List<Comment> selectSubCommentByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据对象id查出所有未删除的评论的评分的平均值
     *
     * @param objectId 对象ID
     * @return 评分的平均值
     */
    @Select("SELECT AVG(grade) AS average_grade FROM comment WHERE object_id = #{objectId} AND deleted = 1 AND object_type = #{objectType}")
    Double selectAverageGradeByObjectId(@Param("objectId") int objectId, @Param("objectType") String objectType);
}