package com.enhui.util.service.impl;

import com.enhui.util.excel.upload.BetaProblemUploadData;
import com.enhui.util.mapper.BetaProblemMapper;
import com.enhui.util.service.BetaProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 胡恩会
 * @Date 2021/8/6 21:23
 **/
@Service
public class BetaProblemServiceImpl implements BetaProblemService {
    @Autowired
    private BetaProblemMapper betaProblemMapper;

    @Override
    public void save(List<BetaProblemUploadData> list) {

    }
}
