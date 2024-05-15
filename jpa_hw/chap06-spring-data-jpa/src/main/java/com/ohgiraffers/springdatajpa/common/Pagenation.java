package com.ohgiraffers.springdatajpa.common;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import org.springframework.data.domain.Page;

public class Pagenation {
    public static PagingButtonInfo getPagingButtonInfo(Page<MenuDTO> menuList) {
        int currentPage = menuList.getNumber() + 1; // 0부터 시작하니까 +1
        int defaultButtonCount = 10;
        int startPage
                = (int)(Math.ceil((double)currentPage / defaultButtonCount) -1) * defaultButtonCount + 1;
        int endPage = startPage + defaultButtonCount -1;
        // endPage가 20인데 TotalPage가 13인경우 endPage를 TotalPage와 동일하게 변경
        if(menuList.getTotalPages() < endPage) endPage = menuList.getTotalPages();
        if(menuList.getTotalPages() == 0 && endPage == 0) endPage = startPage;

        return new PagingButtonInfo(currentPage, startPage, endPage);
    }
}
