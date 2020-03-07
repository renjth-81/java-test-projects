package com.ren.streams;

public class Invoice {
	private int id;
	private String age;
	private int invAmount;
	
	
	public Invoice(int id, String age, int invAmount) {
		super();
		this.id = id;
		this.age = age;
		this.invAmount = invAmount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public int getInvAmount() {
		return invAmount;
	}
	public void setInvAmount(int invAmount) {
		this.invAmount = invAmount;
	}
	
	

}
