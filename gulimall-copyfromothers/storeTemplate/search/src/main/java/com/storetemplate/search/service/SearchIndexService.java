package com.storetemplate.search.service;

import com.storetemplate.search.vo.SearchParam;
import com.storetemplate.search.vo.SearchResult;

public interface SearchIndexService {
    SearchResult searchIndex(SearchParam searchParam);
}
