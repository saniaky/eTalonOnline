package com.etalon.html;

import com.etalon.model.CodexBook;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public final class CacheManager {

    private static final ObjectMapper mapper = new ObjectMapper();

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
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    private CacheManager() {
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

    public static void saveCodexJSON(CodexBook codex) throws IOException {
        var path = Paths.get(JSON_PATH, codex.getId() + ".json");
        var writer = new FileWriter(path.toString());
        mapper.writeValue(writer, codex);
        writer.close();
    }

}
