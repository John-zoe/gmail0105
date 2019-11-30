package com.pis.gmall.search;

import com.pis.gmall.bean.PmsSearchSkuInfo;
import com.pis.gmall.bean.PmsSkuInfo;
import com.pis.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GmallSearchServiceApplicationTests {

	@Test
	void contextLoads() throws IOException {
		put();
	}

	@Reference
	SkuService skuService;
	@Autowired
	JestClient jestClient;

	public void serach() throws IOException {

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
		TermQueryBuilder termQueryBuilder = new TermQueryBuilder("k","v");
		TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("k","v","v");
		boolQueryBuilder.filter(termsQueryBuilder);
		//term、terms
		//must
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("k","v");
		boolQueryBuilder.must(matchQueryBuilder);
		//match
		//query
		searchSourceBuilder.query(boolQueryBuilder);
		List<PmsSearchSkuInfo> pmsSearchSkuInfos =new ArrayList<>();
		String query = "json语句";
		Search search = new Search.Builder(query).addIndex("库").addType("表名").build();
		SearchResult searchResult = jestClient.execute(search);
		List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
		for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
			PmsSearchSkuInfo source = hit.source;
			pmsSearchSkuInfos.add(source);
		}
	}

	public void  put() throws IOException {


		//将db数据转换为es数据结构
		List<PmsSkuInfo>  pmsSkuInfos = new ArrayList<>();
		pmsSkuInfos =  skuService.getAllSku();

		List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

		for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
			PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

			BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
			pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
			pmsSearchSkuInfos.add(pmsSearchSkuInfo);
		}
		//再将数据导入es中
		for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
			Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId() + "").build();
			jestClient.execute(put);
		}
	}

}
