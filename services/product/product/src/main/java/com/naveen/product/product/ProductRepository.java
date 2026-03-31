package com.naveen.product.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByIdInOrderById(List<Integer> ids);

    List<Product> findAllByCategory_IdOrderById(Integer categoryId);
}
