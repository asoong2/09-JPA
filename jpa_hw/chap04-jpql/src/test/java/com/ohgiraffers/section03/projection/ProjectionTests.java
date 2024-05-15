package com.ohgiraffers.section03.projection;

import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectionTests {
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
    public void 단일_엔터티_프로젝션_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        menuList.get(0).setMenuName("test1"); // 영속성 컨텍스트에서 관리된다면, 커밋됐을 때 DB 업데이트가 될 것
        entityTransaction.commit();
    }

    @Test
    public void 양방향_연관관계_엔터티_프로젝션_테스트() {
        //given
        int menuCodeParameter = 3;
        //when
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();
        //then : 양방향이기 때문에 역방향 탐색도 가능
        System.out.println(categoryOfMenu);
        System.out.println(categoryOfMenu.getMenuList());

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        categoryOfMenu.setCategoryName("test2");
        categoryOfMenu.getMenuList().get(1).setMenuName("test3");
        entityTransaction.commit();
    }

    @Test
    public void 임베디드_타입_프로젝션_테스트() {
        //when
        String jpql = "SELECT m.menuInfo FROM embedded_menu m"; // menuInfo는 엔티티가 아니고, 임베디드 타입
        List<MenuInfo> menuInfoList = entityManager.createQuery(jpql, MenuInfo.class).getResultList();
        //then
        assertNotNull(menuInfoList);
        menuInfoList.forEach(System.out::println);
    }

    @Test
    public void TypedQuery를_이용한_스칼라_타입_프로젝션_테스트() {
        //when
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();
        //then
        assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);
    }

    @Test
    public void Query를_이용한_스칼라_타입_프로젝션_테스트() {
        //when
        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";
        // 두 가지를 모두 반환받을 타입이 없으므로 오브젝트의 배열 형태로 반환받는다
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();
        //then
        assertNotNull(categoryList);
        // 오브젝트 배열 안에 있는 걸 하나하나 다시 반복해줘야 함(그냥 반복했을 경우 오브젝트 배열들이 반환됨)
        categoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println); // 오브젝트 배열을 스트림으로 만들어달라는 요청
        });
    }

    @Test
    public void new_명령어를_활용한_프로젝션_테스트() {
        //when
        // CategoryInfo 클래스의 매개변수 생성자를 호출해서 이용!
        String jpql = "SELECT new com.ohgiraffers.section03.projection.CategoryInfo(c.categoryCode, c.categoryName) FROM category_section03 c";
        List<CategoryInfo> categoryList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();
        //then
        assertNotNull(categoryList);
        categoryList.forEach(System.out::println);
    }
}
