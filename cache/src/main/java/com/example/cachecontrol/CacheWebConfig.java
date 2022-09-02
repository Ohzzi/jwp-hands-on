package com.example.cachecontrol;

import static com.example.version.CacheBustingWebConfig.PREFIX_STATIC_RESOURCES;

import com.example.version.ResourceVersion;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    private final ResourceVersion version;

    public CacheWebConfig(final ResourceVersion version) {
        this.version = version;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CacheControlInterceptor())
                .excludePathPatterns(PREFIX_STATIC_RESOURCES + "/" + version.getVersion() + "/**");
    }
}
