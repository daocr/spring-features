package org.example;

import org.example.generator.domain.LeadsGroup;
import org.example.generator.mapper.LeadsGroupMapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
        LeadsGroupMapper mapper = context.getBean(LeadsGroupMapper.class);

        System.out.println(mapper.selectByPrimaryKey(1L));

        List<String> list = Arrays.asList("'[1]'", "'[2]'");

        LeadsGroup leadsGroup = mapper.selectByIdIn(list);
    }
}
