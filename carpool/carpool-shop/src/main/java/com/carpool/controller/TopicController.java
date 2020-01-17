package com.carpool.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carpool.entity.TopicEntity;
import com.carpool.service.TopicService;
import com.carpool.utils.PageUtils;
import com.carpool.utils.Query;
import com.carpool.utils.R;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-20 14:10:08
 */
@RestController
@RequestMapping("topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    /**
     * 查看行程匹配列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
    	params.put("title", "行程匹配");
        //查询列表数据
        Query query = new Query(params);
        List<TopicEntity> topicList = topicService.queryList(query);
        int total = topicService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(topicList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 查看热门活动列表
     */
    @RequestMapping("/hotactlist")
    public R hotactlist(@RequestParam Map<String, Object> params) {
    	params.put("title", "热门活动");
        //查询列表数据
        Query query = new Query(params);
        List<TopicEntity> topicList = topicService.queryList(query);
        int total = topicService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(topicList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        TopicEntity topic = topicService.queryObject(id);

        return R.ok().put("topic", topic);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TopicEntity topic) {
        topicService.save(topic);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TopicEntity topic) {
        topicService.update(topic);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        topicService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<TopicEntity> list = topicService.queryList(params);

        return R.ok().put("list", list);
    }
}
