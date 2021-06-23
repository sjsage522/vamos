package io.wisoft.vamos.domain.category;

import lombok.Getter;

@Getter
public enum CategoryName {

    DIGITAL_DEVICE("DIGITAL_DEVICE", "디지털기기"),
    HOME_APPLIANCES("HOME_APPLIANCES", "생활가전"),
    FURNITURE_INTERIOR("FURNITURE_INTERIOR", "가구/인테리어"),
    INFANT_CHILD("INFANT_CHILD", "유아동"),
    TODDLER_BOOK("TODDLER_BOOK", "유아도서"),
    PROCESSED_FOOD("PROCESSED_FOOD", "생활/가공식품"),
    SPORTS_LEISURE("SPORTS_LEISURE", "스포츠/레저"),
    WOMEN_FASHION("WOMEN_FASHION", "여성패션/잡화"),
    MEN_FASHION("MEN_FASHION", "남성패션/잡화"),
    GAME_HOBBY("GAME_HOBBY", "게임/취미"),
    BEAUTY("BEAUTY", "뷰티/미용"),
    PET_SUPPLIES("PET_SUPPLIES", "반려동물용품"),
    BOOKS_TICKETS_RECORDS("BOOKS_TICKETS_RECORDS", "도서/티켓/음반"),
    PLANT("PLANT", "식물"),
    ETC("ETC", "기타 중고물품"),
    WANT_BUY("WANT_BUY", "삽니다")
    ;

    private String en;
    private String kr;

    CategoryName(String en, String kr) {
        this.en = en;
        this.kr = kr;
    }
}
