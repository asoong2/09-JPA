package com.ohgiraffers.section06.join;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTests {

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
    public void 내부조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c"; // menu의 category 필드를 기준으로 조인
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @Test
    public void 외부조인을_이용한_조회_테스트() {
        //when
        // RIGHT JOIN = category 기준으로 조인
        String jpql = "SELECT m.menuName, c.categoryName FROM menu_section06 m RIGHT JOIN m.category c" +
                " ORDER BY m.category.categoryCode"; // 메뉴 엔티티에서 카테고리 참조 후 카테고리 코드 기준으로 정렬
        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(row -> {
            // 하나의 오브젝트 배열이 한 행을 차지할 수 있도록 출력 구문 작성
            // 카테고리 기준 조인이기 때문에, 메뉴에 할당되지 않은 카테고리들도 출력됨 !
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @Test
    public void 컬렉션조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c LEFT JOIN c.menuList m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();
        //then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @Test
    public void 세타조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c, menu_section06 m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();
        //then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @Test
    public void 페치조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }


}
