package com.nettytransfile.model;

import com.nettytransfile.model.enumeration.ResponseCode;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDataDto {

	private char cmd;
	private String fileName;
	private int fileSize;
	private String dir;
	private byte[] file;

	private ResponseCode responseCode;
}
