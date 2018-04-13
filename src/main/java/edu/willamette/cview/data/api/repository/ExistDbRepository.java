package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ExistdbDao;
import edu.willamette.cview.data.api.model.NormalizedPager;
import edu.willamette.cview.data.api.model.NormalizedRecord;
import edu.willamette.cview.data.api.model.NormalizedResult;

import edu.willamette.cview.data.api.model.existdb.CollectionResults;
import edu.willamette.cview.data.api.model.existdb.Item;
import edu.willamette.cview.data.api.model.existdb.CombinedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExistDbRepository implements RepositoryInterface {

    @Autowired
    ExistdbDao existdbDao;

    @Override
    public NormalizedResult execQuery(String terms, String offset, String mode, String collections) {

        CombinedResult result = existdbDao.execQuery(terms, offset, mode, collections);
        return normalize(result, offset);

    }

    private NormalizedResult normalize(CombinedResult results, String offset) {

        NormalizedResult normalizedResult = new NormalizedResult();
        List<NormalizedRecord> mappedResult = new ArrayList<>();
        Integer total = 0;
        try {
            for (CollectionResults coll : results.getCollectionResults()) {
                total = compareTotal(total, coll.getResult().getTotal());
                if (coll.getResult().getItem() != null) {
                    for (Item item : coll.getResult().getItem()) {
                        NormalizedRecord normalizedRecord = new NormalizedRecord();
                        normalizedRecord.setCollection(item.getCollection());
                        normalizedRecord.setDate(item.getDisplay_date());
                        //  normalizedRecord.setDescription(item.getDescription());
                        normalizedRecord.setId(item.getDate());
                        normalizedRecord.setHits(item.getHits());
                        normalizedRecord.setFiletype("xml");
                        normalizedRecord.setLocator(item.getDate());
                        normalizedRecord.setSource(getCollectionName(item.getCollection()));
                        normalizedRecord.setTitle(item.getTitle());
                        mappedResult.add(normalizedRecord);
                    }
                }
            }
            normalizedResult.setRecords(mappedResult);
            NormalizedPager normalizedPager = new NormalizedPager();
            normalizedPager.setPagingIncrement("10");
            normalizedPager.setStartIndex(offset);
            normalizedPager.setTotalRecs(total);
            normalizedResult.setPager(normalizedPager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return normalizedResult;
    }



    private Integer compareTotal(Integer total, String latestTotal) {

        if (total < Integer.valueOf(latestTotal)) {
            return Integer.valueOf(latestTotal);
        }
        return total;

    }
    private String getCollectionName(String coll) {

        switch(coll) {
            case "collegian": return "Willamette University Collegian";
            case "wallulah": return "Wallulah (Student Yearbook)";
            case "scene": return "Alumni Publications";
            case "bulletinscatalogs": return "Willamette University Catalogs & Bulletins";
            case "commencement": return "Commencement Programs";
            case "puritan": return "Willamette University Puritan";
            case "scrapbooks": return "Scrapbooks";
            case "handbooks": return "Student Handbooks - College of Liberal Arts";
            default: return "";
        }
    }

}
