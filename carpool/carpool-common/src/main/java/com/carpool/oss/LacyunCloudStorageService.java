package com.carpool.oss;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.entity.ContentType;
import org.springframework.web.multipart.MultipartFile;

import com.carpool.utils.RRException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;

/**
 * 腾讯云存储
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-26 20:51
 */
public class LacyunCloudStorageService extends CloudStorageService {

	@Override
	public String upload(MultipartFile file) throws Exception {

		String fileName = file.getOriginalFilename();
		
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		System.out.println(fileName);
		System.out.println(prefix);
		return upload(file.getInputStream(), getPath("upload") + "." + prefix);
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		
		return config.getQcloudDomain() + path;
	}

	@Override
	public String upload(byte[] data, String path) {
		// 这个方法在腾讯新版sdk中已经弃用
		return null;
	}
	
	public void delete(String path) {
	}
}
