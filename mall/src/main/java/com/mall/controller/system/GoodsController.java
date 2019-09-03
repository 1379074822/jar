package com.mall.controller.system;
import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.*;
import com.mall.service.DataDictionaryService;
import com.mall.service.GoodsCategoryService;
import com.mall.service.GoodsService;
import com.mall.tools.Constants;
import com.mall.tools.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Configuration;

import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 后台系统商品管理控制器
 */
@Controller
@RequestMapping("/system/goods")
public class GoodsController {
    private Logger logger = LoggerFactory.getLogger(com.mall.controller.system.GoodsController.class);
    /**
     * 数据字典接口
     */
    @Autowired
    private DataDictionaryService dataDictionaryService;
    /**
     * 商品分类接口
     */
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    /**
     * 商品接口
     */
    @Autowired
    private GoodsService goodsService;

    /**
     * 获取数据字典
     * @param typeCode
     * @return
     */
    public List<DataDictionary> getDataDictionaryList(String typeCode) {
        List<DataDictionary> dataDictionaryList = null;
        try {
            dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
        } catch (Exception e) {
            logger.error("获得数据字典,",e);
        }
        return dataDictionaryList;
    }

    /**
     * 获取数据字典
     * @param typeCode
     * @return
     */
    @RequestMapping({"/dataDictionaryList.json"})
    @ResponseBody
    public List<DataDictionary> getDataDicList(@RequestParam String typeCode) {
        logger.info("数据字典编码是:" + typeCode);
        return this.getDataDictionaryList(typeCode);
    }

    /**
     * 获取分类列表
     * @param pid 分类id
     * @return
     */
    public List<GoodsCategory> getGoodsCategoryList(String pid) {
        List<GoodsCategory> goodsCategoryList = null;
        try {
            goodsCategoryList = goodsCategoryService.getCategoryListByParentId(pid == null ? null : Integer.parseInt(pid));
        } catch (Exception e) {
            logger.error("获得分类列表,",e);
        }
        return goodsCategoryList;
    }

    /**
     * 获取一、二分类列表
     * @param pid
     * @return
     */
    @RequestMapping("/categoryList.json")
    @ResponseBody
    public List<GoodsCategory> getCategoryList(@RequestParam String pid) {
        logger.info("获得分类列表的pid:" + pid);
        if (pid.equals("")) {
            pid = null;
        }
        return this.getGoodsCategoryList(pid);
    }

    /**
     * 管理员条件查询商品信息
     * @param model model
     * @param queryGoodsName 商品名称
     * @param queryGoodsCore 商品货号
     * @param _queryState 商品状态
     * @param _oneCategoryId 商品一级分类id
     * @param _twoCategoryId 商品二级分类id
     * @param pageIndex 当前页码
     * @return
     */
    @RequestMapping({"/list"})
    public String getGoodsListByParam(Model model, @RequestParam(value = "queryGoodsName", required = false) String queryGoodsName,
                                      @RequestParam(value = "queryGoodsCore", required = false) String queryGoodsCore,
                                      @RequestParam(value = "queryState", required = false) String _queryState,
                                      @RequestParam(value = "oneCategoryId", required = false) String _oneCategoryId,
                                      @RequestParam(value = "twoCategoryId", required = false) String _twoCategoryId,
                                      @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        logger.info("商品信息1111:" + queryGoodsName + "  " + queryGoodsCore + "  " + _queryState + "  " + pageIndex);
        List<Goods> goodsList = null;   //商品列表
        List<DataDictionary> stateList = null; //商品状态列表
        List<GoodsCategory> oneCategoryList = null; //列出一级分类列表,二级分类列表通过异步ajax获取
        List<GoodsCategory> twoCategoryList = null;
        //页面大小
        int pageSize = Constants.pageSizeGoods;
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Integer queryState = null;
        if (_queryState != null && !_queryState.equals("")) {
            queryState = Integer.valueOf(_queryState);
        }
        Integer oneCategoryId = null;
        if (_oneCategoryId != null && !_oneCategoryId.equals("")) {
            oneCategoryId = Integer.valueOf(_oneCategoryId);
        }
        Integer twoCategoryId = null;
        if (_twoCategoryId != null && !_twoCategoryId.equals("")) {
            twoCategoryId = Integer.valueOf(_twoCategoryId);
        }
        //总数量
        int totalCount = 0;
        try {
            totalCount = goodsService.getGoodsCountByParam(queryGoodsName, queryGoodsCore, queryState, oneCategoryId, twoCategoryId);
            logger.info("总数量:" + totalCount);
        } catch (Exception e) {
            logger.error("获取商品列表数量失败!",e);
        }
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        logger.info("当前页码：" + currentPageNo + "每页条数:" + pageSize);
        try {
            goodsList = goodsService.getGoodsListByParam(queryGoodsName, queryGoodsCore, queryState, oneCategoryId, twoCategoryId, currentPageNo, pageSize);
            stateList = getDataDicList("GOODS_STATE");
            oneCategoryList = goodsCategoryService.getCategoryListByParentId(null);
        } catch (Exception e) {
            logger.error("获取商品列表失败!",e);
        }
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("oneCategoryList", oneCategoryList);
        model.addAttribute("pages", pages);
        model.addAttribute("queryGoodsName", queryGoodsName);
        model.addAttribute("queryGoodsCore", queryGoodsCore);
        model.addAttribute("queryState", queryState);
        model.addAttribute("oneCategoryId", oneCategoryId);
        model.addAttribute("twoCategoryId", twoCategoryId);
        if (twoCategoryId != null && !twoCategoryId.equals("")) {
            twoCategoryList = this.getGoodsCategoryList(oneCategoryId.toString());
            model.addAttribute("twoCategoryList", twoCategoryList);
        }
        return "system/goods/list";
    }

    /**
     * 进入商品添加页面
     * @return
     */
    @RequestMapping("/goodsAdd")
    public String goodsAdd() {
        return "system/goods/add";
    }

    /**
     * 检查商品名称是否存在
     * @param goodsName 商品名称
     * @return
     */
    @RequestMapping("/checkGoodsName")
    @ResponseBody
    public String checkGoodsName(@RequestParam String goodsName)throws Exception{
        HashMap<String, String> resultMap = new HashMap<>();
        if (StringUtils.isBlank(goodsName)) {
            resultMap.put("goodsName", Constants.RESULT_ERROR);
            return JSONArray.toJSONString(resultMap);
        }
        Goods goods = goodsService.checkGoodsName(goodsName);
        if (goods != null) {
            resultMap.put("goodsName", Constants.RESULT_FALSE);
        } else {
            resultMap.put("goodsName", Constants.RESULT_TRUE);
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 保存商品信息,同时更新库存信息
     * @param goods  商品实体类
     * @param request request
     * @param attach 文件
     * @return
     */
    @RequestMapping(value = "/goodsAddSave", method = RequestMethod.POST)
    public String goodsAddSave(Goods goods, HttpServletRequest request,
                               @RequestParam(value = "stock", required = false) String stock,
                               @RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) throws Exception{
        String logoPicPath = null;
        String logoLocPath = null;
        if (!attach.isEmpty()) {
            String oldFileName = attach.getOriginalFilename(); //原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);  //原文件后缀
            int fileSize = 500000;
            if (attach.getSize() > fileSize) { //上传文件大小不得超过50K
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "system/goods/add";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {
                String fileName = "Goods" + Math.round(Math.random() * 1000) + "." + prefix;
                //本地资源服务器资源
                File targetFile = new File("C:\\DPAN\\Apache24\\htdocs\\images\\" + fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    logger.error("添加商品失败!",e);
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_1);
                    return "system/goods/add";
                }
                logoPicPath = "/images/" + fileName;
                logoLocPath = "C:\\DPAN\\Apache24\\htdocs\\images\\" + fileName;
            } else {
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                return "system/goods/add";
            }
        }
        goods.setLogoPicPath(logoPicPath);
        goods.setLogoLocPath(logoLocPath);
        goods.setState(3);   //添加商品默认状态未审核
        if (goodsService.insertGoods(goods)) {  //添加商品信息
            GoodsStock goodsStock = new GoodsStock();
            Goods g = goodsService.checkGoodsName(goods.getGoodsName());
            goodsStock.setGoodsId(g.getId());
            goodsStock.setStock(Integer.parseInt(stock));
            if (goodsService.insertGoodsStock(goodsStock)) {//添加商品库存与销量
                System.out.println("添加商品成功啦!");
                return "redirect:/system/goods/list";
            }
        }
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName(goods.getGoodsName());
        goodsLog.setContent("已成功添加商品!");
        goodsService.insertLog(goodsLog);
        return "system/goods/add";
    }

    /**
     * 查看单体商品信息
     * @param goodsId 商品id
     * @param model model
     * @return
     */
    @RequestMapping(value = "/goodsView/{goodsId}")
    public String viewGoods(@PathVariable String goodsId, Model model) {
        Goods goods = null;
        try {
            goods = goodsService.getGoodsById(Integer.parseInt(goodsId));
        } catch (Exception e) {
            logger.error("查看单体商品信息失败,",e);
        }
        model.addAttribute(goods);
        return "system/goods/view";
    }

    /**
     * 跳转到商品修改页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/goodsModify/{id}")
    public String modifyGoods(@PathVariable String id, Model model) {
        logger.info("商品ID:" + id);
        Goods goods = null;
        try {
            goods = goodsService.getGoodsById(Integer.parseInt(id));
        } catch (Exception e) {
            logger.error("跳转商品修改页面失败,",e);
        }
        model.addAttribute("goods", goods);
        return "system/goods/modify";
    }

    /**
     * 保存修改商品信息
     * @param goods  商品实体类
     * @param request  request
     * @param stock  库存
     * @param attach attach
     * @return
     */
    @RequestMapping(value = "/goodsModifySave", method = RequestMethod.POST)
    public String modifyGoodsSave(Goods goods, HttpServletRequest request,
                                  @RequestParam(value = "stock", required = false) String stock,
                                  @RequestParam(value = "attach", required = false) MultipartFile attach)throws Exception{
        String logoPicPath = null;
        String logoLocPath = null;
        if (!attach.isEmpty()) {
            String oldFileName = attach.getOriginalFilename(); //原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);  //原文件后缀
            int fileSize = 500000;
            if (attach.getSize() > fileSize) { //上传文件大小不得超过50K
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "system/goods/modify";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {
                String fileName = "Goods" + Math.round(Math.random() * 1000) + "." + prefix;
                File targetFile = new File("C:\\DPAN\\Apache24\\htdocs\\images\\" + fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdir();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_1);
                    return "system/goods/modify";
                }
                logoPicPath = "/images/" + fileName;
                logoLocPath = "C:\\DPAN\\Apache24\\htdocs\\images\\" + fileName;
            } else {
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                return "system/goods/modify";
            }
        }
        goods.setLogoPicPath(logoPicPath);
        goods.setLogoLocPath(logoLocPath);
        Goods g = goodsService.update(goods); //修改商品信息
        if (goodsService.updateStockByAdmin(Integer.parseInt(stock), g.getId())) {//修改商品库存
            System.out.println("修改商品成功啦!");
            return "redirect:/system/goods/list";
        }
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName(g.getGoodsName());
        goodsLog.setContent("已成功修改商品基本信息!");
        goodsService.insertLog(goodsLog);
        return "system/goods/modify";
    }

    /**
     * 管理员修改商品库存
     * @param goodsId  商品id
     * @param stock   商品库存
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyStock")
    public String modifyStock(@RequestParam String goodsId,@RequestParam String stock)throws Exception{
        goodsService.updateStockByAdmin(Integer.parseInt(stock),Integer.parseInt(goodsId));
        Goods goods = goodsService.getGoodsById(Integer.parseInt(goodsId));
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName(goods.getGoodsName());
        goodsLog.setContent("调整商品库存为:"+stock);
        goodsService.insertLog(goodsLog);
        return "redirect:/system/goods/list";
    }


    /**
     * 修改商品时，删除图片
     * @param id  商品id
     * @return
     */
    @RequestMapping(value = "/delFile.json")
    @ResponseBody
    public String deleteGoodsLogo(@RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        if (id == null || id.equals("")) {
            map.put("result", Constants.RESULT_FALSE);
            return JSONArray.toJSONString(map);
        }
        String fileLocPath = (goodsService.getGoodsById(Integer.parseInt(id))).getLogoLocPath();
        File file = new File(fileLocPath);
        if (file.exists()) {
            if (file.delete()) {  //删除服务器存储的物理文件
                if (goodsService.deleteGoodsLogo(Integer.parseInt(id))) {  //更新表
                    map.put("result", Constants.RESULT_TRUE);
                }
            }
        }
        return JSONArray.toJSONString(map);
    }


    /**
     * 删除商品信息、相关库存信息及本地LOGO图片
     * @param id  商品id
     * @return
     */
    @RequestMapping({"/delGoods.json"})
    @ResponseBody
    public String deleteGoods(@RequestParam String id)throws Exception{
        logger.info("要删除的商品id" + id);
        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            map.put("result", Constants.RESULT_ERROR);
            return JSONArray.toJSONString(map);
        }
        if (goodsService.deleteGoodsById(Integer.parseInt(id))) {
            map.put("result", Constants.RESULT_TRUE);
        } else {
            map.put("result", Constants.RESULT_FALSE);
        }
        Goods goods = goodsService.getGoodsById(Integer.parseInt(id));
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName(goods.getGoodsName());
        goodsLog.setContent("已删除商品!");
        goodsService.insertLog(goodsLog);
        return JSONArray.toJSONString(map);
    }

    /**
     * 上、下架商品
     * @param state 商品状态id
     * @param id 商品id
     * @return
     */
    @RequestMapping({"/sale.json"})
    @ResponseBody
    public String saleSwitch(@RequestParam String state, @RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            map.put("result", "error");
            return JSONArray.toJSONString(map);
        }
        Goods goods = goodsService.getGoodsById(Integer.parseInt(id));
        Goods g = new Goods();
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName(goods.getGoodsName());
        if (state.equals("2") || state.equals("3")) {  //待审核或者下架，变成上架
            g.setState(1);
            goodsLog.setContent("上架该商品!");
        } else {
            g.setState(2);
            goodsLog.setContent("下架该商品!");
        }
        goodsService.insertLog(goodsLog);  //记录商品上下架记录
        g.setId(Integer.parseInt(id));
        Goods goods1 = goodsService.update(g);
        if(goods1 != null){
            map.put("result", Constants.RESULT_TRUE);
        }else {
            map.put("result", Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 后台商品分类查看
     * @param model model
     * @param parent_id 父类id
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping({"/categoryList"})
    public String getCategoryList(Model model,@RequestParam(value = "parent_id", required = false) String parent_id,
                                  @RequestParam(value = "pageIndex", required = false) String pageIndex)throws Exception {
        logger.info("父节点是:" + parent_id + "页吗是;" + pageIndex);
        List<GoodsCategory> categoryList = null;
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Integer parentId = null;
        if (parent_id != null && !parent_id.equals("")) {
            parentId = Integer.valueOf(parent_id);
        }
        //总数量表
        int totalCount = 0;
        totalCount = goodsCategoryService.getCategoryCount(parentId);
        logger.info("总数量:" + totalCount);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        logger.info("当前页码：" + currentPageNo + "每页条数:" + pageSize);
        categoryList = goodsCategoryService.getCategoryList(parentId, currentPageNo, pageSize);
        model.addAttribute("pages", pages);
        model.addAttribute("categoryList", categoryList);
        return "system/goodsCategory/list";
    }

    /**
     * 跳转至分类添加页面
     * @return
     */
    @RequestMapping({"/addCategory"})
    public String addCategory() {
        return "system/goodsCategory/add";
    }

    /**
     * 添加商品分类保存
     * @param goodsCategory  商品分类实体类
     * @param categoryName   商品分类名称
     * @param parentId  商品分类id
     * @return
     * @throws Exception
     */
    @RequestMapping("/categoryAddSave")
    public String addCategorySave(GoodsCategory goodsCategory,
                                  @RequestParam(value = "categoryName", required = false) String categoryName,
                                  @RequestParam(value = "parentId", required = false) String parentId) throws Exception {
        logger.info("分类名称是:" + categoryName + "父节点id是:" + parentId);
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName("无");
        if (parentId.equalsIgnoreCase("0")) {
            goodsCategory.setParentId(null);
            goodsLog.setContent("添加商品一级分类:"+categoryName);
        } else {
            goodsCategory.setParentId((short) Integer.parseInt(parentId));
            goodsLog.setContent("添加商品二级分类:"+categoryName);
        }
        goodsService.insertLog(goodsLog);//记录商品分类添加的日志
        goodsCategory.setCategoryName(categoryName);
        if (goodsCategoryService.insert(goodsCategory)) {
            return "redirect:/system/goods/categoryList";
        }
        return "system/goodsCategory/add";
    }

    /**
     * 进入商品分类编辑页面
     * @param cid   商品分类id
     * @param model   model
     * @return  商品分类页面
     */
    @RequestMapping("/categoryEdit")
    public String categoryEdit(@RequestParam String cid,Model model){
        try {
            GoodsCategory goodsCategory = goodsCategoryService.getCategory(Integer.parseInt(cid));
            model.addAttribute("goodsCategory",goodsCategory);
        } catch (Exception e) {
            logger.error("查看分类失败!!",e);
        }
        return "system/goodsCategory/modify";
    }

    /**
     * 保存商品分类更改
     * @param goodsCategory  商品分类实体类
     * @return
     */
    @RequestMapping(value = "/categoryModifySave",method = RequestMethod.POST)
    public String categoryModifySave(GoodsCategory goodsCategory){
        logger.info("修改分类名称::"+goodsCategory.getCategoryName()+goodsCategory.getId());
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName("无");
        goodsLog.setContent("修改商品分类为:"+goodsCategory.getCategoryName()+",该分类id是"+goodsCategory.getId());
        goodsService.insertLog(goodsLog);
        try{
            goodsCategoryService.updateCategory(goodsCategory);
        }catch (Exception e){
            logger.error("更新分类错误,",e);
            return "system/goodsCategory/modify";
        }
        return "redirect:/system/goods/categoryList";
    }

    /**
     * 删除一级商品分类时,检查二级分类下是否含有商品
     * @param id id
     * @return
     */
    @RequestMapping("/checkTwoCategory")
    @ResponseBody
    public String checkTwoCategory(@RequestParam String id){
        HashMap<String, String> map = new HashMap<>();
        try {
            GoodsCategory goodsCategory = goodsCategoryService.getByParentId(Integer.parseInt(id));
            if(goodsCategory != null){
                map.put("result",Constants.RESULT_TRUE);
            }else {
                map.put("result",Constants.RESULT_FALSE);
            }
        } catch (Exception e) {
            logger.error("查二级分类下是否含有商品失败:",e);
            map.put("result",Constants.RESULT_ERROR);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 删除二级商品分类时,检查二级分类下是否含有商品
     * @param id 商品分类id
     * @return
     */
    @RequestMapping("/checkGoods")
    @ResponseBody
    public String checkGoods(@RequestParam String id){
        HashMap<String, String> map = new HashMap<>();
        try {
            Goods goods = goodsService.checkByCategory(Integer.parseInt(id));
            if(goods != null){
                map.put("result",Constants.RESULT_TRUE);
            }else {
                map.put("result",Constants.RESULT_FALSE);
            }
        } catch (Exception e) {
            logger.error("检查该分类下是否含有商品失败:",e);
            map.put("result",Constants.RESULT_ERROR);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 删除商品分类
     * @param id  商品分类id
     * @return
     */
    @RequestMapping("/delCategory")
    @ResponseBody
    public String delCategory(@RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        GoodsCategory goodsCategory = goodsCategoryService.getCategory(Integer.parseInt(id));
        GoodsLog goodsLog = new GoodsLog();
        goodsLog.setGoodsName("无");
        goodsLog.setContent("已删除商品分类:"+goodsCategory.getCategoryName());
        try {
            if(goodsCategoryService.deleteCategory(Integer.parseInt(id))){
                goodsService.insertLog(goodsLog);
                map.put("result",Constants.RESULT_TRUE);
            }else {
                map.put("result",Constants.RESULT_FALSE);
            }
        } catch (Exception e) {
            logger.error("删除商品分类失败:",e);
            map.put("result",Constants.RESULT_ERROR);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 查看商品操作日志信息列表
     * @param model model
     * @param goodsName 商品名称
     * @param queryCreateTime 创建时间
     * @param pageIndex 当前页码
     * @return
     * @throws Exception
     */
    @RequestMapping("/logList")
    public String getLogList(Model model,@RequestParam(value = "goodsName",required = false) String goodsName,
                             @RequestParam(value = "createTime",required = false) String queryCreateTime,
                             @RequestParam(value = "pageIndex", required = false) String pageIndex)throws Exception{
        List<GoodsLog> goodsLogList = new ArrayList<>();
        int pageSize = Constants.pageSizeAddress;  //页面大小
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        int totalCount = goodsService.getLogCount(goodsName,queryCreateTime); //总数量表
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();   //总页数
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        try{
            goodsLogList = goodsService.getLogList(goodsName,queryCreateTime,currentPageNo,pageSize);
        }catch (Exception e){
            logger.error("查询日志列表出错:",e);
        }
        model.addAttribute("goodsLogList", goodsLogList);
        model.addAttribute("goodsName",goodsName);
        model.addAttribute("createTime",queryCreateTime);
        model.addAttribute("pages", pages);
        return "system/goods/logList";
    }

    /**
     * 管理员删除操作日志表
     * @param id 日志id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteLog")
    @ResponseBody
    public String deleteGoodsLog(@RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        if(goodsService.deleteLog(Integer.parseInt(id))){
            map.put("result",Constants.RESULT_TRUE);
        }else {
            map.put("result",Constants.RESULT_ERROR);
            throw new Exception("删除日志异常!");
        }
        return JSONArray.toJSONString(map);
    }
}
