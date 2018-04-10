package edu.willamette.cview.data.api.repository;

import com.google.gson.Gson;
import model.NormalizedPager;
import model.NormalizedRecord;
import model.NormalizedResult;
import model.contentdm.Record;
import model.contentdm.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ContentdmRepository  {

    Logger log = LogManager.getLogger(ContentdmRepository.class);

    private final String queryUrl;

    public ContentdmRepository(String terms, String offset) {

        String cdmHost = Domains.CONDM.getHost();
        String collection = Domains.CONDM.getCollection();
        String query = Domains.CONDM.getQuery();
        String sort = Domains.CONDM.getSort();
        String rootPath = Domains.CONDM.getRootPath();
        String returnFields = Domains.CONDM.getReturnFields();
        String setSize = Domains.CONDM.getSetSize();

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
        queryUrl = url.replace("{$query}", terms);

//        Flowable cdm = Flowable.fromCallable(() -> execQuery(queryUrl));
        // Eventually will do object mapping and normalizaton.
        // Not yet sure we gain by using separate thread.
//        List<String> result = cdm
//                .flatMap(json -> json)
//                .toList()
//                .blockingGet();

    }

    public NormalizedResult execQuery () {

        log.info(queryUrl);

        Gson gson = new Gson();

        URL url = null;
        try {
            url = new URL(this.queryUrl);
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
        return normalize(cdmResult);
    }

    private NormalizedResult normalize(Result results) {

        NormalizedResult normalizedResult = new NormalizedResult();

        List<NormalizedRecord> mappedResult = new ArrayList<>();
        for (Record record : results.getRecords()) {
            NormalizedRecord normalizedRecord = new NormalizedRecord();
            if (record.getCollection().contentEquals("/eads")
                    || record.getCollection().contentEquals("/oclcsample")) {
                continue;
            }
            normalizedRecord.setCollection(record.getCollection());
            normalizedRecord.setDate(record.getBdate());
            normalizedRecord.setDescription(record.getDescri());
            normalizedRecord.setId(record.getPointer());
            normalizedRecord.setFiletype(record.getFiletype());
            normalizedRecord.setLocator(record.getFind());
            normalizedRecord.setSource(record.getSource());
            normalizedRecord.setTitle(record.getTitle());
            mappedResult.add(normalizedRecord);
        }
        normalizedResult.setRecords(mappedResult);
        NormalizedPager normalizedPager = new NormalizedPager();
        normalizedPager.setPagingIncrement(results.getPager().getMaxrecs());
        normalizedPager.setStartIndex(results.getPager().getStart());
        normalizedPager.setTotalRecs(results.getPager().getTotal());
        normalizedResult.setPager(normalizedPager);
        return normalizedResult;
    }


}
