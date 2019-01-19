package com.altimetrik.challenge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

	double amount;
	DateTime time;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public DateTime getTime() {
		return time;
	}
	public void setTime(DateTime time) {
		this.time = time;
	}
	public Transaction(double amount, DateTime time) {
		super();
		this.amount = amount;
		this.time = time;
	}

}
