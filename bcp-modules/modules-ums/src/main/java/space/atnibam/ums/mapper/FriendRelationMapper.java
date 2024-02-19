package space.atnibam.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.ums.model.entity.FriendRelation;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_relation(好友表)】的数据库操作Mapper
 * @createDate 2024-02-18 17:48:12
 * @Entity space.atnibam.ums.model.entity.FriendRelation
 */
public interface FriendRelationMapper extends BaseMapper<FriendRelation> {
    /**
     * 根据用户ID查询该用户的好友关系列表
     *
     * @param ownId 用户ID
     * @return 好友id列表
     */
    @Select("SELECT friend_id FROM friend_relation WHERE own_id = #{ownId}")
    List<Integer> getFriendRelationsByOwnId(int ownId);
}