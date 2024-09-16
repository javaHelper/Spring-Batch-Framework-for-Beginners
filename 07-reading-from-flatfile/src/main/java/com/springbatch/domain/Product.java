package com.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

	private Integer productId;
	private String productName;
	private String productCategory;
	private Integer productPrice;
}
