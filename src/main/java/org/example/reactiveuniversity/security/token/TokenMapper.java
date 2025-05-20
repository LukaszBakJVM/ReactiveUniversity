package org.example.reactiveuniversity.security.token;

import org.springframework.stereotype.Service;

@Service
public class TokenMapper {
    TokenEntity saveToken(TokenSaveDto dto){
        TokenEntity token = new TokenEntity();
        token.setEmail(dto.email());
        token.setToken(dto.token());
        return token;
    }
    GetToken getToken (TokenEntity entity){
        return new GetToken(entity.getToken());
    }
}
