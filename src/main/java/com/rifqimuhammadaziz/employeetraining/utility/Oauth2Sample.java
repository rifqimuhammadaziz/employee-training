package com.rifqimuhammadaziz.employeetraining.utility;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Oauth2Sample {

    private static final String APP_NAME = "";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
    );
    private static Oauth2 oauth2;
    private static GoogleClientSecrets clientSecrets;

    private static Credential authorize() throws Exception {
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(Objects.requireNonNull(Oauth2Sample.class.getResourceAsStream("/client_secret.json"))));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            log.info("Enter Client ID and Secret from https://code.google.com/apis/console/ into oauth2-cmdlinesample src/main/resources/client_secrets.json");
        }

        GoogleAuthorizationCodeFlow codeFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory).build();
        return new AuthorizationCodeInstalledApp(codeFlow,
                new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) throws Exception {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global OAuth instance
            oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APP_NAME).build();
            log.info("my token: {}", credential.getAccessToken());
            tokenInfo(credential.getAccessToken());
            userInfo();
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage(), throwable);
        }
    }

    private static void tokenInfo(String accessToken) throws IOException {
        Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
        log.info(tokeninfo.toPrettyString());
        if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
            log.error("ERROR: audience does not match our client ID!");
        }
    }

    private static void userInfo() throws IOException {
        Userinfoplus userinfo = oauth2.userinfo().get().execute();
        log.info(userinfo.toPrettyString());
    }

}