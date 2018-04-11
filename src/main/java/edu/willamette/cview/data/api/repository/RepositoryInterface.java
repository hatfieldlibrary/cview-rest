package edu.willamette.cview.data.api.repository;

import model.NormalizedResult;

public interface RepositoryInterface {

    NormalizedResult execQuery(String terms, String offset);
}
