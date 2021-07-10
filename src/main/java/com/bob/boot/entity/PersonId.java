package com.bob.boot.entity;

import java.io.Serializable;

/**
 * person类
 * 
 * @author bob
 *
 */
public class PersonId implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	
	public PersonId(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return "PersonId{" +
        		"id='" + id + '\''
                 +"}";
    }
}