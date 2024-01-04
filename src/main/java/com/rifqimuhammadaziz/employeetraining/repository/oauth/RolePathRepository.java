package com.rifqimuhammadaziz.employeetraining.repository.oauth;

import com.rifqimuhammadaziz.employeetraining.model.oauth.RolePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RolePathRepository extends JpaRepository<RolePath,Long> {
    RolePath findOneByName(String rolePathName);
}
