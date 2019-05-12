package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificationClient;
import com.leyou.item.pojo.*;

import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchRequest;
import com.leyou.pojo.SearchResult;
import com.leyou.repository.GoodsRepository;
import com.leyou.utils.JsonUtils;
import com.leyou.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cq
 * @create 2018-07-27 17:21
 * @copy alibaba
 */
@Service
public class IndexService {

    @Autowired
    //商品分类查询
    private CategoryClient categoryClient;

    @Autowired
    //查询spu下额所有sku
    private GoodsClient goodsClient;

    @Autowired
    //查询规格参数
    private SpecificationClient specClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired

    private BrandClient brandClient;


    //构建Goods对象
    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();


        //查询spu下的所有sku
        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        //创建集合用来保存所有sku 的价格
        Set<Long> prices = new TreeSet<>();
        //我们只需要sku中的:id tatil image price
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {

            prices.add(sku.getPrice());
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("images", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuList.add(map);
        }
        //查询规格参数的key searching 是可搜索 结果里只有key没有值
        List<SpecParam> params = specClient.queryParam(null, spu.getCid3(), null, true);
        //查询spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
        //取出详情钟的值 通用的   上面key 下面值
        String json = spuDetail.getGenericSpec();
        Map<Long, Object> genericParam = JsonUtils.parseMap(json, Long.class, Object.class);
        Map<Long, Object> specialParam = JsonUtils.parseMap(spuDetail.getSpecialSpec(), Long.class, Object.class);
        //查询商品分类  得到商品分类集合
        List<Category> categories = categoryClient.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String all = spu.getTitle() + "/" + StringUtils.join(categories, "/");
        //规格参数信息
        HashMap<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            //这是key
            Long id = param.getId();
            if (param.getGeneric()) {
                specs.put(param.getName(), chooseSegment(genericParam.get(id).toString(), param));
            } else {
                specs.put(param.getName(), specialParam.get(id));
            }

        }


        Goods goods = new Goods();
        //spuid
        goods.setId(spu.getId());
        //标题
        goods.setSubTitle(spu.getSubTitle());
        //商品id 1 2 3 三级
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        //品牌id
        goods.setBrandId(spu.getBrandId());
        //时间
        goods.setCreateTime(spu.getCreateTime());
        //标题饼名字搜索字段
        goods.setAll(all);
        //保存规格参数
        goods.setSpecs(specs);
        //准备价格
        goods.setPrice(new ArrayList<>(prices));
        //skus的json 格式
        goods.setSkus(JsonUtils.serialize(skuList));

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            // TODO 给默认搜索条件，或者什么都不返回
            return null;
        }

        // 原生查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、分页
        Integer page = request.getPage() - 1;
        Integer size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 2、条件过滤
        QueryBuilder basicQuery = bulidBasicQuery(request);
        queryBuilder.withQuery(basicQuery);
        // 结果筛选
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

        // 3、聚合
        String categoryAggName = "categoryAgg";
        //聚合名称 聚合商品
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //聚合品牌
        String brandAggName = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //4.搜素
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 5、解析结果
        long total = result.getTotalElements();
        long totalPage = (total + size - 1) / size;
        List<Goods> list = result.getContent();

        //商品分类聚合结果
        Aggregation agg = result.getAggregation(categoryAggName);
        List<Category> categories = handleCategoryAgg(result.getAggregation(categoryAggName));
        //品牌分类聚合结果
        List<Brand> brands = handleBrandAgg(result.getAggregation(brandAggName));
        //规格参数过滤结果

        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
            specs = buildSpecs(categories.get(0).getId(), basicQuery);

        }
        return new SearchResult(total, totalPage, list, categories, brands,specs);
    }

    private QueryBuilder bulidBasicQuery(SearchRequest request) {
        BoolQueryBuilder queryBuilder =QueryBuilders.boolQuery();
       //must对应普通查询  查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).minimumShouldMatch("75%"));
        //过滤条件
        // 过滤条件
        Map<String, String> filter = request.getFilter();
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if(!"cid3".equals(name) && !"brandId".equals(name)){
                name = "specs."+name+".keyword";
            }
            filterQueryBuilder.must(QueryBuilders.termQuery(name,value));
        }
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecs(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 1、查询当前分类下的可搜索的规格参数
        List<SpecParam> params = specClient.queryParam(null, cid, null, true);
        // 2、对这些规格进行聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 基本过滤条件
        queryBuilder.withQuery(basicQuery);
        //s设置查询数量为最小
        queryBuilder.withPageable(PageRequest.of(0, 1));

        for (SpecParam param : params) {
            String name = param.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));

        }
        // 执行聚合
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggs = result.getAggregations();
        // 3、解析聚合的结果
        for (SpecParam param : params) {
            // 封装聚合结果的一个Map
            Map<String,Object> map = new HashMap<>();
            String name = param.getName();
            StringTerms terms = aggs.get(name);
            List<String> options = terms.getBuckets().stream()
                    .map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            map.put("k", name);
            map.put("options", options);
            specs.add(map);
        }
        return  specs;
    }


    private List<Brand> handleBrandAgg(Aggregation agg) {
        LongTerms terms = (LongTerms) agg;
        // 获取品牌的id的集合
        List<Long> ids = terms.getBuckets().stream()
                .map(bucket -> bucket.getKeyAsNumber().longValue())
                .collect(Collectors.toList());

        return brandClient.queryByIds(ids);
    }


    private List<Category> handleCategoryAgg(Aggregation agg) {
        LongTerms terms = (LongTerms) agg;
        // 从桶中取出id形成集合
        List<Long> ids = terms.getBuckets().stream()
                .map(bucket -> bucket.getKeyAsNumber().longValue())
                .collect(Collectors.toList());
        // 根据id查询商品分类
        return categoryClient.queryByIds(ids);
    }

    public void deleteIndex(Long spuId) {

        goodsRepository.deleteById(spuId);


    }

    public void insertIndex(Long spuId) {

        //查询spuId查询商品
        Spu spu = goodsClient.querySpuById(spuId);
//buildGoods 代码抽取
        Goods goods = buildGoods(spu);

        goodsRepository.save(goods);


    }
}
