package edu.willamette.cview.data.api.dao;

import com.google.gson.Gson;
import edu.willamette.cview.data.api.repository.Domains;
import model.contentdm.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ContentdmDao {

    Logger log = LogManager.getLogger(ContentdmDao.class);

    String cdmHost;
    String collection;
    String query;
    String sort;
    String rootPath;
    String returnFields;
    String setSize;

    public ContentdmDao() {

        cdmHost = Domains.CONDM.getHost();
        collection = Domains.CONDM.getCollection();
        query = Domains.CONDM.getQuery();
        sort = Domains.CONDM.getSort();
        rootPath = Domains.CONDM.getRootPath();
        returnFields = Domains.CONDM.getReturnFields();
        setSize = Domains.CONDM.getSetSize();

//        Flowable cdm = Flowable.fromCallable(() -> execQuery(queryUrl));
        // Eventually will do object mapping and normalizaton.
        // Not yet sure we gain by using separate thread.
//        List<String> result = cdm
//                .flatMap(json -> json)
//                .toList()
//                .blockingGet();

    }

    public Result execQuery (String terms, String offset) {

        String queryUrl = formatQuery(terms, offset);
        Gson gson = new Gson();
        URL url = null;
        try {
            url = new URL(queryUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Result cdmResult = null;
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            cdmResult = gson.fromJson(content.toString(), Result.class);
            in.close();
            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return (cdmResult);
    }

    private String formatQuery(String terms, String offset) {

        String url = "http://" +
                cdmHost + "/" +
                rootPath + "/" +
                collection + "/" +
                query + "/" +
                returnFields + "/" +
                sort + "/" +
                setSize + "/" +
                offset +
                "/1/0/0/0/0/0/json";

        terms = terms.replace(" ", "+");
        return url.replace("{$query}", terms);
    }

}
