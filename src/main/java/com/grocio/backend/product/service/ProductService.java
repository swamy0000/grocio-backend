package com.grocio.backend.product.service;

import com.grocio.backend.product.dto.ProductDetailsDTO;
import com.grocio.backend.product.dto.ProductResponse;
import com.grocio.backend.product.dto.RelatedProductDTO;
import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.mapper.ProductMapper;
import com.grocio.backend.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsBySubCategory(Long subCategoryId) {
        return productRepository.findBySubCategory_IdAndIsActiveTrue(subCategoryId);
    }

    public ProductDetailsDTO getProductDetailsWithSuggestions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Product> relatedProductsEntity = productRepository
                .findTop5BySubCategoryAndProductIdNotAndIsActiveTrue(product.getSubCategory(), productId);

        List<RelatedProductDTO> relatedProductDTOs = relatedProductsEntity.stream().map(rp -> {
            RelatedProductDTO rpDTO = new RelatedProductDTO();
            rpDTO.setId(rp.getProductId());
            rpDTO.setName(rp.getName());
            rpDTO.setPrice(rp.getPrice());
            rpDTO.setOldPrice(rp.getOldPrice());
            rpDTO.setUnit(rp.getUnit());
            rpDTO.setImageUrl(rp.getImageUrl());
            return rpDTO;
        }).collect(Collectors.toList());

        ProductDetailsDTO response = new ProductDetailsDTO();
        response.setId(product.getProductId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setOldPrice(product.getOldPrice());
        response.setUnit(product.getUnit());
        response.setImageUrl(product.getImageUrl());
        response.setBadge(product.getBadge());
        response.setStock(product.getStockQuantity());
        response.setDescription(product.getDescription());
        response.setShelfLife(product.getShelfLife());
        response.setCountryOfOrigin(product.getOrigin());
        response.setRating(product.getRating() != null ? product.getRating() : 4.5);

        response.setRelatedProducts(relatedProductDTOs);

        return response;
    }

    public Map<String, Object> getBestSellers() {
        List<ProductResponse> products = productRepository
                .findTop10ByIsActiveTrueOrderByProductIdDesc()
                .stream()
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setProductId(product.getProductId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setOldPrice(product.getOldPrice());
                    dto.setUnit(product.getUnit());
                    dto.setImageUrl(product.getImageUrl());
                    dto.setBadge(product.getBadge());
                    dto.setRating(product.getRating());
                    return dto;
                }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("products", products);

        return response;
    }

    public Map<String, Object> getProductsByCategory(Long categoryId) {
        List<ProductResponse> products = productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("products", products);

        return response;
    }
}

