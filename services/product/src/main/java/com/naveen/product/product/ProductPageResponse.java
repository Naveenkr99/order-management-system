package com.naveen.product.product;

import java.util.List;

public record ProductPageResponse(
        int pageNo,
        long totalNoOfData,
        List<ProductResponse> data
) {
}

