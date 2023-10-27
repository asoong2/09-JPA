package com.ohgiraffers.section01.manytoone;

import javax.persistence.*;

@Entity(name="menu_and_category")
@Table(name="tbl_menu")
public class MenuAndCategory {
    @Id
    private int menuCode;

    private String menuName;

    private int menuPrice;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="categoryCode")   // FK에 해당하는 컬럼명
    /* 만약 참조하는 테이블의 PK 컬럼명과 현재 테이블의 FK의 컬럼명이 다를 경우,
    * 현재 테이블의 FK에 해당하는 컬럼명을 작성한다. */
    private Category category;

    private String orderableStatus;

    public MenuAndCategory() {
    }

    public MenuAndCategory(int menuCode, String menuName, int menuPrice, Category category, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "MenuAndCategory{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
