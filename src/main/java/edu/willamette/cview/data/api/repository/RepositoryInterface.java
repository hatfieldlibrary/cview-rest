package edu.willamette.cview.data.api.repository;

import edu.willamette.cview.data.api.model.NormalizedResult;

public interface RepositoryInterface {

    NormalizedResult execQuery(String terms, String offset);
}
