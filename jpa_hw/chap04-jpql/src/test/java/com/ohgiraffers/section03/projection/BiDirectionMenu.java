package com.ohgiraffers.section03.projection;

import javax.persistence.*;
import java.util.List;

@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
public class BiDirectionMenu {

    @Id
    private int menuCode;
    private String menuName;
    private int menuPrice;

    @ManyToOne // Many = menu, One = category => 카테고리코드를 FK로 사용
    @JoinColumn(name = "categoryCode")
    private BiDirectionCategory category;   // 하나의 메뉴는 하나의 카테고리를 참조함

    private String orderableStatus;

    public BiDirectionMenu() {
    }

    public BiDirectionMenu(int menuCode, String menuName, int menuPrice, BiDirectionCategory category, String orderableStatus) {
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

    public BiDirectionCategory getCategory() {
        return category;
    }

    public void setCategory(BiDirectionCategory category) {
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
        return "BiDirectionMenu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}


