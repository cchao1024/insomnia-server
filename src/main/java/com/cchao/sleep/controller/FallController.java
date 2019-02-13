package com.cchao.sleep.controller;

import com.cchao.sleep.constant.enums.Results;
import com.cchao.sleep.dao.FallImage;
import com.cchao.sleep.json.fall.FallIndex;
import com.cchao.sleep.dao.FallMusic;
import com.cchao.sleep.exception.CommonException;
import com.cchao.sleep.json.fall.FallImageVo;
import com.cchao.sleep.json.RespBean;
import com.cchao.sleep.json.RespListBean;
import com.cchao.sleep.json.fall.FallMusicVo;
import com.cchao.sleep.service.FallService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Fall的控制器，包含 图片和音频
 */
@RestController
@RequestMapping(value = "/fall")
public class FallController {

    @Autowired
    FallService mFallService;

    /**
     * 首页数据
     */
    @RequestMapping(value = "/index")
    public RespBean<FallIndex> getFallIndex() {
        FallIndex fallIndex = new FallIndex();
//        fallIndex.setBanners(fallBannerRepository.findAll());
        fallIndex.setFallImages(mFallService.getImageByPage(0, 8).getContent());
        fallIndex.setMusic(mFallService.getMusicByPage(0, 6).getContent());
        return RespBean.suc(fallIndex);
    }

    @RequestMapping(value = "/image/getByPage")
    public RespListBean<FallImageVo> getImageByPage(@RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {

        Page<FallImage> pageObj = mFallService.getImageByPage(page, pageSize);
        List<FallImageVo> categoryTypeList = pageObj.getContent().stream()
                .map(fallImage -> {
                    FallImageVo fallImageVo = new FallImageVo();
                    BeanUtils.copyProperties(fallImage, fallImageVo);
                    return fallImageVo;
                }).collect(Collectors.toList());

        return RespListBean.of(pageObj, categoryTypeList, page);
    }

    @RequestMapping(value = "/image/add")
    public RespBean postFallImage(@ModelAttribute FallImage fallImage) {
        if (fallImage == null) {
            throw CommonException.of(Results.PARAM_EMPTY);
        }
        mFallService.save(fallImage);

        return RespBean.suc();
    }

    @RequestMapping(value = "/music/getByPage")
    public RespListBean<FallMusicVo> getMusicByPage(@RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "pageSize", defaultValue = "30") int pageSize) {

        Page<FallMusic> pageObj = mFallService.getMusicByPage(page, pageSize);
        List<FallMusicVo> categoryTypeList = pageObj.getContent().stream()
                .map(fallImage -> {
                    FallMusicVo fallImageVo = new FallMusicVo();
                    BeanUtils.copyProperties(fallImage, fallImageVo);
                    return fallImageVo;
                }).collect(Collectors.toList());

        return RespListBean.of(pageObj, categoryTypeList, page);
    }

    @RequestMapping(value = "/music/play")
    public RespBean updatePlayCount(@RequestParam(value = "id") long id) {
        mFallService.onMusicPlayed(id);
        return RespBean.suc();
    }
}