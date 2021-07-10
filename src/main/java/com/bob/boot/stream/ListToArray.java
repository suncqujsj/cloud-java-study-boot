package com.bob.boot.stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.bob.boot.entity.Person;
import com.bob.boot.util.DateTimeUtil;

public class ListToArray {
	public static void main(String[] args) {
		List<Person> ps = new ArrayList<Person>();
		for (int i = 0; i < 3; i++) {
			Person p = new Person();
			p.setBirthDay(DateTimeUtil.formatAll(DateTimeUtil.addDays(new Date(), -i)));
			p.setAge(i);
			p.setId("id-" + i);
			p.setName("name-" + i);
			ps.add(p);
		}

		for (int i = 0; i < 2; i++) {
			Person p = new Person();
			p.setBirthDay(DateTimeUtil.formatAll(DateTimeUtil.addDays(new Date(), -i)));
			p.setAge(i);
			p.setId("id-" + i);
			p.setName("name-" + i);
			ps.add(p);
		}

		System.out.println("--------------------1.ps所有元素输出----------------------------------");
		System.out.println(ps);
		
		System.out.println("--------------------2.ps转数组后所有元素输出----------------------------------");
		Person[] pers = ps.stream().toArray(Person[]::new);
		Arrays.asList(pers).stream().forEach(System.out::println);
	}
}