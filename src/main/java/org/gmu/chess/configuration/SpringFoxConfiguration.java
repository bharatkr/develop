/*

 */

package org.gmu.chess.configuration;

import ca.watier.echechess.common.enums.CasePosition;

import org.gmu.chess.models.CasePositionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpSession;

@Configuration
@EnableSwagger2
public class SpringFoxConfiguration {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(HttpSession.class)
                .directModelSubstitute(CasePosition.class, CasePositionModel.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ca.watier"))
                .paths(PathSelectors.any())
                .build();
    }
}
