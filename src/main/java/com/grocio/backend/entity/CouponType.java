package com.grocio.backend.entity;

public enum CouponType {
    PUBLIC,      // అందరికీ కనిపించేవి
    PRIVATE,     // కేవలం కొంతమంది సెలెక్టెడ్ యూజర్లకి మాత్రమే
    REFERRAL,    // ఫ్రెండ్స్ కి రిఫర్ చేసినప్పుడు వచ్చేవి
    FIRST_ORDER, // మొదటి ఆర్డర్ కి మాత్రమే వర్తించేవి
    LOYALTY,     // పాత కస్టమర్ల రివార్డ్స్ కోసం
    CAMPAIGN     // స్పెషల్ సేల్స్/పండుగల కోసం
}