package io.wisoft.vamos.domain.category;

import lombok.Getter;

@Getter
public enum CategoryName {

    DIGITAL_DEVICE(1L, "DIGITAL_DEVICE", "디지털기기"),
    HOME_APPLIANCES(2L, "HOME_APPLIANCES", "생활가전"),
    FURNITURE_INTERIOR(3L, "FURNITURE_INTERIOR", "가구/인테리어"),
    INFANT_CHILD(4L, "INFANT_CHILD", "유아동"),
    TODDLER_BOOK(5L, "TODDLER_BOOK", "유아도서"),
    PROCESSED_FOOD(6L, "PROCESSED_FOOD", "생활/가공식품"),
    SPORTS_LEISURE(7L, "SPORTS_LEISURE", "스포츠/레저"),
    WOMEN_FASHION(8L, "WOMEN_FASHION", "여성패션/잡화"),
    MEN_FASHION(9L, "MEN_FASHION", "남성패션/잡화"),
    GAME_HOBBY(10L, "GAME_HOBBY", "게임/취미"),
    BEAUTY(11L, "BEAUTY", "뷰티/미용"),
    PET_SUPPLIES(12L, "PET_SUPPLIES", "반려동물용품"),
    BOOKS_TICKETS_RECORDS(13L, "BOOKS_TICKETS_RECORDS", "도서/티켓/음반"),
    PLANT(14L, "PLANT", "식물"),
    ETC(15L, "ETC", "기타 중고물품"),
    WANT_BUY(16L, "WANT_BUY", "삽니다")
    ;

    private Long no;
    private String en;
    private String kr;

    CategoryName(Long no, String en, String kr) {
        this.no = no;
        this.en = en;
        this.kr = kr;
    }
}
