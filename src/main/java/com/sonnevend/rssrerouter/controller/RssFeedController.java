package com.sonnevend.rssrerouter.controller;

import com.sonnevend.rssrerouter.dto.LinkedFileDTO;
import com.sonnevend.rssrerouter.services.FacadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by s_lor_000 on 4/16/2016.
 */
@Controller
@RequestMapping("/rss")
public class RssFeedController {

    private static final String APPLICATION_X_BITTORRENT = "application/x-bittorrent";

    private static final Logger LOG = LoggerFactory.getLogger(RssFeedController.class);

    @Autowired
    private FacadeService facadeService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> serveRSS(HttpServletRequest httpServletRequest) {
        String url = httpServletRequest.getRequestURL().toString();
        return new ResponseEntity<>(facadeService.serveRSS(url), HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET, produces = APPLICATION_X_BITTORRENT)
    public ResponseEntity<byte[]> serveTorrentFile(@PathVariable String id, HttpServletRequest httpServletRequest) {
        final LinkedFileDTO linkedFileDTO = facadeService.serverTorrentFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-disposition", linkedFileDTO.getContentDisposition());
        LOG.info("Served file: {} to {} ", new Object[]{linkedFileDTO.getContentDisposition().split("\"")[1], httpServletRequest.getRemoteAddr() });
        return new ResponseEntity<>(linkedFileDTO.getFile(), headers, HttpStatus.OK);
    }

}
