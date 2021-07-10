package com.bob.boot.stream;

import java.util.ArrayList;
import java.util.List;

import com.bob.boot.entity.Person;

/**
 *   获取list中id等于指定值的对象
 * @author bob
 *
 */
public class ListFindOneIdObject {
	
	public static void main(String[] args) {
		List<Person> pers = new ArrayList<>();
		pers.add(new Person("1","张三"));
		pers.add(new Person("2","李四"));
		pers.add(new Person("3","王五"));
		pers.add(new Person("4","赵六"));
		
	    String studentId = "3";
	    Person per = pers.stream().filter(o -> o.getId() == studentId).findAny().orElse(null);
	    System.out.println(per);
	}
}