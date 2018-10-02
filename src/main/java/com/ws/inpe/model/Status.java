package com.ws.inpe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("on")
    ON,
    @JsonProperty("off")
    OFF;

}
