package com.grocio.backend.service;

import com.grocio.backend.dto.ProductDetailsDTO;
import com.grocio.backend.dto.RelatedProductDTO;
import com.grocio.backend.entity.Product;
import com.grocio.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDetailsDTO getProductDetailsWithSuggestions(Long productId) {
        // 1. మెయిన్ ప్రొడక్ట్ ని తెచ్చుకోవడం
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. దానికి సంబంధించిన సజెషన్స్ (Related Products) తెచ్చుకోవడం
        List<Product> relatedProductsEntity = productRepository
                .findTop5BySubCategoryAndProductIdNotAndIsActiveTrue(product.getSubCategory(), productId);

        // 3. సజెషన్స్ ని DTO కి మార్చడం
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

        // 4. మెయిన్ ప్రొడక్ట్ డేటాని DTO కి సెట్ చేయడం
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
        response.setRating(product.getRating() != null ? product.getRating() : 4.5); // డీఫాల్ట్ రేటింగ్ 4.5

        // 5. సజెషన్స్ ని యాడ్ చేసి పంపడం
        response.setRelatedProducts(relatedProductDTOs);

        return response;
    }
}