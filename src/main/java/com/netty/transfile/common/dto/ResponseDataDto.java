package com.netty.transfile.common.dto;

import com.netty.transfile.common.enumeration.ResponseCode;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDto {

	private String opcode;
	private int data;

	public void readData() {
		String responseType = this.opcode;
		String responseMessage = ResponseCode.getMessageOf(data);

		System.out.println("responseType: " + responseType);
		System.out.println("responseMessage: " + responseMessage);
	}
}
