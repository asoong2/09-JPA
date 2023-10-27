package com.ohgiraffers.section03.persistencecontext;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class A_EntityLifeCycleTests {
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
    public void 비영속성_테스트() {
        //given
        Menu foundMenu = entityManager.find(Menu.class, 11);
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());
        //when
        boolean isTrue = (foundMenu == newMenu);
        //then
        assertFalse(isTrue);
    }

    @Test
    public void 영속성_연속_조회_테스트() {
        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);
        //when
        boolean isTrue = (foundMenu1 == foundMenu2);
        //then
        assertTrue(isTrue);
    }

    @Test
    public void 영속성_객체_추가_테스트() {
        //given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");
        //when
        entityManager.persist(menuToRegist);
        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToRegist == foundMenu);
        //then
        assertTrue(isTrue);
    }

    @Test
    public void 영속성_객체_추가_값_변경_테스트() {
        //given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");
        //when
        entityManager.persist(menuToRegist);
        menuToRegist.setMenuName("메론죽");
        Menu foundMenu = entityManager.find(Menu.class, 500);
        //then
        assertEquals("메론죽", foundMenu.getMenuName());
    }

    @Test
    public void 준영속성_detach_테스트() {
        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);
        //when
        entityManager.detach(foundMenu2);
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);
        //then
        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 준영속성_clear_테스트() {
        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);
        //when
        entityManager.clear();
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);
        //then
        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 준영속성_close_테스트() {
        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);
        //when
        entityManager.close();
        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);
        //then
        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 삭제_remove_테스트() {
        //given
        Menu foundMenu = entityManager.find(Menu.class, 2);
        //when
        entityManager.remove(foundMenu);
        Menu refoundMenu = entityManager.find(Menu.class, 2);
        //then
        assertEquals(2, foundMenu.getMenuCode());
        assertEquals(null, refoundMenu);
    }

    @Test
    public void 병합_merge_수정_테스트() {
        //given
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);
        //when
        menuToDetach.setMenuName("수박죽");
        Menu refoundMenu = entityManager.find(Menu.class, 2);
        entityManager.merge(menuToDetach);
        //then
        Menu mergedMenu = entityManager.find(Menu.class, 2);
        assertEquals("수박죽", mergedMenu.getMenuName());
    }

    @Test
    public void 병합_merge_삽입_테스트() {
        //given
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);
        //when
        menuToDetach.setMenuCode(999);  // DB에서 조회할 수 없는 키 값으로 변경
        menuToDetach.setMenuName("수박죽");
        entityManager.merge(menuToDetach);  //영속 상태의 엔티티와 병합해야 하지만 존재하지 않을 경우 삽입 된다.
        //then
        Menu mergedMenu = entityManager.find(Menu.class, 999);
        assertEquals("수박죽", mergedMenu.getMenuName());
    }









}
