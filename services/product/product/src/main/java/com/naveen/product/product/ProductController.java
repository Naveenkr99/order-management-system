package com.naveen.product.product;

import com.naveen.order_management.ratelimit.ProductListRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService service;
    private final ProductListRateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<List<Integer>> createProduct(
            @RequestBody @NotEmpty(message = "At least one product is required") List<@Valid ProductRequest> request
    ) {
        return ResponseEntity.ok(service.createProducts(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request
    ) {
        return ResponseEntity.ok(service.purchaseProducts(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(service.findById(productId));
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<List<ProductResponse>> findByCategoryId(
            @PathVariable("category-id") Integer categoryId
    ) {
        return ResponseEntity.ok(service.findAllByCategoryId(categoryId));
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> findAll(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "pageNo must be 0 or greater") int pageNo,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "pageSize must be at least 1") int pageSize,
            HttpServletRequest httpServletRequest
    ) {
        rateLimiter.validateRequest(extractClientKey(httpServletRequest));
        return ResponseEntity.ok(service.findAll(pageNo, pageSize));
    }

    private String extractClientKey(HttpServletRequest request) {
        var forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
