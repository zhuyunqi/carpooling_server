package com.carpool.service.impl;

import com.carpool.dao.CommentPictureDao;
import com.carpool.entity.CommentPictureEntity;
import com.carpool.service.CommentPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 评价图片Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-29 14:45:55
 */
@Service("commentPictureService")
public class CommentPictureServiceImpl implements CommentPictureService {
    @Autowired
    private CommentPictureDao commentPictureDao;

    @Override
    public CommentPictureEntity queryObject(Integer id) {
        return commentPictureDao.queryObject(id);
    }

    @Override
    public List<CommentPictureEntity> queryList(Map<String, Object> map) {
        return commentPictureDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return commentPictureDao.queryTotal(map);
    }

    @Override
    public int save(CommentPictureEntity commentPicture) {
        return commentPictureDao.save(commentPicture);
    }

    @Override
    public int update(CommentPictureEntity commentPicture) {
        return commentPictureDao.update(commentPicture);
    }

    @Override
    public int delete(Integer id) {
        return commentPictureDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return commentPictureDao.deleteBatch(ids);
    }

    @Override
    public int deleteByCommentId(Integer commentId) {
        return commentPictureDao.deleteByCommentId(commentId);
    }
}
