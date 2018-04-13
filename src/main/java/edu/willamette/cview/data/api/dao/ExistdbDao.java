package edu.willamette.cview.data.api.dao;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.willamette.cview.data.api.repository.Domains;
import edu.willamette.cview.data.api.model.existdb.CombinedResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class ExistdbDao {

    private final String existHost;
    private final String query;
    private final String rootPath;
    private final String setSize;

    @Value("${exist.default}")
    String collections;

    public ExistdbDao() {

        existHost = Domains.EXIST.getHost();
        query = Domains.EXIST.getQuery();
        rootPath = Domains.EXIST.getRootPath();
        setSize = Domains.EXIST.getSetSize();
    }

    public CombinedResult execQuery(String terms, String offset, String mode, String collections) {

        String queryUrl = formatQuery(terms, offset, mode, collections);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        URL url = null;
        try {
            url = new URL(queryUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CombinedResult existResult = null;
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            existResult = gson.fromJson(content.toString(), CombinedResult.class);
            in.close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return (existResult);

    }

    /**
     * Formats url for exist-db api queries.  Supported modes are 'all', 'any', and 'phrase'.
     * @param terms the terms to search
     * @param offset the offset value for the search (1-based offsets)
     * @param mode the query mode
     * @return the url for an exist-db query
     */
    private String formatQuery(String terms, String offset, String mode, String requestCollections) {

        // If specific collections are provided in the request,
        // use them and not the default collection value.
        if (!requestCollections.contentEquals("all")) {
            collections = requestCollections;
        }


        String url = "http://" +
                existHost + "/" +
                rootPath + "&collection=" +
                collections + "&q=" +
                query + "&records=" +
                setSize + "&start=" +
                offset;

        terms = terms.replace(" ", "+");
        return url.replace("{$query}", terms).replace("{$mode}", mode);
    }
}
