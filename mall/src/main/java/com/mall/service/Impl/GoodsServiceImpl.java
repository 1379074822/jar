package com.mall.service.Impl;

import com.mall.mapper.GoodsCommentMapper;
import com.mall.mapper.GoodsLogMapper;
import com.mall.mapper.GoodsMapper;
import com.mall.mapper.GoodsStockMapper;
import com.mall.pojo.*;
import com.mall.service.GoodsService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@CacheConfig(cacheNames = "goods",cacheManager = "goodsCacheManager")
@Service
public class GoodsServiceImpl implements GoodsService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    /**
     * 商品mapper
     */
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 商品库存mapper
     */
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    /**
     * 商品评论mapper
     */
    @Autowired
    private GoodsCommentMapper goodsCommentMapper;
    /**
     * 商品日志mapper
     */
    @Autowired
    private GoodsLogMapper goodsLogMapper;
    /**
     * ES操作接口JestClient
     */
    @Autowired
    private JestClient jestClient;

    @Override
    public List<Goods> getGoodsListByParam(String queryGoodsName, String queryGoodsCore, Integer queryState, Integer queryOneCatagoryId, Integer queryTwoCatagoryId, Integer currentPageNo, Integer pageSize) throws Exception {
        return goodsMapper.getGoodsListByParam(queryGoodsName, queryGoodsCore, queryState, queryOneCatagoryId,
                queryTwoCatagoryId, (currentPageNo - 1) * pageSize, pageSize);
    }

    @Override
    public int getGoodsCountByParam(String queryGoodsName, String queryGoodsCore, Integer queryState, Integer queryOneCatagoryId, Integer queryTwoCatagoryId) throws Exception {
        return goodsMapper.getGoodsCountByParam(queryGoodsName,queryGoodsCore,queryState,queryOneCatagoryId,queryTwoCatagoryId);
    }

    @Override
    public Goods checkGoodsName(String goodsName) throws Exception {
        return goodsMapper.checkGoods(goodsName);
    }

    @Override
    public boolean insertGoods(Goods goods) throws Exception {
        boolean flag = false;
        if(goodsMapper.insert(goods)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean insertGoodsStock(GoodsStock goodsStock) throws Exception {
        boolean flag = false;
        if(goodsStockMapper.insert(goodsStock)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    @Cacheable(value = "goods",key = "#id")
    public Goods getGoodsById(Integer id) throws Exception {
        System.out.println("111111");
        return goodsMapper.getGoodsById(id);
    }

    //@CacheEvict(value = "goods",key = "#result.id")
    @Override
    public boolean deleteGoodsById(Integer id) throws Exception {
        boolean flag = false;
        Goods goods = goodsMapper.getGoodsById(id);
        //1.删除上传的LOGO图片
        if(goods.getLogoLocPath() != "" && goods.getLogoLocPath() != null){
            File file = new File(goods.getLogoLocPath());
            if(file.exists()){
                if(!file.delete())
                    throw new Exception();
            }
        }
        //2.删除商品基本信息
        if(goodsMapper.deleteGoodsById(id)>0){
            //3.删除商品关联的库存记录
            if(goodsStockMapper.deleteStock(id)>0){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    @CachePut(value = "goods",key = "#result.id")
    public Goods update(Goods goods) throws Exception {
        goodsMapper.update(goods);
        Goods g = goodsMapper.getGoodsById(goods.getId());
        return g;
    }

    @Override
    public boolean updateStockByAdmin(Integer stock, Integer goodsId) throws Exception {
        boolean flag = false;
        if(goodsStockMapper.updateStockByAdmin(stock,goodsId)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteGoodsLogo(Integer id) throws Exception {
        boolean flag = false;
        if(goodsMapper.deleteGoodsLogo(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Goods> getAllGoodsByLimit() throws Exception {
       return goodsMapper.getAllGoodsByLimit();
    }

    @Override
    public int getGoodsSale(Integer id) throws Exception {
        return goodsStockMapper.getGoodsSale(id);
    }
    @Override
    public int getGoodsStock(Integer id) throws Exception {
        return goodsStockMapper.getGoodsStock(id);
    }

    @Override
    public Goods checkByCategory(Integer id) throws Exception {
        return goodsMapper.checkByCategory(id);
    }


    @Override
    public List<Goods> getGoodsList(){
        return goodsMapper.getGoodsList();
    }

    @Override
    public List<Goods> getHotGoods() {
        return goodsMapper.getHotGoods();
    }

    @Override
    public List<Goods> getNewGoods() {
        return goodsMapper.getNewGoods();
    }

    /**
     * 批量保存内容到es
     * @param goodsList 商品列表
     */
    @Override
    public void saveGoods(List<Goods> goodsList) {
        Bulk.Builder builder = new Bulk.Builder();
        for(Goods goods:goodsList){
            //创建索引并加入构建器
            Index index = new Index.Builder(goods).index(Goods.INDEX).type(Goods.TYPE).build();
            builder.addAction(index);
        }
        try{
            jestClient.execute(builder.build());
            logger.info("插入ES成功!");
        }catch (Exception e){
            logger.error("插入es失败!",e);
        }
    }

    /**
     * 通过关键字搜索商品
     * @param keyword 关键字
     * @return
     */
    @Override
    public List<Goods> searchGoods(String keyword) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(keyword != null){
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("goodsName",keyword));
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("goodsCore",keyword));
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("goodsDescription",keyword));
        }
        //使关键字高亮的接口
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("goodsName");
        highlightBuilder.field("goodsDescription");
        highlightBuilder.preTags("<font color='blue'>").postTags("</font>");//高亮标签

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder).size(20);  //需要检索的条数，后续可分页
        Search search = new Search.Builder(searchSourceBuilder.toString()).
                        addIndex(Goods.INDEX).addType(Goods.TYPE).build();
        try{
            SearchResult result = jestClient.execute(search);
            System.out.println("本次查询到"+result.getTotal()+"次商品!");
            List<SearchResult.Hit<Goods, Void>> hits = result.getHits(Goods.class);
            System.out.println(hits.size());
            List<Goods> goodsList = new ArrayList<>();

            for(SearchResult.Hit<Goods, Void> hit:hits){
                Goods goods = hit.source;
                //获取高亮后的内容
                Map<String, List<String>> highlight = hit.highlight;
                if(highlight != null){
                    List<String> goodsName = highlight.get("goodsName");
                    if(goodsName != null){
                        goods.setGoodsName(goodsName.get(0));
                    }
                    List<String> goodsDescription = highlight.get("goodsDescription");
                    if(goodsDescription != null){
                        goods.setGoodsDescription(goodsDescription.get(0));
                    }
                }
                Goods g = new Goods();
                g.setId(goods.getId());
                g.setLogoPicPath(goods.getLogoPicPath());
                g.setPrice(goods.getPrice());
                g.setGoodsDescription(goods.getGoodsDescription());
                g.setGoodsName(goods.getGoodsName());
                g.setGoodsCore(goods.getGoodsCore());
                goodsList.add(g);
            }
            return goodsList;
        } catch (IOException e) {
            logger.error("查询异常报错，",e);
        }
        return null;
    }

    @Override
    public boolean insertComment(List<GoodsComment> goodsCommentList, Integer userId) throws Exception {
        boolean flag = false;
        if( goodsCommentMapper.insert(goodsCommentList,userId)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<GoodsComment> getListById(Integer id, Integer currentPageNo, Integer pageSize) throws Exception {
        return goodsCommentMapper.getById(id,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getCountById(Integer id) throws Exception {
        return goodsCommentMapper.getCountById(id);
    }

    @Override
    public boolean insertLog(GoodsLog goodsLog) {
        boolean flag = false;
        if( goodsLogMapper.insert(goodsLog)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteLog(Integer id) {
        boolean flag = false;
        if( goodsLogMapper.delete(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getLogCount(String goodsName, String createTime) {
        return goodsLogMapper.getLogCounts(goodsName,createTime);
    }

    @Override
    public List<GoodsLog> getLogList(String goodsName, String createTime, Integer currentPage, Integer pageSize) {
        return goodsLogMapper.getLogList(goodsName,createTime,(currentPage-1)*pageSize,pageSize);
    }

}
