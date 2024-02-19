package space.atnibam.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.ums.model.dto.FriendRequestQueryResultDTO;
import space.atnibam.ums.model.entity.FriendRequest;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_request(增添好友请求表)】的数据库操作Mapper
 * @createDate 2024-02-18 17:48:12
 * @Entity space.atnibam.ums.model.entity.FriendRequest
 */
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {
    @Select("SELECT request_id AS requestId, user_id_adding AS requesterId, message, status_code AS statusCode " +
            "FROM friend_request WHERE user_id_added = #{userId}")
    List<FriendRequestQueryResultDTO> getFriendRequestsByUserId(int userId);
}