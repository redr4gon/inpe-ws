package com.ws.inpe.configuration;

import com.ws.inpe.model.INPEObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:inpe.properties")
public class INPEConfiguration {

    @Value( "${url.cidade}" )
    private String urlCidade;

    @Value( "${url.check}" )
    private String urlCheck;

    @Value( "${url.previsao}" )
    private String urlPrevisao;

    @Value( "${cidade}" )
    private String cidade;

    /**
     * Cria um objeto INPE a ser usado nas conexões com as apis
     *
     * @return INPEObject
     */
    @Bean
    public INPEObject inpeObject() {

        INPEObject inpeObject = new INPEObject();
        inpeObject.setUrlCheck(urlCheck);
        inpeObject.setUrlCidade(urlCidade);
        inpeObject.setUrlPrevisao(urlPrevisao);
        inpeObject.setCidade(cidade);

        return inpeObject;
    }

}
