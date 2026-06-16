package com.grocio.backend.service;

import com.grocio.backend.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponCalculator {

    public double calculateDiscount(Coupon coupon, Double cartTotal) {
        double discountAmount = 0.0;

        // FLAT Discount Logic (ఉదా: ₹50 ఆఫ్)
        if ("FLAT".equalsIgnoreCase(coupon.getDiscountType())) {
            discountAmount = coupon.getDiscountValue();
        } 
        // PERCENTAGE Discount Logic (ఉదా: 10% ఆఫ్)
        else if ("PERCENT".equalsIgnoreCase(coupon.getDiscountType())) {
            discountAmount = (cartTotal * coupon.getDiscountValue()) / 100.0;
            
            // Maximum Discount Limit Check (మాక్స్ క్యాపింగ్ ₹100 దాటకూడదు లాంటి రూల్స్)
            if (coupon.getMaxDiscount() != null && discountAmount > coupon.getMaxDiscount()) {
                discountAmount = coupon.getMaxDiscount();
            }
        }

        // సేఫ్టీ చెక్: డిస్కౌంట్ అమౌంట్ ఎప్పుడూ కార్ట్ టోటల్ బిల్లును దాటకూడదు
        if (discountAmount > cartTotal) {
            discountAmount = cartTotal;
        }

        return discountAmount;
    }
}