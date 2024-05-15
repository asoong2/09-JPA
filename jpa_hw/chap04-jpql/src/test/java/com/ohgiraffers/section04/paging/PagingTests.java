package com.ohgiraffers.section04.paging;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PagingTests {

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
    public void 페이징_API를_이용한_조회_테스트() {
        //given
        int offset = 10; // 10번까지 건너뛰고 싶다, 즉 11번 시작
        int limit = 5; // 조회해오고자 하는 컨텐츠 개수, 즉 15번 끝
        //when
        String jpql = "SELECT m FROM menu_section04 m ORDER BY m.menuCode DESC";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class) // 타입드쿼리는 여러가지 설정을 해줄 수 있음
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
