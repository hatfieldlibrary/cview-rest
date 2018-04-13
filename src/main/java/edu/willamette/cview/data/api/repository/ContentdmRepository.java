package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ContentdmDao;
import edu.willamette.cview.data.api.model.NormalizedPager;
import edu.willamette.cview.data.api.model.NormalizedRecord;
import edu.willamette.cview.data.api.model.NormalizedResult;
import edu.willamette.cview.data.api.model.contentdm.Record;
import edu.willamette.cview.data.api.model.contentdm.Result;
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
    public NormalizedResult execQuery(String terms, String offset, String mode, String collections) {

        Result cdmResult = contentdmDao.execQuery(terms, reduceOffset(offset), mode, collections);
        return normalize(cdmResult);
    }

    private NormalizedResult normalize(Result results) {

        NormalizedResult normalizedResult = new NormalizedResult();
        List<NormalizedRecord> mappedResult = new ArrayList<>();
        try {
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
            normalizedPager.setStartIndex(increaseOffset(results.getPager().getStart()));
            normalizedPager.setTotalRecs(results.getPager().getTotal());
            normalizedResult.setPager(normalizedPager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return normalizedResult;
    }

    /**
     * This function adjust the offset value down by 1 to
     * accommodate CONTENTdm's zero-based pagination.
     * @param offset
     * @return
     */
    private String reduceOffset(String offset) {

        Integer tmp = Integer.valueOf(offset);
        return Integer.toString(tmp - 1);
    }

    /**
     * This function increases the offset by one to
     * accommodate the the cview data API 1-based
     * pagination.
     * @param offset
     * @return
     */
    private String increaseOffset(String offset) {
        Integer tmp = Integer.valueOf(offset);
        return Integer.toString(tmp + 1);
    }

}
