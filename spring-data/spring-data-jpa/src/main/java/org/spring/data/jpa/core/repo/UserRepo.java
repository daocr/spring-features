package org.spring.data.jpa.core.repo;

import org.spring.data.jpa.bean.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author daocr
 * @date 2020/11/6
 */
public interface UserRepo extends CrudRepository<Integer, Users> {

    List<Users> findByLastname(String lastname);
}
