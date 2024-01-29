package space.atnibam.cms.service.impl;

import space.atnibam.cms.model.dto.ArticleDTO;
import space.atnibam.cms.model.entity.Article;
import cn.hutool.http.HtmlUtil;
import space.atnibam.cms.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import space.atnibam.cms.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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




