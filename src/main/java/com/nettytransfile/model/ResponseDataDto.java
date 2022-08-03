package com.nettytransfile.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDto {

	private String opcode;
	private int data;
}
