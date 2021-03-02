package com.swagger.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.swagger.starter.properties.SwaggerProperties.PREFIX;

/**
 * @author jien.lee
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class SwaggerProperties {
    public static final String PREFIX = "jienlee.swagger";

    private String basePackage = "com.base.controller";

    private String title = "Jienlee-Swagger";

    private String description = "swagger 接口文档";

    private String version = "1.0.0";

}
