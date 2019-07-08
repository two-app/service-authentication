package com.two.authentication;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Hashids getHashids() {
        return new Hashids("asodhaoHSDoIAHsdo12313hh1h321h3£££**£!", 6);
    }

}
