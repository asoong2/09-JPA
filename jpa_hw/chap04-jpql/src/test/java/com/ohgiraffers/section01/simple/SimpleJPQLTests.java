package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleJPQLTests {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    @Test
    public void TypedQuery를_이용한_단일메뉴_조회_테스트() {
        //when
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7"; // 별칭 필수 !
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class); // 수행하고 싶은 jpql 구문 , 반환받을 데이터 타입 지정
        String resultMenuName = query.getSingleResult();
        //then
        assertEquals("민트미역국", resultMenuName);
    }

    @Test
    public void Query를_이용한_단일메뉴_조회_테스트() {
        //when
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7";
        Query query = entityManager.createQuery(jpql);
        Object resultMenuName = query.getSingleResult(); // 반환값 타입이 지정되지 않았기 때문에 상위 타입인 Object로 반환
        //then
        assertEquals("민트미역국", resultMenuName);
    }

    @Test
    public void TypedQuery를_이용한_단일행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m WHERE m.menuCode = 7"; // 전체 컬럼 조회시 별칭만
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class); // 결과 타입은 메뉴여야 함(전체 컬럼 조회)
        Menu foundMenu = query.getSingleResult(); // 메뉴 코드 7번에 대한 정보를 한 행으로 받기 때문에 SingleResult()
        //then
        assertEquals(7,foundMenu.getMenuCode());
        System.out.println(foundMenu);
    }

    @Test
    public void TypedQuery를_이용한_다중행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m"; // 엔티티에 있는 모든 메뉴 조회
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        List<Menu> foundMenuList =  query.getResultList();
        //then
        assertNotNull(foundMenuList); // List기 때문에 확인 코드 NotNull로 변경
        foundMenuList.forEach(System.out::println); // 리스트 반환 시, 한 줄시 출력하기 위해 forEach 사용
    }

    @Test
    public void Query를_이용한_다중행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m";
        Query query = entityManager.createQuery(jpql);  // 반환 타입을 지정하지 못하기 때문에 제네릭도 생성될 수 없음
        List<Menu> foundMenuList =  query.getResultList();
        //then
        assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);
    }

    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트() {
        //when
        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        List<Integer> categoryCodeList = query.getResultList();
        //then
        assertNotNull(categoryCodeList);
        categoryCodeList.forEach(System.out::println);
    }

    @Test
    public void in_연산자를_활용한_조회_테스트() {
        // 카테고리 코드가 6이거나 10인 menu 조회
        //when
        String jpql = "SELECT m FROM menu_section01 m WHERE m.categoryCode IN (6,10)";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    public void like_연산자를_활용한_조회_테스트() {
        // "마늘"이 메뉴 이름으로 들어간 menu 조회
        //when
        String jpql = "SELECT m FROM menu_section01 m WHERE m.menuName LIKE '%마늘%'";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
