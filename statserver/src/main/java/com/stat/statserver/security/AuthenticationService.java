package com.stat.statserver.security;

import com.stat.statserver.exceptions.InvalidApiKeyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    public static Authentication getAuthentication(HttpServletRequest request,  String appApiKey) {

// TODO так норм? фактически здесь задается не только api key но и uri для которого идет авторизация
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if ((apiKey == null || !apiKey.equals(appApiKey)) && request.getRequestURI().startsWith("/stat")) {
            throw new InvalidApiKeyException();
        }
        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }

}
