package com.ws.inpe.model;

public class INPEObject {

    private String urlCidade;
    private String urlCheck;
    private String urlPrevisao;

    private String cidade;
    private Integer codCidade;

    public String getUrlCidade() {
        return urlCidade;
    }

    public void setUrlCidade(String urlCidade) {
        this.urlCidade = urlCidade;
    }

    public String getUrlCheck() {
        return urlCheck;
    }

    public void setUrlCheck(String urlCheck) {
        this.urlCheck = urlCheck;
    }

    public String getUrlPrevisao() {
        return urlPrevisao;
    }

    public void setUrlPrevisao(String urlPrevisao) {
        this.urlPrevisao = urlPrevisao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Integer getCodCidade() {
        return codCidade;
    }

    public void setCodCidade(Integer codCidade) {
        this.codCidade = codCidade;
    }
}
