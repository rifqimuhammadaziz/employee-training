package com.rifqimuhammadaziz.employeetraining.utility;

import com.rifqimuhammadaziz.employeetraining.model.oauth.Client;
import com.rifqimuhammadaziz.employeetraining.model.oauth.Role;
import com.rifqimuhammadaziz.employeetraining.model.oauth.RolePath;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.ClientRepository;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.RolePathRepository;
import com.rifqimuhammadaziz.employeetraining.repository.oauth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
@RequiredArgsConstructor
public class DatabaseSeeder implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository oAuthRoleRepository;
    private final ClientRepository oAuthClientRepository;
    private final RolePathRepository oAuthRolePathRepository;

    private List<String> roles = List.of(
            "ROLE_SUPERUSER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_ADMIN:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_USER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_READ:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_WRITE:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS"
    );
    private List<String> clients = List.of(
            "my-client-apps:ROLE_READ ROLE_WRITE", // mobile
            "my-client-web:ROLE_READ ROLE_WRITE" // web
    );

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.insertRole();
        this.insertClients();
    }

    @Transactional
    public void insertRole() {
        for (String role : roles) {
            List<String> str = List.of(role.split(":"));
            String name = str.get(0);
            String type = str.get(1);
            String pattern = str.get(2);
            List<String> methods = List.of(str.get(3).split("\\|"));
            Role oldOAuthRole = oAuthRoleRepository.findOneByName(name);

            if (oldOAuthRole == null) {
                oldOAuthRole = new Role();
                oldOAuthRole.setName(name);
                oldOAuthRole.setType(type);
                oldOAuthRole.setRolePaths(new ArrayList<>());

                for (String m : methods) {
                    String rolePathName = String.format("%s_%s", name.toLowerCase(), m.toLowerCase());
                    RolePath oAuthRolePath = oAuthRolePathRepository.findOneByName(rolePathName);

                    if (oAuthRolePath == null) {
                        oAuthRolePath = new RolePath();
                        oAuthRolePath.setName(rolePathName);
                        oAuthRolePath.setMethod(m.toUpperCase());
                        oAuthRolePath.setPattern(pattern);
                        oAuthRolePath.setRole(oldOAuthRole);
                        oAuthRolePathRepository.save(oAuthRolePath);
                        oldOAuthRole.getRolePaths().add(oAuthRolePath);
                    }
                }
            }
            oAuthRoleRepository.save(oldOAuthRole);
        }
    }

    @Transactional
    public void insertClients() {
        for (String c : clients) {
            List<String> s = List.of(c.split(":"));
            String clientName = s.get(0);
            List<String> clientRoles = List.of(s.get(1).split("\\s"));
            Client oldOAuthClient = oAuthClientRepository.findOneByClientId(clientName);

            if (oldOAuthClient == null) {
                oldOAuthClient = new Client();
                oldOAuthClient.setClientId(clientName);
                oldOAuthClient.setAccessTokenValiditySeconds(28800);
                oldOAuthClient.setRefreshTokenValiditySeconds(7257600);
                oldOAuthClient.setGrantTypes("password refresh_token authorization_code");
                oldOAuthClient.setClientSecret(passwordEncoder.encode("password"));
                oldOAuthClient.setApproved(true);
                oldOAuthClient.setRedirectUris("");
                oldOAuthClient.setScopes("read write");
                List<Role> oAuthRoles = oAuthRoleRepository.findByNameIn(clientRoles);

                if (!oAuthRoles.isEmpty()) {
                    oldOAuthClient.getAuthorities().addAll(oAuthRoles);
                }
            }
            oAuthClientRepository.save(oldOAuthClient);
        }
    }
}