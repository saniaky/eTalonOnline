package com.etalon.utils;

import com.etalon.model.CodexBook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public final class CodexManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String HOME = System.getProperty("user.home");
    private static final String HTML_CACHE_PATH = Path.of(HOME, "eTalon", "html").toString();
    private static final String JSON_PATH = Path.of(HOME, "eTalon", "json").toString();
    public static final String HTML_EXT = ".html";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36";
    public static final String REFERRER = "http://www.google.com";

    static {
        if (new File(HTML_CACHE_PATH).mkdirs() && new File(JSON_PATH).mkdirs()) {
            log.info("Folders initialized.");
        } else {
            log.info("Folders existed.");
        }
    }

    private CodexManager() {
    }

    public static Document getCodexHTML(String codexId) throws IOException {
        Document doc;
        Path path = Path.of(HTML_CACHE_PATH, codexId + HTML_EXT);
        if (Files.exists(path)) {
            doc = Jsoup.parse(new FileInputStream(path.toString()), "UTF-8", "");
        } else {
            log.info("Codex '{}' wasn't found, downloading one.", codexId);
            doc = Jsoup.connect("http://etalonline.by/document/?regnum=" + codexId)
                    .maxBodySize(1024 * 1024 * 20) // 20 MB
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .get();
            saveHtml(codexId, doc);
        }
        return doc;
    }

    public static FileInputStream loadHtml(String fileName) throws IOException {
        Path path = Path.of(HTML_CACHE_PATH, fileName + HTML_EXT);
        return new FileInputStream(path.toString());
    }

    public static void saveHtml(String fileName, Document doc) throws IOException {
        var path = Path.of(HTML_CACHE_PATH, fileName + HTML_EXT);
        var writer = new FileWriter(path.toString());
        writer.write(doc.html());
        writer.flush();
        writer.close();
    }

    public static void downloadFile(URL url, String fileName) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

    }

    public static void saveCodexJSON(CodexBook codex) throws IOException {
        var path = Paths.get(JSON_PATH, codex.getId() + ".json");
        var writer = new FileWriter(path.toString());
        gson.toJson(codex, writer);
        writer.close();
    }

}
