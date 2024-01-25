package atnibam.space.cms.controller;

import atnibam.space.cms.model.dto.ArticleDTO;
import atnibam.space.cms.service.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping
    public void publishArticle(ArticleDTO articleDTO) {
        articleService.publish(articleDTO);
    }
}
