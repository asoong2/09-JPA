package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

// 빈 등록 될 수 있도록 어노테이션 추가
@Service
public class MenuService {
    // 내가 사용하고자 하는 객체를 의존성 주입해서 사용함
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapeer;

    public MenuService(MenuRepository menuRepository, ModelMapper modelMapeer) {
        this.menuRepository = menuRepository;
        this.modelMapeer = modelMapeer;
    }

    public MenuDTO findMenuByCode(int menuCode) {

        // Entity로 조회한 뒤 비영속 객체인 MenuDTO로 변환해서 반환한다.
        // Optional : NPE를 안전하게 처리할 수 없을까? null처리를 위한 다양한 메소드 존재
        // IllegalArgumentException : 메뉴 코드가 올바르지 않을 때 발생하는 Exception (넘어온 파라미터가 부적합하다)
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);

        return modelMapeer.map(menu, MenuDTO.class); // (소스, 결과타입) 메뉴 엔티티의 내용들을 MenuDTO형태로 옮겨담는다
    }

}
