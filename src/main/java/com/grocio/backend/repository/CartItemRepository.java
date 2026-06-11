package com.grocio.backend.repository;

import com.grocio.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // 🟢 1. కార్ట్ ఐడీ మరియు ప్రొడక్ట్ ఐడీ ని బట్టి ఒక నిర్దిష్ట ఐటెమ్ ని వెతకడం
    CartItem findByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);
    
    // 🟢 2. ఒక కార్ట్ లోని మొత్తం ఐటెమ్స్ ని ఒకేసారి డిలీట్ చేయడం (ఉదా: చెకౌట్ అయ్యాక కార్ట్ క్లియర్ చేయడానికి)
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.cartId = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);
    
    // 🟢 3. కార్ట్ లోని ఒక పర్టిక్యులర్ ఐటెమ్ ని డిలీట్ చేయడం (యూజర్ రిమూవ్ నొక్కినప్పుడు)
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.cartId = :cartId AND c.product.productId = :productId")
    void deleteByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
}