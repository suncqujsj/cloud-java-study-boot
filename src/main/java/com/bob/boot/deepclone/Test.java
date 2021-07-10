package com.bob.boot.deepclone;

import java.util.ArrayList;
import java.util.List;

import com.bob.boot.entity.Person;

/**
 * 
 * @author bob 有两种方式： 1). 实现Cloneable接口并重写Object类中的clone()方法； 2).
 *         实现Serializable接口，通过对象的序列化和反序列化实现克隆，可以实现真正的深度克隆，代码如下：
 */
public class Test {

	public static void main(String[] args) {
		try {
			Person p1 = new Person("111", "郭靖1");
			Person p3 = new Person("222", "郭靖2");
			Person p2 = CloneUtil.clone(p1); // 深度克隆
			p2.setId("222");
			System.out.println("--------------------1.old person----------------------------------");
			System.out.println(p1);
			System.out.println("--------------------2.deep clone person----------------------------------");
			System.out.println(p2);

			ArrayList<Person> ps = new ArrayList<>();
			ps.add(p1);
			ps.add(p3);
			List<Person> ps1 = CloneUtil.clone(ps); // 深度克隆
			System.out.println("--------------------3.deep clone list----------------------------------");
			ps1.forEach(vo -> System.out.println(vo));
			System.out.println(
					"--------------------4.is deep clone person equals to old person clone list----------------------------------");
			System.out.println(p1.equals(ps1.get(0)));
			System.out.println(p1 == ps1.get(0));
			System.out.println(p1.getId().equals(ps1.get(0).getId()));
			System.out.println("--------------------4.for each change----------------------------------");
			ps1.forEach(vo -> {
				vo.setId("foreachid");
			});
			ps1.forEach(vo -> System.out.println(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}