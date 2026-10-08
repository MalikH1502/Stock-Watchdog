package com.malikh.stockwatchdog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.malikh.stockwatchdog.entity.HelpArticle;
import com.malikh.stockwatchdog.repository.HelpRepository;

@Service 
public class HelpService {
    private final HelpRepository helpRepo;

    public HelpService(HelpRepository helpRepo){
        this.helpRepo = helpRepo;
    }

    public List<HelpArticle> getAll(){
        return helpRepo.findAll();
    }

}
