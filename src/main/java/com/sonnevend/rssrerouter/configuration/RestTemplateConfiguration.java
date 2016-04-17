package com.sonnevend.rssrerouter.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by s_lor_000 on 11/24/2015.
 */
@Configuration
public class RestTemplateConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RestTemplateConfiguration.class);

    @Bean
    public RestTemplate CustomRestTemplate(
            @Value("${app.httpclient.connect-timeout:10000}") int connectionTimeout,
            @Value("${app.httpclient.requests.timeout:10000}") int readTimeout,
            @Value("${app.httpclient.pool.max-size:50}") int poolMaxSize,
            @Value("${app.httpclient.socket-timeout:0}") int socketTimeout,
            @Value("${app.httpclient.revalidate-timeout:-1}") int reValidateTimeOut
    ) {

        LOG.debug("-----------------------------------------");
        LOG.debug("connectionTimeout: {}", connectionTimeout);
        LOG.debug("readTimeout: {}", readTimeout);
        LOG.debug("poolMaxSize: {}", poolMaxSize);
        LOG.debug("socketTimeout: {}", socketTimeout);
        LOG.debug("reValidateTimeOut: {}", reValidateTimeOut);
        LOG.debug("-----------------------------------------");

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(readTimeout)
                .setConnectTimeout(connectionTimeout)
                //Turning off the socket timeouts
                .setSocketTimeout(socketTimeout)
                .build();

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                //The packets supposed to be small, so it's safe to turn tcp no delay on
                .setTcpNoDelay(true)
                .build();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // Negative value turns of validation
        poolingHttpClientConnectionManager.setValidateAfterInactivity(reValidateTimeOut);
        poolingHttpClientConnectionManager.setMaxTotal(poolMaxSize);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(poolMaxSize);

        CloseableHttpClient httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultSocketConfig(socketConfig).build();

        requestFactory.setHttpClient(httpClientBuilder);

        final RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

}
