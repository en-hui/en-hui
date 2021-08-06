package com.enhui.util.service;

import com.enhui.util.excel.upload.BetaProblemUploadData;

import java.util.List;

public interface BetaProblemService {
    void save(List<BetaProblemUploadData> list);
}
