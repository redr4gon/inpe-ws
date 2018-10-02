package com.ws.inpe.model;

public class Health {

    private String dataHora;
    private String Conexao_INPE;
    private String Conexao_MQTT;

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getConexao_INPE() {
        return Conexao_INPE;
    }

    public void setConexao_INPE(String conexao_INPE) {
        Conexao_INPE = conexao_INPE;
    }

    public String getConexao_MQTT() {
        return Conexao_MQTT;
    }

    public void setConexao_MQTT(String conexao_MQTT) {
        Conexao_MQTT = conexao_MQTT;
    }
}
