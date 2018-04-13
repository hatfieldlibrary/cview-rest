package edu.willamette.cview.data.api.dao;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import edu.willamette.cview.data.api.model.existdb.Item;

public class ExistHitsExclusion  implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getDeclaringClass() == Item.class && f.getName().equals("cost"));
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
