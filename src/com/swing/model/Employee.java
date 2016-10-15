package com.swing.model;

public class Employee {
	
	public Employee(Integer id, String name, String surname, String position, Integer salary) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.position = position;
		this.salary = salary;
	}
	
	public Employee() {
		super();
	}
	
	private Integer id;
	private String name;
	private String surname;
	private String position;
	private Integer salary;
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
}