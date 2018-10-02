package com.ws.inpe.configuration;

import com.ws.inpe.model.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:token.properties")
@Configuration
public class TokenConfiguration {

    @Value( "${token}" )
    private String token;

    /**
     * Cria um objeto Token a ser usado nos controllers
     *
     * @return Token
     */
    @Bean
    Token token() {
        Token tokenObject = new Token();
        tokenObject.setToken(this.token);
        return tokenObject;
    }
}
