# RSSRerouter

The Synology Download Station application is not able to handle cookie authenticated restricted RSS feeds.

This application provides a transparent authentication layer for the Download Stations. The links in the RSS feed
are rerouted through the application to provide authentication.

Configuration
---

The following properties in the application.properties file control the rerouting:

RSS feed address:

    rss.url=https://ncore.cc/rss/rssdd.xml

Download link pattern:

    rss.download.url=https://ncore.cc/torrents.php?action=download&id=

Nick cookie property:

    rss.nick=XXX

Pass cookie property:

    rss.pass=XXX
    
You can override the configuration present in the jar file by placing an application.properties file in a 'config' folder
next to the jar file.

Heroku
---

The Procfile in the root makes it easy to deploy the application to the  Heroku Cloud. 

Follow the instructions in the documentation: [link] (https://devcenter.heroku.com/articles/deploying-java).

After deploying the application to Heroku you add http://\<heroku_address\>/rssrerouter/rss to the Download Station RSS
feed list.

