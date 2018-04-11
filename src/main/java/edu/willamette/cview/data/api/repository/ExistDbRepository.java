package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ExistdbDao;
import model.NormalizedPager;
import model.NormalizedRecord;
import model.NormalizedResult;

import model.existdb.Record;
import model.existdb.CombinedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExistDbRepository implements RepositoryInterface {

    @Autowired
    ExistdbDao existdbDao;

    @Override
    public NormalizedResult execQuery(String terms, String offset) {

        CombinedResult result = existdbDao.execQuery(terms, offset);
        return normalize(result);

    }

    private NormalizedResult normalize(CombinedResult results) {

        NormalizedResult normalizedResult = new NormalizedResult();
        List<NormalizedRecord> mappedResult = new ArrayList<>();
        for (Record record : results.getRecords()) {
            NormalizedRecord normalizedRecord = new NormalizedRecord();
            normalizedRecord.setCollection(record.getCollection());
            normalizedRecord.setDate(record.getDate());
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
