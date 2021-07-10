package com.bob.boot.stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.bob.boot.entity.Person;
import com.bob.boot.util.DateTimeUtil;

public class UniqueOnSomeAttributesList {
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

		List<Person> uniqueId = ps.stream().collect(
				collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getId, (s1, s2) -> {
					return s1.compareTo(s2);
				}))), ArrayList::new));
		System.out.println("--------------------2.ps去除id重复的对象后输出----------------------------------");
		System.out.println(uniqueId);
		
		System.out.println("--------------------2.ps去除id重复的对象后再去除name重复的对象后输出,多个条件以此类推即可----------------------------------");
		List<Person> uniqueIdAndName = ps.stream().collect(
				collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getName, (s1, s2) -> {
					return s1.compareTo(s2);
				}))), ArrayList::new));
		
		System.out.println(uniqueIdAndName);
	}
}