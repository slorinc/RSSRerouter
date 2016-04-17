package com.sonnevend.rssrerouter.services;

import com.sonnevend.rssrerouter.dto.LinkedFileDTO;
import com.sonnevend.rssrerouter.dto.LinkedFileDTOBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * Created by s_lor_000 on 4/16/2016.
 */
@Service
public class ConnectorService {

    @Autowired
    private RestTemplate customRestTemplate;

    @Value("${rss.url}")
    private String rssUrl;

    @Value("${rss.download.url}")
    private String rssDownloadURL;

    @Value("${rss.nick}")
    private String nick;

    @Value("${rss.pass}")
    private String pass;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectorService.class);

    /**
     * Downloads the RSS feed
     *
     * @return RSS feed as String
     */
    public String downloadRSSFeed() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_XML));
        headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
        headers.setContentType(new MediaType("text", "xml", Charset.defaultCharset()));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String downloadRSSContent = null;

        try {
            Instant before = Instant.now();
            downloadRSSContent = customRestTemplate
                    .exchange(rssUrl, HttpMethod.GET, entity, String.class).getBody();
            Instant after = Instant.now();
            LOG.debug("Request to {} took {} ", new Object[]{rssUrl, Duration.between(before, after)});
        } catch (HttpStatusCodeException ex) {
            LOG.error("HTTP Error", ex);
        }

        return downloadRSSContent;
    }

    /**
     * Downloads the linked file corresponding to the file id
     * with the necessary authorization cookies
     *
     * @param id file id
     * @return DTO containing the file and the content disposition header
     */
    public LinkedFileDTO downloadTorrent(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType("application", "x-bittorrent")));
        headers.add("Cookie", "nick=" + nick + ";pass=" + pass);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> responseEntity;

        try {
            Instant before = Instant.now();
            responseEntity = customRestTemplate
                    .exchange(rssDownloadURL + id, HttpMethod.GET, entity, byte[].class);
            Instant after = Instant.now();
            LOG.debug("Request to {} took {} ", new Object[]{rssDownloadURL + id, Duration.between(before, after)});
        } catch (HttpStatusCodeException ex) {
            LOG.error("HTTP Error", ex);
            return null;
        }

        return new LinkedFileDTOBuilder().setFile(responseEntity.getBody()).setFileName(responseEntity.getHeaders().get("content-disposition").get(0)).createTorrentFile();
    }

}
