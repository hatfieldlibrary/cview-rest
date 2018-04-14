package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.dao.ExistdbSingleDao;
import edu.willamette.cview.data.api.model.NormalizedPager;
import edu.willamette.cview.data.api.model.NormalizedRecord;
import edu.willamette.cview.data.api.model.NormalizedResult;
import edu.willamette.cview.data.api.model.existdb.Item;
import edu.willamette.cview.data.api.model.existdb.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExistDbSingleRepository implements RepositoryInterface {

    @Autowired
    ExistdbSingleDao existdbSingleDao;

    @Override
    @Cacheable("search")
    public NormalizedResult execQuery(String terms, String offset, String mode, String collections) {

        Result result = existdbSingleDao.execQuery(terms, offset, mode, collections);
        return normalize(result, offset);

    }

    private NormalizedResult normalize(Result results, String offset) {

        NormalizedResult normalizedResult = new NormalizedResult();
        List<NormalizedRecord> mappedResult = new ArrayList<>();
        ExistUtils existUtils = new ExistUtils();
        String total = results.getTotal();
        List<Item> items = results.getItem();
        for (Item item : items) {
            NormalizedRecord normalizedRecord = existUtils.getNormalizedRecord(item);
            mappedResult.add(normalizedRecord);
        }
        normalizedResult.setRecords(mappedResult);
        NormalizedPager normalizedPager = new NormalizedPager();
        normalizedPager.setPagingIncrement("10");
        normalizedPager.setStartIndex(offset);
        normalizedPager.setTotalRecs(Integer.valueOf(total));
        normalizedResult.setPager(normalizedPager);
        return normalizedResult;
    }

}
