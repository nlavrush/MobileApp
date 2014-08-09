package com.eccentex.dcm.MobileApp.Authentication;

import org.apache.http.entity.FileEntity;

public interface ProgressiveEntityListener {
    void onFileUpload(String response, String fileName, FileEntity fileEntity, String responseId);
}
