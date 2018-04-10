package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ContentdmDao;
import model.NormalizedPager;
import model.NormalizedRecord;
import model.NormalizedResult;
import model.contentdm.Record;
import model.contentdm.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class ContentdmRepository {

    Logger log = LogManager.getLogger(ContentdmRepository.class);


    public ContentdmRepository() { }

    public NormalizedResult execQuery(String terms, String offset) {

        ContentdmDao contentdmDao = new ContentdmDao();
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

    public boolean hasNext(NormalizedPager pager, String offset) {

        if (pager.getTotalRecs() > Integer.valueOf(offset) + 10) {
            return true;
        }
        return false;
    }

    public boolean hasPrev(String offset) {

        if (Integer.valueOf(offset) > 0) {
            return true;
        }
        return false;
    }

    public String getOffset(String direction, String offset, NormalizedPager pager) {

        String newOffset = "0";
        if (direction.contentEquals("next")) {
            Integer offsetValue = Integer.valueOf(offset);
            if (pager.getTotalRecs() > offsetValue) {
                Integer tmpOffset = offsetValue + 10;
                newOffset = Integer.toString(tmpOffset);
            }
        }
        if (direction.contentEquals("prev")) {
            Integer offsetValue = Integer.valueOf(offset);
            if (offsetValue > 0) {
                Integer tmpOffset = offsetValue - 10;
                newOffset = Integer.toString(tmpOffset);
            }
        }
        return newOffset;
    }

}
