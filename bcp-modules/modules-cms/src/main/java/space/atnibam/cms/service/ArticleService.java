package space.atnibam.cms.service;

import space.atnibam.cms.model.dto.ArticleDTO;
import space.atnibam.cms.model.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArticleService extends IService<Article> {

    void publish(ArticleDTO articleDTO);
}
