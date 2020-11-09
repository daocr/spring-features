package org.spring.data.jpa.bean;

import lombok.Data;
import org.spring.data.jpa.event.UsersSaveEvent;
import org.springframework.data.domain.DomainEvents;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author daocr
 * @date 2020/11/6
 */

@Data
public class Users {

    private Integer id;
    private String name;

    //该方法会在 Users.save()调用时被触发调用
    @DomainEvents
    Collection<UsersSaveEvent> domainEvents() {
        return Arrays.asList(new UsersSaveEvent(this.id));
    }

}
