package com.aurora.auroralike.service;


import com.aurora.auroralike.entity.Article;
import com.aurora.auroralike.model.dto.ArticleRankListDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;



public interface ArticleService  extends IService<Article> {

    List<ArticleRankListDTO> listArticlesTop();

}
