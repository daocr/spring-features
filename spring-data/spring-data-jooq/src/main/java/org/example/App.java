
package org.example;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.hellokoding.jooq.model.Tables.MP_QUESTION;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        String user = "tbs";
        String password = "LangBoshi^666";
        String url = "jdbc:mysql://mysql.test.tuboshi.co:3306/sHouseApp_pre?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String driver = "com.mysql.jdbc.Driver";

        Class.forName(driver).newInstance();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
            Result<Record> fetch = dslContext.select().from(MP_QUESTION)
                    .where(MP_QUESTION.CITY_ID.in(604, 605, 606, 607, 626, 635, 634, 621))
                    .fetch();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
