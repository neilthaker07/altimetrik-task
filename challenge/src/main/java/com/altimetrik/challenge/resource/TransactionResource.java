package com.altimetrik.challenge.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.altimetrik.challenge.service.TransactionCalculator;

@RestController
@CrossOrigin
public class TransactionResource {

	@Autowired
	TransactionCalculator transactionCalculator;
	
	@RequestMapping(method=RequestMethod.POST, value="/saveTransaction")
	public ResponseEntity saveTransaction(@RequestBody Map<String, Object> input)
	{
		HttpStatus hs = transactionCalculator.saveTransaction(input);
		return new ResponseEntity(hs);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getStats")
	public Map<String, String> getStats()
	{
		return transactionCalculator.getStats();
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/deleteTransactions")
	public ResponseEntity deleteTransactions()
	{
		transactionCalculator.deleteTransactions();
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
