package com.livecoding.blog.controller;

import com.livecoding.blog.entity.Article;
import com.livecoding.blog.entity.Category;
import com.livecoding.blog.repository.ArticleRepository;
import com.livecoding.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("articles")
    public List<Article> getAllArticles(@RequestParam(required = false) Long category) {
        return articleRepository.findAll();
    }

    @GetMapping("articles/latest")
    public List<Article> getLatestArticles() {
        return articleRepository.findFirst3ByOrderByCreatedAtAsc();
    }

    @GetMapping("/articles/{articleId}")
    public Article getArticleById(@PathVariable Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article Not Found");
        }
    }

    @PostMapping("articles")
    public Article createArticle(@RequestParam(required = true) Long category, @RequestBody Article article) {

        Category categoryToUse = categoryRepository.findById(category).get();

        article.setCategory(categoryToUse);

        return articleRepository.save(article);
    }

    @PutMapping("articles/{articleId}")
    public Article updateArticle(@PathVariable Long articleId, @RequestParam(required = true) Long category, @RequestBody Article body) {

        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        Optional<Category> optionalCategory = categoryRepository.findById(category);
        if (optionalArticle.isPresent() && optionalCategory.isPresent()) {
            Article articleToUpdate = optionalArticle.get();
            Category categoryToUse = optionalCategory.get();

            articleToUpdate.setTitle(body.getTitle());
            articleToUpdate.setContent(body.getContent());
            articleToUpdate.setSlug(body.getSlug());
            articleToUpdate.setCategory(categoryToUse);

            return articleRepository.save(articleToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }
    }

    @DeleteMapping("articles/{id}")
    public boolean deleteArticle(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return true;
    }
}
