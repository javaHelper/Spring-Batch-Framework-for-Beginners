package com.springbatch.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

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
	@Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
	private String productCategory;
	@Max(100000)
	private Integer productPrice;
}
