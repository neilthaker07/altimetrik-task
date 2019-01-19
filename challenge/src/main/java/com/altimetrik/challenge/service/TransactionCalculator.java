package com.altimetrik.challenge.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.altimetrik.challenge.model.Transaction;

@Component
public class TransactionCalculator extends TimerTask{

	Queue<Transaction> queue = new LinkedList();
	Map<String, String> stats = new HashMap();
	double sum=0.0;
	double avg=0.0;
	double max=0.0;
	double min=0.0;
	int count=0;
	
	public HttpStatus saveTransaction(Map<String, Object> input)
	{
		HttpStatus hs = null;
		try {
			double amount = Double.valueOf((String)input.get("amount"));
			//String time = (String)input.get("time");
			DateTime currentTime =  new org.joda.time.DateTime();
			String time = currentTime.toString();//"2019-01-16T07:01:12.534Z"

			DateTime dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(time);
			System.out.println(dateTime);
			
			// no case for future time
			if(dateTime.getMillis() < currentTime.minusMinutes(1).getMillis())
			{
				hs = HttpStatus.NO_CONTENT;
			}
			else
			{
				Transaction t1 = new Transaction(amount, dateTime);
				queue.add(t1);
				this.sum += amount;
				this.count++;
				this.avg = this.sum / this.count;
				
				hs = HttpStatus.CREATED;
			}
		}
		catch(Exception e)
		{
			hs = HttpStatus.BAD_REQUEST;
		}
		
		return hs;
	}

	public Map<String, String> getStats()
	{
		stats.put("sum", String.valueOf(this.sum));
		stats.put("avg", String.valueOf(this.avg));
		stats.put("max", String.valueOf(this.max));
		stats.put("min", String.valueOf(this.min));
		stats.put("count", String.valueOf(this.count));
		
		return stats;
	}
	
	public void deleteTransactions()
	{
		this.queue.clear();

		this.sum=0.0;
		this.avg=0.0;
		this.max=0.0;
		this.min=0.0;
		this.count=0;
	}

	@Override
	public void run() {
		DateTime currentTime =  new org.joda.time.DateTime();
		currentTime = currentTime.minusMinutes(1);
		
		if(this.queue.isEmpty())
		{
			this.sum=0.0;
			this.avg=0.0;
			this.max=0.0;
			this.min=0.0;
			this.count=0;
		}
		
		while(!this.queue.isEmpty() && this.queue.peek().getTime().getMillis() < currentTime.getMillis())
		{
			Transaction removed = this.queue.poll();
			if(removed != null)
			{
				this.sum = this.sum - removed.getAmount();
				this.count--;
				this.avg = this.count > 0 ? (this.sum / this.count) : 0.0;	// min stack, max stack
			}
			else
			{
				break;
			}
		}		
	}
}
