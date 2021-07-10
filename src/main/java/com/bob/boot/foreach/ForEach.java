package com.bob.boot.foreach;

import java.util.ArrayList;
import java.util.List;

import com.bob.boot.entity.Person;

/**
 * @author bob
 */
public class ForEach {

	public static void main(String[] args) {

		List<Person> ps = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Person p = new Person(i+"", "郭靖"+i);
			ps.add(p);
		}

		ps.forEach(vo->{
			System.out.println(vo);
		});
	}
}