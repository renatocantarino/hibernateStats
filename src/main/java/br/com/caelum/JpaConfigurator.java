package br.com.caelum;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class JpaConfigurator {

	@Bean
	public DataSource getDataSource() throws PropertyVetoException {
		// DriverManagerDataSource dataSource = new DriverManagerDataSource();

		// dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		// dataSource.setUrl("jdbc:mysql://localhost/projeto_jpa?useTimezone=true&serverTimezone=UTC");
		// dataSource.setUsername("root");
		// dataSource.setPassword("root");

		ComboPooledDataSource _comboPooled = new ComboPooledDataSource();
		_comboPooled.setDriverClass("com.mysql.cj.jdbc.Driver");

		_comboPooled.setUser("root");
		_comboPooled.setPassword("root");
		_comboPooled.setJdbcUrl("jdbc:mysql://localhost/projeto_jpa?useTimezone=true&serverTimezone=UTC");

		_comboPooled.setMinPoolSize(5);
		_comboPooled.setNumHelperThreads(5);

		return _comboPooled;
	}

	@Bean
	public Statistics statistics(EntityManagerFactory entityManagerFactory) {

		SessionFactory _sf = entityManagerFactory.unwrap(SessionFactory.class);
		return _sf.getStatistics();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setPackagesToScan("br.com.caelum");
		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties props = new Properties();

		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		props.setProperty("hibernate.cache.use_second_level_cache", "true");
		props.setProperty("hibernate.cache.use_query_cache", "true");
		props.setProperty("hibernate.cache.region.factory_class",
				"org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		props.setProperty("hibernate.generate_statistics", "true");

		entityManagerFactory.setJpaProperties(props);
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

}
