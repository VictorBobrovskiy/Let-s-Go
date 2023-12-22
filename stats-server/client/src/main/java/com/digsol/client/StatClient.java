package com.digsol.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import com.digsol.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HttpServletRequest req) {
        log.debug("----- App {} sent a POST request for EndpointHit", req.getContextPath());
        return post("/hit", new EndpointHitDto(
                req.getContextPath(),
                req.getRequestURI(),
                req.getRemoteAddr(),
                LocalDateTime.now())
        );
    }

    public ResponseEntity<Object> getViewStats(String start, String end, Collection<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", String.join(",", uris),
                "unique", unique
        );
        log.debug("----- Client sent a GET request for EndpointHit");
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}