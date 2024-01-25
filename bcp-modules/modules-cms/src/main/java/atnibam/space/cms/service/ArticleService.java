package atnibam.space.cms.service;

import atnibam.space.cms.model.dto.ArticleDTO;
import atnibam.space.cms.model.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArticleService extends IService<Article> {

    void publish(ArticleDTO articleDTO);
}
