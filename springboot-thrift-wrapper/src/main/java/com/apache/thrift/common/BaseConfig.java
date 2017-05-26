package com.apache.thrift.common;

import com.apache.thrift.annotation.TService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ACA on 2017-5-22.
 */
public abstract class BaseConfig {

    private List<Class> services;

    public BaseConfig() {

        this.services =  new ArrayList<>();
    }

    @Bean
    public ConfigProperties thriftProperties() {
        return new ConfigProperties();
    }

    public Class[] getServices() {

        return services.toArray(new Class[]{});
    }

    public void registerClass(Class clazz) {

        services.add(clazz);
    }

    public void registerClasses(Class[] classes) {

        for(Class clazz : classes) {

            registerClass(clazz);
        }
    }

    public void registerPackage(String basePackage) {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(TService.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(org.springframework.stereotype.Component.class));
        registerClasses(scanner.findCandidateComponents(basePackage)
                .stream()
                .map(beanDefinition -> ClassUtils
                        .resolveClassName(beanDefinition.getBeanClassName(), this.getClass().getClassLoader()))
                .collect(Collectors.toList()).toArray(new Class[]{}));
    }
}
