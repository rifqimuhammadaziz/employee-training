package com.rifqimuhammadaziz.employeetraining.repository.oauth;

import com.rifqimuhammadaziz.employeetraining.model.oauth.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClientRepository extends PagingAndSortingRepository<Client,Long> {
    Client findOneByClientId(String clientId);
}
