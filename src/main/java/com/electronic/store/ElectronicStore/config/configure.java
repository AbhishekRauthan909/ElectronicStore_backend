package com.electronic.store.ElectronicStore.config;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class configure
{
    @Bean
    public ModelMapper mapper()
    {
        return new ModelMapper();
    }
}
