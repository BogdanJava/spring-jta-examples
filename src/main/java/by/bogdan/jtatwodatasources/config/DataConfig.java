package by.bogdan.jtatwodatasources.config;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * @author Bogdan Shishkin
 * project: jta-two-datasources
 * date/time: 17/06/2018 / 15:44
 * email: bogdanshishkin1998@gmail.com
 */

@Configuration
public class DataConfig {

    private final XADataSourceWrapper wrapper;

    public DataConfig(XADataSourceWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Bean
    public DataSource dataSourceFirst() throws Exception {
        return wrapper.wrapDataSource(dataSource("first"));
    }

    @Bean
    public DataSource dataSourceSecond() throws Exception {
        return wrapper.wrapDataSource(dataSource("second"));
    }

    @Bean
    public DataSourceInitializer initFirst(@Qualifier("dataSourceFirst") DataSource first) {
        return init(first, "first");
    }

    @Bean
    public DataSourceInitializer initSecond(@Qualifier("dataSourceSecond") DataSource second) {
        return init(second, "second");
    }

    private DataSourceInitializer init(DataSource dataSource, String name) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource(name + ".sql")
        ));
        return initializer;
    }

    private JdbcDataSource dataSource(String schema) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./   " + schema);
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
