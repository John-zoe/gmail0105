package com.pis.gmall.search.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pis.gmall.bean.PmsSearchParam;
import com.pis.gmall.bean.PmsSearchSkuInfo;
import com.pis.gmall.bean.PmsSkuAttrValue;
import com.pis.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> getSearchList(PmsSearchParam pmsSearchParam) {
        String dsl = getSearchDsl(pmsSearchParam);
        System.err.println(dsl);
        //利用api进行复杂的查询
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        Search search = new Search.Builder(dsl).addIndex("gmall0105").addType("PmsSkuInfo").build();
        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            final Map<String, List<String>> highlight = hit.highlight;
            if(highlight != null){
                String skuName = highlight.get("skuName").get(0);
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);
        }
        return pmsSearchSkuInfos;
    }

    public String getSearchDsl(PmsSearchParam pmsSearchParam) {
        String[] pmsSkuAttrValues = pmsSearchParam.getValueIds();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();


        //jest的dsl工具类
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //highlight
        searchSourceBuilder.highlighter();
        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //filter
        if(StringUtils.isNotBlank(catalog3Id)){
            //term、terms
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            //TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("k","v","v");
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(pmsSkuAttrValues != null){
            for (String pmsSkuAttrValue : pmsSkuAttrValues) {
                TermQueryBuilder termQueryBuilder  = new TermQueryBuilder("pmsSkuAttrValues.valueId",pmsSkuAttrValue);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        if(StringUtils.isNotBlank(keyword)){
            //must
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
            //match
        }
        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //highlitght
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");

        //arregations
        TermsAggregationBuilder groupby_attr  = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
        searchSourceBuilder.aggregation(groupby_attr);

        return searchSourceBuilder.toString();
    }
}
