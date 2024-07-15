package org.example;

import org.example.generator.mapper.LeadsGroupMapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
        LeadsGroupMapper mapper = context.getBean(LeadsGroupMapper.class);

        System.out.println(mapper.selectByPrimaryKey(1L));

    }
}
