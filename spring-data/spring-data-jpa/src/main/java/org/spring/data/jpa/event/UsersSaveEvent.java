package org.spring.data.jpa.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersSaveEvent {

    private Integer id;

}