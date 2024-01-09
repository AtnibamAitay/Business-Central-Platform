package atnibam.space.article.service.impl;

import atnibam.space.article.model.dto.ArticleDTO;
import atnibam.space.article.model.entity.Article;
import cn.hutool.http.HtmlUtil;
import atnibam.space.article.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import atnibam.space.article.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author huanghuiyuan
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2023-08-16 15:15:05
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;


    @Override
    public void publish(ArticleDTO articleDTO) {
        //获取文章标题和内容
        String title = articleDTO.getTitle();
        String originContent = articleDTO.getContent();
        //对原始的HtmlStr特殊字符进行转义
        String handledContent = HtmlUtil.escape(originContent);
        //创建Article对象
        Article article = new Article();
        article.setTitle(title);
        article.setContent(handledContent);

        //写入数据库
        articleMapper.insert(article);
    }
}




