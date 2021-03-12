package com.jienlee.swagger.properties;

import static com.jienlee.swagger.properties.SwaggerProperties.PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author jien.lee
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class SwaggerProperties {
    public static final String PREFIX = "jienlee.swagger";

    private String title = "Jienlee-Swagger";

    private String description = "swagger 接口文档";

    private String version = "1.0.0";

}
