package com.altimetrik.challenge.service;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks{
	
	@Autowired
	TransactionCalculator transactionCalculator;
	
	@Scheduled(fixedRate = 1000)
	public void scheduleTaskWithFixedRate() {
		System.out.println(System.currentTimeMillis());
		Timer timer = new Timer();
		timer.schedule(transactionCalculator, 0, 1000);
	}

}
