package com.grocio.backend.coupon.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocio.backend.cart.entity.CartCoupon;
import com.grocio.backend.cart.repository.CartCouponRepository;
import com.grocio.backend.coupon.dto.BestCouponResponse;
import com.grocio.backend.coupon.dto.CouponValidationRequest;
import com.grocio.backend.coupon.dto.CouponValidationResponse;
import com.grocio.backend.coupon.dto.CouponValidationResult;
import com.grocio.backend.coupon.enums.CouponStatus;
import com.grocio.backend.coupon.repository.CouponRepository;
import com.grocio.backend.coupon.service.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CartCouponRepository cartCouponRepository;
    private final CouponRepository couponRepository;

    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(@RequestBody CouponValidationRequest request) {

        CouponValidationResult result = couponService.processCoupon(
                request.getCode(),
                request.getUserId(),
                request.getCartTotal());

        CouponValidationResponse response = new CouponValidationResponse(
                result.isSuccess(),
                result.getMessage(),
                result.getCouponCode(),
                result.getDiscountAmount(),
                result.getFinalAmount());

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        // 🟢 మీ ఐడియా ప్రకారం: వాలిడేషన్ సక్సెస్ అవ్వగానే ఆటోమేటిక్‌గా టేబుల్ లో
        // సేవ్/అప్‌డేట్ చేస్తున్నాం
        CartCoupon cartCoupon = new CartCoupon();
        cartCoupon.setUserId(request.getUserId());
        cartCoupon.setCouponCode(result.getCouponCode());
        cartCouponRepository.save(cartCoupon);

        return ResponseEntity.ok(response);
    }

    // 🟢 ఫీచర్: కస్టమర్ కార్ట్ పేజీ కి రాగానే ఆల్రెడీ సేవ్ అయి ఉన్న కూపన్ ని
    // తెలుసుకోవడానికి API
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

    // 🟢 ఫీచర్: కూపన్ రికూవ్ చేసినా లేదా కార్ట్ టోటల్ 0 అయినా డేటాబేస్ నుండి
    // క్లియర్ చేయడానికి API
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

    @GetMapping("/available/{userId}")
    public ResponseEntity<?> getAvailableCoupons(
            @PathVariable Long userId,
            @RequestParam Double cartTotal) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("coupons",
                couponService.getAvailableCoupons(userId, cartTotal));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/best/{userId}")
    public ResponseEntity<BestCouponResponse> getBestCoupon(
            @PathVariable Long userId,
            @RequestParam Double cartTotal) {

        return ResponseEntity.ok(
                couponService.getBestCoupon(userId, cartTotal));
    }

    @GetMapping("/details/{code}")
    public ResponseEntity<?> getCouponDetails(
            @PathVariable String code) {

        return couponRepository
                .findByCodeAndStatus(code.toUpperCase(), CouponStatus.ACTIVE)
                .map(coupon -> ResponseEntity.ok(coupon))
                .orElse(ResponseEntity.notFound().build());
    }

}