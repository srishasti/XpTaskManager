package com.srishasti.service;

import com.srishasti.model.MetaData;
import com.srishasti.repo.MetaDataRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduledService {


    @Autowired
    private TaskService taskService;

    @Autowired
    private MetaDataRepo metaDataRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledProcess(){
        taskService.checkOverdueTasks();
        MetaData metaData = metaDataRepo.findById("lastProcessed").orElse(null);
        if(metaData == null){
            metaData = new MetaData();
            metaData.setMetakey("lastProcessed");
        }
        metaData.setValue(LocalDate.now().toString());
        metaDataRepo.save(metaData);
    }

    @PostConstruct
    public void initializeServer(){
        MetaData metaData = metaDataRepo.findById("lastProcessed").orElse(null);
        if(metaData == null || metaData.getValue() == null){
            scheduledProcess();
        }
        else{
            LocalDate lastProcessed =  LocalDate.parse(metaData.getValue());
            if(!lastProcessed.equals(LocalDate.now())){
                scheduledProcess();
            }
        }

    }

}
