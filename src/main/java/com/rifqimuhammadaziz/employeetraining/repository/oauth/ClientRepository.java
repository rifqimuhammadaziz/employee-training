package com.rifqimuhammadaziz.employeetraining.repository.oauth;

import com.rifqimuhammadaziz.employeetraining.model.oauth.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findOneByClientId(String clientId);
}
