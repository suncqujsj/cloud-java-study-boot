package com.bob.boot.entity;

import java.io.Serializable;

/**
 * person类
 * 
 * @author bob
 *
 */
public class PersonIntName implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private int name;
	
	public PersonIntName(String id, int name) {
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
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	@Override
    public String toString() {
        return "Person{" +
        		"id='" + id + '\''+
                "name=" + name +"}";
    }
}