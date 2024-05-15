package com.ohgiraffers.section05.groupfunction;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class GroupFunctionTests {

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
    public void 특정_카테고리의_등록된_메뉴_수_조회() {
        //given
        int categoryCodeParameter = 3;
        //when
        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();
        //then
        assertTrue(countOfMenu >= 0);
        System.out.println(countOfMenu);
    }

    @Test
    public void count를_제외한_다른_그룹함수의_조회결과가_없는_경우_테스트() {
        //given
        int categoryCodeParameter = 2;
        //when
        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        //then
        // 이 구문 실행하면 exception이 실행될 것이라는 테스트
        // (내가 예상하는 exception 타입, 수행하고자 하는 function)
        assertThrows(NullPointerException.class, () -> {
            long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult(); // 반환 값은 null, null은 long(기본자료형)에 담길 수 없음});
        });
        // 수행할 구문만 들어가면 됨, 포인트는 래퍼 타입 사용 long -> Long
        assertDoesNotThrow(() -> {
            Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult();
            System.out.println(sumOfPrice);
        });
    }

    @Test
    public void groupby절과_having절을_사용한_조회_테스트() {
        //given
        long minPrice = 50000L;     // 정수면 Long, 실수면 Double
        //when
        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice)" +
                        // 키워드 앞에 띄어쓰기에 유의한다 !
                      " FROM menu_section05 m" +
                      " GROUP BY m.categoryCode" +
                      " HAVING SUM(m.menuPrice) >= :minPrice";
        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice)
                .getResultList();
        //then
        assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });

    }
}
