package com.bob.boot.copyproperties;

import org.springframework.beans.BeanUtils;

import com.bob.boot.entity.Person;
import com.bob.boot.entity.PersonId;
import com.bob.boot.entity.PersonIntName;

/**
 * @author bob       
 */
public class Test {

	public static void main(String[] args) {
			Person p1 = new Person("111", "郭靖111");
			Person p2 = new Person("222", "郭靖222");
			System.out.println("--------------------1.old person----------------------------------");
			System.out.println(p1);
			PersonIntName p3 = new PersonIntName("333", 333);
			System.out.println("--------------------2.old personintname----------------------------------");
			System.out.println(p3);
			BeanUtils.copyProperties(p3, p1);
			System.out.println("--------------------3.copy cant int properties----------------------------------");
			System.out.println(p1);
			System.out.println("--------------------4.copy can same str properties----------------------------------");
			BeanUtils.copyProperties(p2, p1);
			System.out.println(p1);
			System.out.println("--------------------5.child properties copy----------------------------------");
			PersonId id = new PersonId("idid");
			BeanUtils.copyProperties(id, p1);
			System.out.println(p1);
	}
}