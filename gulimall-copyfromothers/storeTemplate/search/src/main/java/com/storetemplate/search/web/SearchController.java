package com.storetemplate.search.web;
import com.storetemplate.search.service.SearchIndexService;
import com.storetemplate.search.vo.SearchParam;
import com.storetemplate.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/6 10:25
 * @Version 1.0
 **/
@Controller
public class SearchController {
    @Autowired
    private SearchIndexService searchService;

    @GetMapping({"/", "/index.html"})
    public String index(SearchParam searchParam, Model model, HttpServletRequest request) {
        // 获取路径原生的查询属性
        searchParam.set_queryString(request.getQueryString());
         SearchResult searchResult= searchService.searchIndex(searchParam);
         model.addAttribute("result",searchResult);
        return "index";
    }


}
