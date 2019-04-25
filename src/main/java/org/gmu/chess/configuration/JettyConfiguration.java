/*

 */

package org.gmu.chess.configuration;

import org.gmu.chess.components.HttpToHttpsJettyConfiguration;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfiguration {

    @Bean
    public ConfigurableServletWebServerFactory servletWebServerFactory(
            JettyServerCustomizer securityServerCustomizer,
            HttpToHttpsJettyConfiguration httpToHttpsJettyConfiguration) {

        JettyServletWebServerFactory jettyServletWebServerFactory = new JettyServletWebServerFactory();
        jettyServletWebServerFactory.addServerCustomizers(securityServerCustomizer);
        jettyServletWebServerFactory.addConfigurations(httpToHttpsJettyConfiguration);

        return jettyServletWebServerFactory;
    }
}
