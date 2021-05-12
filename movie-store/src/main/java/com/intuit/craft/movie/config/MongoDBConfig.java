package com.intuit.craft.movie.config;

import com.intuit.craft.movie.helper.ZonedDateTimeReadConverter;
import com.intuit.craft.movie.helper.ZonedDateTimeWriteConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private final String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    public MongoCustomConversions customConversions() {
        final List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }
}
