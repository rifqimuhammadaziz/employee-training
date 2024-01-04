package com.rifqimuhammadaziz.employeetraining.repository.oauth;

import com.rifqimuhammadaziz.employeetraining.model.oauth.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends PagingAndSortingRepository<Role,Long> {
    Role findOneByName(String name);
    List<Role> findByNameIn(Collection<String> name);
}
