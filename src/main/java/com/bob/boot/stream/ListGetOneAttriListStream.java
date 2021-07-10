package com.bob.boot.stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bob.boot.entity.Person;
import com.bob.boot.util.DateTimeUtil;

/**
 * 提取name的list列表，输出name
 * 
 * @author bob
 *
 */
public class ListGetOneAttriListStream {
	public static void main(String[] args) {
		List<Person> ps = new ArrayList<Person>();
		for (int i = 0; i < 3; i++) {
			Person p = new Person();
			p.setBirthDay(DateTimeUtil.formatAll(DateTimeUtil.addDays(new Date(), -i)));
			p.setAge(i);
			p.setId("id-" + i);
			p.setName("name-" + i);
			p.setScore(i);
			ps.add(p);
		}
		
		for (int i = 0; i < 2; i++) {
			Person p = new Person();
			p.setBirthDay(DateTimeUtil.formatAll(DateTimeUtil.addDays(new Date(), -i)));
			p.setAge(i);
			p.setId("id-" + i);
			p.setName("name-" + i);
			p.setScore(i);
			ps.add(p);
		}

		System.out.println("--------------------提取name的list列表，输出name----------------------------------");
		//1.提取出list对象中的一个属性
		List<String> nameList = ps.stream().map(Person::getName).collect(Collectors.toList());
		//提取后输出name
		nameList.forEach(s-> System.out.println(s));
		System.out.println("--------------------提取name的list列表并去重，输出name----------------------------------");
		//2.提取出list对象中的一个属性并去重
		List<String> nameList2 = ps.stream().map(Person::getName).distinct().collect(Collectors.toList());
		nameList2.forEach(s-> System.out.println(s));
	}
}