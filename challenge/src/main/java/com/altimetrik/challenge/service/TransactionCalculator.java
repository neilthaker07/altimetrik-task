package com.altimetrik.challenge.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
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
	PriorityQueue<Transaction> pqMin = new PriorityQueue<Transaction>(
			new Comparator<Transaction>(){
				public int compare(Transaction t1, Transaction t2)
				{
					return (int) (t1.getAmount()-t2.getAmount());
				}
			}
			);
	
	PriorityQueue<Transaction> pqMax = new PriorityQueue<Transaction>(
			new Comparator<Transaction>(){
				public int compare(Transaction t1, Transaction t2)
				{
					return (int) (t2.getAmount()-t1.getAmount());
				}
			}
			);
	
	double sum=0.0;
	double avg=0.0;
	double max=0.0;
	double min=0.0;
	long count=0;
	
	public synchronized HttpStatus saveTransaction(Map<String, Object> input)
	{
		HttpStatus hs = null;
		try {
			Set<String> set = input.keySet();
			if(set.size()>2)
			{
				throw new Exception();
			}
			
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
				
				sum += amount;
				count++;
				avg = sum/count;
				pqMin.add(t1);
				pqMax.add(t1);
				
				hs = HttpStatus.CREATED;
			}
		}
		catch(Exception e)
		{
			hs = HttpStatus.BAD_REQUEST;
		}
		
		return hs;
	}

	public synchronized Map<String, String> getStats()
	{
		stats.put("sum", String.valueOf(sum));
		stats.put("avg", String.valueOf(avg));
		stats.put("max", String.valueOf(pqMax.size() == 0 ? 0.0 : pqMax.peek().getAmount()));
		stats.put("min", String.valueOf(pqMin.size() == 0 ? 0.0 : pqMin.peek().getAmount()));
		stats.put("count", String.valueOf(count));
		
		return stats;
	}
	
	public synchronized void deleteTransactions()
	{
		queue.clear();
		pqMin.clear();
		pqMax.clear();

		sum=0.0;
		avg=0.0;
		max=0.0;
		min=0.0;
		count=0;
	}

	@Override
	public synchronized void run() {
		
		if(queue.isEmpty())
		{
			sum=0.0;
			avg=0.0;
			max=0.0;
			min=0.0;
			count=0;
			
			pqMin.clear();
			pqMax.clear();
		}
		else
		{
			DateTime currentTime =  new org.joda.time.DateTime();
			currentTime = currentTime.minusMinutes(1);

			while(!queue.isEmpty() && queue.peek().getTime().getMillis() < currentTime.getMillis())
			{
				Transaction removed = queue.poll();
				if(removed != null)
				{
					sum = sum - removed.getAmount();
					count--;
					avg = count > 0 ? (sum / count) : 0.0;
					pqMin.remove(removed);
					pqMax.remove(removed);
				}
				else
				{
					break;
				}
			}	
		}
	}
}
