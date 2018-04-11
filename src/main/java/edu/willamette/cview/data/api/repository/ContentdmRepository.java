package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ContentdmDao;
import model.NormalizedPager;
import model.NormalizedRecord;
import model.NormalizedResult;
import model.contentdm.Record;
import model.contentdm.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContentdmRepository implements RepositoryInterface {

    Logger log = LogManager.getLogger(ContentdmRepository.class);

    @Autowired
    ContentdmDao contentdmDao;

    @Override
    @Cacheable("cdm")
    public NormalizedResult execQuery(String terms, String offset) {

        Result cdmResult = contentdmDao.execQuery(terms, offset);
        return normalize(cdmResult);
    }

    private NormalizedResult normalize(Result results) {

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
