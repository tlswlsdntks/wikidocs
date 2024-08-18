package kr.wikidocs.config;

import kr.wikidocs.config.handler.CustomArgumentResolver;
import kr.wikidocs.config.handler.CustomReturnValueHandler;
import kr.wikidocs.config.interceptor.AdminInterceptor;
import kr.wikidocs.config.mapper.CustomObjectMapper;
import kr.wikidocs.config.resolver.CustomPathResourceResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * WebMvcConfig
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    private String profiles;

    @Value("${spring.web.resources.static-locations}")
    private String staticLocations;

    @Autowired
    List<HttpMessageConverter<?>> converters;

    private final AdminInterceptor adminInterceptor;
    private final CustomArgumentResolver customArgumentResolver;
    private final CustomReturnValueHandler customReturnValueHandler;

    private final CustomObjectMapper customObjectMapper;
//	private final CustomBodyMethodProcessor customBodyMethodProcessor;

    /**
     * 레파지토리
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations(staticLocations + "/upload/")
                .resourceChain(true)
                .addResolver(new CustomPathResourceResolver());
    }
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/apps/**")
//				.addResourceLocations("classpath:/apps/")
//				.setCachePeriod(20);
//	}

//	/**
//	 * 필터
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public FilterRegistrationBean setFilterRegistration() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ParameterFilter());
//		filterRegistrationBean.addUrlPatterns("/demo/data/*");
//		return filterRegistrationBean;
//	}

    /**
     * 인터셉터 설정
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/resources/**",
                        "/favicon.ico",
                        "/health_check",
                        "/errors/**",
                        "/user/login",
                        "/user/logout",
                        //"/frame",
                        //"/main",
                        //"/demo/**",
                        "/api/**"
                );
    }

    /**
     * CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*");
//				.allowedOriginPatterns(
//					"http://loc*",
//					"http://192.168.*",
//					"http://localhost*",
//					"http://*.domain.com*",
//					"https://*.domain.com*"
//				);
    }

    /**
     * 파라미터 설정
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver);
        //resolvers.add(0, customBodyMethodProcessor);
        //WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    /**
     * 리턴값 설정
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(customReturnValueHandler);
        //WebMvcConfigurer.super.addReturnValueHandlers(handlers);
    }

    /**
     * 컨버터 설정
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(customObjectMapper));
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    /**
     * properties
     *
     * @return
     * @throws IOException
     */
    @Bean
    PropertiesFactoryBean properties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:properties/" + profiles + ".properties");
        propertiesFactoryBean.setLocations(resources);
        return propertiesFactoryBean;
    }

    /**
     * jsonView
     */
    @Bean
    MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

}