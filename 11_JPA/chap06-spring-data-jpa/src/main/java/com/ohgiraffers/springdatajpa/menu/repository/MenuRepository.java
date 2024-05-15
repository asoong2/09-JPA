package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer>  {
    // <내가 컨트롤하고 싶은 대상 엔티티, ID속성(PK)의 데이터 타입>
    // 해당 타입의 인터페이스들은 전부 자동으로 빈등록이 된다 = 빈 등록 어노테이션이 필요하지 않다

}
