package com.teach.core.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

/**
 *
 * @author jien.lee
 */
public class YamlUtil {

    private static final Logger logger = LoggerFactory.getLogger(YamlUtil.class);

    public static Map<String, Object> yaml2Map(String yamlSource) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot load yaml.", e);
            return null;
        }
    }
}
