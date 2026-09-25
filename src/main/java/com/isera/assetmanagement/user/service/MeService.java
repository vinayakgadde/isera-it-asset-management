package com.isera.assetmanagement.user.service;

import com.isera.assetmanagement.user.dto.MeResponse;
import com.isera.assetmanagement.user.dto.MyAssetResponse;
import com.isera.assetmanagement.user.dto.MyAssignmentResponse;

import java.util.List;

public interface MeService {

    MeResponse getCurrentUser();

    List<MyAssetResponse> getMyAssets();

    List<MyAssignmentResponse> getMyAssignments();
}