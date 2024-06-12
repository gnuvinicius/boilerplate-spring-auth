package br.com.garage.notes.config.userInfo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class UserInfoConfig implements WebMvcConfigurer {

    private final UserInfoMehtodArgumentResolver customArgumentResolver;

    public UserInfoConfig(UserInfoMehtodArgumentResolver customArgumentResolver) {
        this.customArgumentResolver = customArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver);
    }
}
