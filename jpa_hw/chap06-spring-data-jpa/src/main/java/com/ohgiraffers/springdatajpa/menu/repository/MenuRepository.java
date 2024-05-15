package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// <내가 컨트롤하고 싶은 대상 엔티티, ID속성(PK)의 데이터 타입>
// 해당 타입의 인터페이스들은 전부 자동으로 빈등록이 된다 = 빈 등록 어노테이션이 필요하지 않다
public interface MenuRepository extends JpaRepository<Menu, Integer>  {

    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록 조회
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice);


    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록을 메뉴 가격 오름차순으로 조회
    List<Menu> findByMenuPriceGreaterThanOrderByMenuPrice(Integer menuPrice);


    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록을 메뉴 가격 내림차순으로 조회
    // 처음 메소드와 이름은 동일하지만 전달받는 파라미터에 정렬 기준 추가
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);


    List<Menu> findByMenuCodeOrMenuName(Integer menuCode, String menuName);

    List<Menu> findByMenuPriceBetween(Integer menuPrice1, Integer menuPrice2);

    List<Menu> findByMenuNameContaining(String menuName);

}
