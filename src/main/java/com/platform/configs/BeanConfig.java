package com.platform.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class BeanConfig {
    Environment environment;
    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    public BeanConfig(Environment environment, RestTemplateBuilder builder, ObjectMapper objectMapper) {
        this.environment = environment;
        this.restTemplate = builder.build();
        this.objectMapper = objectMapper;
    }

    @Bean("restTemplateAPI")
    public RestTemplate restTemplateAPI() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // Read config timeout
        int readTimeOut = environment.getProperty("rest.template.timeout.read", Integer.class, 30000);
        int connectionTimeOut = environment.getProperty("rest.template.timeout.connection", Integer.class, 30000);

        final TrustStrategy acceptingTrustStrategy = (certificates, authType) -> true;
        final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null,acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setTlsVersions(TLS.V_1_3)
                .build();
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(Timeout.ofMinutes(readTimeOut)).build())
                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                .setConnectionTimeToLive(TimeValue.ofMinutes(readTimeOut))
                .build();

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(StandardCookieSpec.STRICT).build())
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(client);
        requestFactory.setConnectTimeout(connectionTimeOut);
        requestFactory.setConnectionRequestTimeout(connectionTimeOut);
//        requestFactory.setBufferRequestBody(false); only fore turn big size

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }
}
