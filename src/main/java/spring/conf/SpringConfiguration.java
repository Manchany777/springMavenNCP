package spring.conf;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@PropertySource("classpath:spring/db.properties") // (복수형이면 @PropertySources로 사용)
//@EnableTransactionManagement // Transaction 설정 - 여기다 쓰거나 xml에다 쓰거나 둘 중 하나만 해야함
@MapperScan("user.dao") // 중간자인 mybatis를 안거치고 바로 db와  통신
public class SpringConfiguration {
	// 3번 방식
	@Value("${jdbc.driver}") // 변수에 Value(초기값) 입력 - 주의) import시 spring로 해야함 lombok 아님
	private String driver;
	@Value("${jdbc.url}")
	private String url;
	private @Value("${jdbc.username}") String username;
	private @Value("${jdbc.password}") String password;
	
	// dataSource 빈 설정
	@Bean    // 리턴타입        메소드명
	public BasicDataSource dataSource() {  // 메소드()명은 리턴클래스의 아이디명으로 해줘야함(일종의 약속)
		// 3번방식 (2번 방식인 네임스페이스 방식은 여기서 쓸 필요x)
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driver);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		
		return basicDataSource;
		//     빈으로 생성할 리턴값 (메소드에서 생성(new)하는 얘를 빈으로 생성함)
	}
	// xml의 <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource"> 와 동일
	
	// sqlSessionFactory 빈 설정
	@Bean
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext, DataSource dataSource) throws Exception {
		 SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource);  			// 메서드 호출(dataSource())하는게 아니라 DataSource를 직접 부름(dataSource)
	        sessionFactory.setTypeAliasesPackage("user.bean"); 	// allias 설정
	        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/*Mapper.xml"));
       
        return sessionFactory.getObject();
    }
	
	// SqlSession 빈 설정
    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception {
    	SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    	return sqlSessionTemplate;
    }
    
    // Transaction 빈 설정
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    	DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    	return dataSourceTransactionManager;
    }
}