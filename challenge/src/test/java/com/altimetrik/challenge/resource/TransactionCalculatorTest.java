package com.altimetrik.challenge.resource;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.altimetrik.challenge.service.TransactionCalculator;

import junit.framework.Assert;

public class TransactionCalculatorTest {

	TransactionCalculator tc ;
	
	@Before
	public void setup()
	{
		tc = new TransactionCalculator();
	}
	
	@Test
	public void testSaveTransaction_CREATED() {
		
		Map<String, Object> input = new HashMap();
		input.put("amount", "89.50");
		input.put("time", "2019-01-19T07:01:12.534Z");

		HttpStatus hs = tc.saveTransaction(input);
		
		Assert.assertEquals(HttpStatus.CREATED, hs);
	}
	
	@Test
	public void testSaveTransaction_BAD_REQUEST() {
		
		Map<String, Object> input = new HashMap();
		input.put("amount", "name");
		input.put("time", "2019-01-16T07:01:12.534Z");

		HttpStatus hs = tc.saveTransaction(input);
		
		Assert.assertEquals(HttpStatus.BAD_REQUEST, hs);
	}
	
	/*@Test
	public void testSaveTransaction_NO_CONTENT() {
		
		Map<String, Object> input = new HashMap();
		input.put("amount", "89.50");
		input.put("time", "2019-01-16T07:01:12.534Z");

		HttpStatus hs = tc.saveTransaction(input);
		
		Assert.assertEquals(HttpStatus.NO_CONTENT, hs);
	}*/
	
	@Test
	public void testGetStats() {
		
		Map<String, Object> input = new HashMap();
		input.put("amount", "75.25");
		input.put("time", "2019-01-16T07:01:12.534Z");
		tc.saveTransaction(input);
		
		input.put("amount", "10.25");
		input.put("time", "2019-01-16T07:01:12.534Z");
		tc.saveTransaction(input);
		
		Map<String, String> stats = tc.getStats();
		Assert.assertEquals("2", stats.get("count"));
		Assert.assertEquals("85.5", stats.get("sum"));
	}

}

