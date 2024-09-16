package com.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OSProduct extends Product {
	
	private Integer taxPercent;
	private String sku;
	private Integer shippingRate;
	
}
