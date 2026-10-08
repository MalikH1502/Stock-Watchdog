package com.malikh.stockwatchdog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malikh.stockwatchdog.entity.HelpArticle;
import com.malikh.stockwatchdog.service.HelpService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/api")
public class HelpController {
    private HelpService helpService;

    public HelpController(HelpService helpService){
        this.helpService = helpService;
    }
    
    @GetMapping("/help")
    public List<HelpArticle> returnAllArticles() {
        return helpService.getAll();
    }
    
}
