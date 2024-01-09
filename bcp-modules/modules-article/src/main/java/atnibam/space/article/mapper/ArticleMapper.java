package atnibam.space.article.mapper;

import atnibam.space.article.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author huanghuiyuan
 * @description 针对表【article】的数据库操作Mapper
 * @createDate 2023-08-16 15:15:05
 * @Entity entity.model.article.atnibam.space.Article
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}




