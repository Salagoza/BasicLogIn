package io.muic.ssc.webapp.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder

public class ConfigProperties {

    private String driverClassName;
    private String connectionUrl;
    private String username;
    private String password;

}
