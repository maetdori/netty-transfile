package com.netty.transfile.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.dto.RequestDataDto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TransUtil {

	private static final Gson gson = new GsonBuilder().create();

	public static RequestDataDto convertRequestDataDto(String rawData) {

		RequestDataDto dataDto = null;

		Map<String, String> map = new HashMap<>();
		try {
			String[] eachLine = rawData.split(TransFileProtocol.CRLF);
			for (String data : eachLine) {
				String[] keyValue = data.split(TransFileProtocol.DELIMITER);
				String key = keyValue[0];
				String value = keyValue[1];
				map.put(key, value);
			}
			if (map.isEmpty()) {
				return null;
			}

			String json = gson.toJson(map);
			dataDto = gson.fromJson(json, RequestDataDto.class);
		} catch (Exception e) {
			log.error(e.getClass().getSimpleName() + " : {}, data={}", e.getMessage(), rawData, e);
		}

		return dataDto;
	}

	public static boolean saveFile(RequestDataDto dataDto) {

		boolean isSuccess = true;

		File directory = new File(dataDto.getDir());
		File file = new File(dataDto.getDir() + dataDto.getFileName());

		if (!directory.exists() && !directory.mkdirs()) {
			log.error("file save failed : {}", dataDto.getDir() + dataDto.getFileName());
			isSuccess = false;
		}

		if (!file.exists()) {
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(dataDto.getFile());
				fos.flush();
			} catch (IOException e) {
				log.error(e.getClass().getSimpleName() + " : {}", e.getMessage(), e);
				isSuccess = false;
			}
		}

		return isSuccess;
	}

	public static boolean removeFile(RequestDataDto dataDto) {

		boolean isSuccess = true;

		File file = new File(dataDto.getDir() + dataDto.getFileName());

		if (file.exists()) {
			if (!file.delete()) {
				isSuccess = false;
				log.error("file delete failed : {}", dataDto.getFileName());
			}
		} else {
			isSuccess = false;
		}

		return isSuccess;
	}
}
