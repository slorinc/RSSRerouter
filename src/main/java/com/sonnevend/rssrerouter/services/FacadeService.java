package com.sonnevend.rssrerouter.services;

import com.sonnevend.rssrerouter.dto.LinkedFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by s_lor_000 on 4/16/2016.
 */
@Service
public class FacadeService {

    @Autowired
    private ConnectorService connectorService;

    @Value("${rss.download.url}")
    private String rssDownloadLink;

    /**
     * Transforms the RSS feed to have download links pointing to the
     * download endpoint of this application
     *
     * @param url Application URL
     * @return transformed RSS feed
     */
    public String serveRSS(String url) {
        return connectorService.downloadRSSFeed().replace(rssDownloadLink.replace("&", "&amp;"), url + "/download/");
    }

    /**
     * Retrieves the {@link LinkedFileDTO} from {@link ConnectorService}
     *
     * @param id file ID
     * @return DTO containing the torrent file and content disposition header
     */
    public LinkedFileDTO serverTorrentFile(String id) {
        return connectorService.downloadTorrent(id);
    }
}
