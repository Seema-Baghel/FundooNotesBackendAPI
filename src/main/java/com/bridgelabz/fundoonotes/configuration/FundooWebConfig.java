package com.bridgelabz.fundoonotes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.fundoonotes.utility.FundooInterceptor;


@Configuration
@EnableWebMvc
public class FundooWebConfig implements WebMvcConfigurer  {

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(customInterceptor()).
       addPathPatterns("/**").excludePathPatterns("/user/register/**","/user/verify/**",
        "/user/login/**","/user/forgotpassword/**");
   }

   @Bean
   public FundooInterceptor customInterceptor() {
       return new FundooInterceptor();
   }


}
