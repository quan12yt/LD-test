package com.example.launchdarkly;

import com.launchdarkly.sdk.server.Components;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;

@Slf4j
@Configuration
public class LaunchDarklyConfig {

    private LDClient ldClient;

    private static final String SDK_KEY = "sdk-9cf369e2-0439-4028-bda4-ef71007b5a5b";


    @Bean
    public LDClient ldClient() {

        LDConfig config = new LDConfig.Builder()
                .http(
                        Components.httpConfiguration()
                                .connectTimeout(Duration.ofSeconds(3))
                                .socketTimeout(Duration.ofSeconds(3))
                )
                .events(
                        Components.sendEvents()
                                .flushInterval(Duration.ofSeconds(10))
                )
                .build();

        this.ldClient = new LDClient(SDK_KEY, config);

        if (ldClient.isInitialized()) {
           log.info("SDK initialize successfully");
        } else {
            log.info("SDK failed to initialize");
        }

        return this.ldClient;
    }

    @PreDestroy
    public void destroy() throws IOException {
        this.ldClient.close();
    }

}
