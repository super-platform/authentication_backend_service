package com.ngtu.api;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableCaching
@Slf4j
@ComponentScan(basePackages = {"com.ngtu"})
public class AuthenticationBackendServiceApplication {

	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	private static final String SPRING_PROFILE_DEVELOPMENT = "dev";
	private static final String SPRING_PROFILE_UAT = "uat";
	private static final String SPRING_PROFILE_PRODUCTION = "prod";
	private static final String SPRING_PROFILE_CLOUD = "cloud";

	private final Environment env;

	public AuthenticationBackendServiceApplication(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void initApplication(){
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if(activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_PRODUCTION)){
			log.error("ou have misconfigured your application!");
		}

		if(activeProfiles.contains(SPRING_PROFILE_UAT) && activeProfiles.contains(SPRING_PROFILE_PRODUCTION)){
			log.error("ou have misconfigured your application!");
		}

		if(activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_CLOUD)){
			log.error("ou have misconfigured your application!");
		}

	}

	private static void addDefaultProfile(SpringApplication app){
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AuthenticationBackendServiceApplication.class);

		addDefaultProfile(app);

		Environment env = app.run(args).getEnvironment();
		log.info("====================ENV====================: {}", env.getProperty("spring.kafka.bootstrap-servers"));

		String protocol = "http";
		if(env.getProperty("server.ssl.key-store") != null){
			protocol = "https";
		}
		log.info(
				"\n---------------------------------------------------------\n\t"
					+ "Application '{}' is running! Access URLs: \n\t" + "Protocol: \t\t{}\n\t"
					+ "Profile(s) \t{}\n-----------------------------------------------------------",
				env.getProperty("spring.application.name"), protocol, env.getActiveProfiles());
	}

}
