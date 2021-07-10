package com.bob.boot.entity;

import java.io.Serializable;

/**
 * person类
 * 
 * @author bob
 *
 */
public class Person implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private int score;
	private int age;
	private String birthDay;

	public Person() {

	}

	public Person(String name) {
		super();
		this.name = name;
	}
	
	public Person(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	
	@Override
    public String toString() {
        return "Person{" +
        		"id='" + id + '\''+
                "name='" + name + '\'' +
                '}';
    }
}