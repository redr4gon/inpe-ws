package com.ws.inpe.controller;

import com.ws.inpe.model.StatusModel;
import com.ws.inpe.model.Token;
import com.ws.inpe.service.IrrigacaoService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe responsável pelos controladores disponíveis de irrigação
 */
@RestController
@RequestMapping("/irrigacao")
public class IrrigacaoController {

    protected static final Logger logger = LoggerFactory.getLogger(IrrigacaoController.class);

    @Autowired
    IrrigacaoService irrigacaoService;

    @Autowired
    Token tokenObject;

    /**
     * Solicita a ativação ou desativação da irrigação
     *
     * @param status = ON/OFF
     * @param request
     * @return status
     */
    @PutMapping(value = "/controla")
    public ResponseEntity controla(@RequestBody StatusModel status, HttpServletRequest request) {

        logger.info("controllerIrrigacao: controla" + status.getStatus().name());

        if (!tokenObject.validateToken(request.getHeader("token"))) {
            logger.info("controllerIrrigacao: controla - UNAUTHORIZED");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        JSONObject jsonObject = irrigacaoService.controlaIrrigacao(status.getStatus());

        return jsonObject == null ? new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR) :
                new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

    /**
     * Acessa o INPE para previsão do tempo dos próximos 4 dias
     *
     * @return TEXTO com previsão do tempo
     */
    @GetMapping(value = "/verifica")
    public ResponseEntity verifica() {

        logger.info("controllerIrrigacao: verifica");

        return new ResponseEntity(irrigacaoService.buscaTempo(false), HttpStatus.OK);
    }

    /**
     * Acessa o INPE para previsão do tempo dos próximos 4 dias
     *
     * @return JSON com previsão do tempo
     */
    @GetMapping(value = "/verifica/json")
    public ResponseEntity verificaJson() {

        logger.info("controllerIrrigacao: verifica/json");

        return new ResponseEntity(irrigacaoService.buscaTempo(true), HttpStatus.OK);
    }

    /**
     * Exibe o status da comunicação com o INPE e MQTT
     *
     * @return JSON com os status
     */
    @GetMapping
    public ResponseEntity healthCheck() {

        logger.info("controllerIrrigacao: healthCheck");

        return new ResponseEntity(irrigacaoService.healthCheck(), HttpStatus.OK);
    }

    /**
     * Retorna o status atual da irrigação
     *
     * @return ON/OFF
     */
    @GetMapping(value = "/status")
    public ResponseEntity status() {

        logger.info("controllerIrrigacao: status");

        JSONObject jsonObject = irrigacaoService.findStatus();
        return jsonObject == null ? new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR) :
                new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

}
