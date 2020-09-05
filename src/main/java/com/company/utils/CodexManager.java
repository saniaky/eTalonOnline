package com.company.utils;

import com.company.model.Codex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public final class CodexManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String HOME = System.getProperty("user.home");
    private static final String HTML_CACHE_PATH = Path.of(HOME, "eTalon", "html").toString();
    private static final String JSON_PATH = Path.of(HOME, "eTalon", "json").toString();

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
        Path path = Path.of(HTML_CACHE_PATH, codexId + ".html");
        if (Files.exists(path)) {
            doc = Jsoup.parse(new FileInputStream(path.toString()), "UTF-8", "");
        } else {
            log.info("Codex '{}' wasn't found, downloading one.", codexId);
            doc = Jsoup.connect("http://etalonline.by/document/?regnum=" + codexId).get();
            saveCodexHTML(doc, codexId);
        }
        return doc;
    }

    public static void saveCodexHTML(Document doc, String codexId) throws IOException {
        var path = Path.of(HTML_CACHE_PATH, codexId + ".html");
        var writer = new FileWriter(path.toString());
        writer.write(doc.toString());
        writer.close();
    }

    public static void saveCodexJSON(Codex codex) throws IOException {
        var path = Paths.get(JSON_PATH, codex.getId() + ".json");
        var writer = new FileWriter(path.toString());
        gson.toJson(codex, writer);
        writer.close();
    }

}
