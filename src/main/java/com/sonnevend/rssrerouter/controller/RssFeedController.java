package com.sonnevend.rssrerouter.controller;

import com.sonnevend.rssrerouter.dto.LinkedFileDTO;
import com.sonnevend.rssrerouter.services.FacadeService;
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

    public static final String APPLICATION_X_BITTORRENT = "application/x-bittorrent";

    @Autowired
    private FacadeService facadeService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> serveRSS(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return new ResponseEntity<>(facadeService.serveRSS(url), HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET, produces = APPLICATION_X_BITTORRENT)
    public ResponseEntity<byte[]> serveTorrentFile(@PathVariable String id) {
        final LinkedFileDTO linkedFileDTO = facadeService.serverTorrentFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-disposition", linkedFileDTO.getContentDisposition());
        return new ResponseEntity<>(linkedFileDTO.getFile(), headers, HttpStatus.OK);
    }

}
