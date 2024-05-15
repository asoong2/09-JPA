package com.ohgiraffers.section04.enumtype;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumTypeMappingTests {
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
    public void enum_타입_매핑_테스트() {
        //given
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("홍길동");
        member.setPhone("010-1234-5678");
        member.setAddress("서울시 종로구");
        member.setEnrollDate(new Date());
        member.setMemberRole(RoleType.MEMBER);
        member.setStatus("Y");
        //when
        entityManager.persist(member);
        //then
        Member foundMember = entityManager.find(Member.class, member.getMemberNo());
        assertEquals(member.getMemberNo(), foundMember.getMemberNo());
    }
}
