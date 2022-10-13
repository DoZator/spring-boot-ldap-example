package com.ad.project.config;

import com.ad.project.utils.CustomUserDetailsContextMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true )
public class SecurityConfig {

    @Value("${ad.domain}")
    private String AD_DOMAIN;

    @Value("${ad.url}")
    private String AD_URL;

    @Configuration
    public class SecuredConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();

            http.authorizeRequests()
                    .anyRequest().hasAnyRole("USER")
                    .and().formLogin();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
        }

        @Bean
        public AuthenticationManager authenticationManager() {
            var providerManager = new ProviderManager(List.of((activeDirectoryLdapAuthenticationProvider())));
            providerManager.setEraseCredentialsAfterAuthentication(false);
            return providerManager;
        }

        @Bean
        public CustomUserDetailsContextMapper customUserDetailsContextMapper() {
            return new CustomUserDetailsContextMapper();
        }

        @Bean
        public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
            ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(AD_DOMAIN, AD_URL);
            provider.setConvertSubErrorCodesToExceptions(true);
            provider.setUseAuthenticationRequestCredentials(true);
            provider.setUserDetailsContextMapper(customUserDetailsContextMapper());

            return provider;
        }
    }
}
