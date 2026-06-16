package com.grocio.backend.controller;

import com.grocio.backend.dto.CouponValidationRequest;
import com.grocio.backend.dto.CouponValidationResponse;
import com.grocio.backend.entity.CartCoupon;
import com.grocio.backend.repository.CartCouponRepository;
import com.grocio.backend.service.CouponService;
import com.grocio.backend.service.CouponValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CartCouponRepository cartCouponRepository;

    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(@RequestBody CouponValidationRequest request) {
        
        CouponValidationResult result = couponService.processCoupon(
                request.getCode(), 
                request.getUserId(), 
                request.getCartTotal()
        );

        CouponValidationResponse response = new CouponValidationResponse(
                result.isSuccess(),
                result.getMessage(),
                result.getCouponCode(),
                result.getDiscountAmount(),
                result.getFinalAmount()
        );

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        // 🟢 మీ ఐడియా ప్రకారం: వాలిడేషన్ సక్సెస్ అవ్వగానే ఆటోమేటిక్‌గా టేబుల్ లో సేవ్/అప్‌డేట్ చేస్తున్నాం
        CartCoupon cartCoupon = new CartCoupon();
        cartCoupon.setUserId(request.getUserId());
        cartCoupon.setCouponCode(result.getCouponCode());
        cartCouponRepository.save(cartCoupon);

        return ResponseEntity.ok(response);
    }

    // 🟢 ఫీచర్: కస్టమర్ కార్ట్ పేజీ కి రాగానే ఆల్రెడీ సేవ్ అయి ఉన్న కూపన్ ని తెలుసుకోవడానికి API
    @GetMapping("/cart/active/{userId}")
    public ResponseEntity<Map<String, Object>> getActiveCartCoupon(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        var cartCouponOpt = cartCouponRepository.findById(userId);
        if (cartCouponOpt.isPresent()) {
            response.put("hasCoupon", true);
            response.put("couponCode", cartCouponOpt.get().getCouponCode());
        } else {
            response.put("hasCoupon", false);
        }
        return ResponseEntity.ok(response);
    }

    // 🟢 ఫీచర్: కూపన్ రికూవ్ చేసినా లేదా కార్ట్ టోటల్ 0 అయినా డేటాబేస్ నుండి క్లియర్ చేయడానికి API
    @PostMapping("/cart/remove/{userId}")
    public ResponseEntity<Map<String, Object>> removeCartCoupon(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        cartCouponRepository.deleteById(userId);
        response.put("success", true);
        response.put("message", "Cart coupon cleared successfully");
        return ResponseEntity.ok(response);
    }

    // 🟢 ఫ్లట్టర్ నుండి కూపన్ పక్కాగా సేవ్ అవ్వడానికి ప్రత్యేక API
    @PostMapping("/cart/save")
    public ResponseEntity<Map<String, Object>> saveCartCoupon(@RequestBody CartCoupon request) {
        Map<String, Object> response = new HashMap<>();
        cartCouponRepository.save(request);
        response.put("success", true);
        response.put("message", "Coupon saved to cart database successfully!");
        return ResponseEntity.ok(response);
    }
}