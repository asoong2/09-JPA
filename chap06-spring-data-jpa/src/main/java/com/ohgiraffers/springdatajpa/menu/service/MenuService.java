package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Category;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// 빈 등록 될 수 있도록 어노테이션 추가
@Service
public class MenuService {
    // 내가 사용하고자 하는 객체를 의존성 주입해서 사용함
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapeer;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, ModelMapper modelMapeer) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapeer = modelMapeer;
    }

    /* 1. id로 entity 조회 : findById */
    public MenuDTO findMenuByCode(int menuCode) {

        // Entity로 조회한 뒤 비영속 객체인 MenuDTO로 변환해서 반환한다.
        // Optional : NPE를 안전하게 처리할 수 없을까? null처리를 위한 다양한 메소드 존재
        // IllegalArgumentException : 메뉴 코드가 올바르지 않을 때 발생하는 Exception (넘어온 파라미터가 부적합하다)
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);

        return modelMapeer.map(menu, MenuDTO.class); // (소스, 결과타입) 메뉴 엔티티의 내용들을 MenuDTO형태로 옮겨담는다
    }

    /* 2. 모든 entity 조회 : findAll(Sort) */
    public List<MenuDTO> findMenuList() {
        // Sort.by(필드명 작성 O, 컬럼명 작성 X).desc... => 역순으로 정렬
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());

        return menuList.stream().map(menu -> modelMapeer.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 2-2. 페이징 된 entity 조회 : findAll(Pageable) */
    public Page<MenuDTO> findMenuList(Pageable pageable) {

        // 3항 연산자 : 0이하면 0, 0이 아니라면 -1
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("menuCode").descending());

        Page<Menu> menuList = menuRepository.findAll(pageable);

        return menuList.map(menu -> modelMapeer.map(menu, MenuDTO.class));
    }

    /* 3. Query Method Test */
    public List<MenuDTO> findByMenuPrice(Integer menuPrice) {

        //List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice);
        //List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuPrice(menuPrice);
        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice, Sort.by("menuPrice").descending());
        return menuList.stream().map(menu -> modelMapeer.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 4. JPQL or Native Query Test */
    public List<CategoryDTO> findAllCategory() {

        List<Category> categoryList = categoryRepository.findAllCategory();
        return categoryList.stream()
                .map(category -> modelMapeer.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    /* 5. Entity 저장 */
    @Transactional
    public void registNewMenu(MenuDTO menu) {
        // 넘어온 파라미터 값이 엔티티가 아니므로 엔티티로 변환
        menuRepository.save(modelMapeer.map(menu, Menu.class));
    }

    /* 6. Entity 수정 */
    @Transactional
    public void modifyMenu(MenuDTO menu) {
        Menu foundMenu = menuRepository.findById(menu.getMenuCode()).orElseThrow(IllegalArgumentException::new);
        // 반환된 엔티티는 영속성 컨텍스트에서 관리 되는 객체임 => 종료될 때 변경이 감지되면서 update 구문 작성
        foundMenu.setMenuName(menu.getMenuName());
    }

    /* 7. Entity 삭제 */
    @Transactional
    public void deleteMenu(Integer menuCode) {
        menuRepository.deleteById(menuCode);
    }


    public List<MenuDTO> findByMenuCodeOrMenuName(Integer menuCode, String menuName) {
        List<Menu> menuList = menuRepository.findByMenuCodeOrMenuName(menuCode,menuName);
        return menuList.stream().map(menu -> modelMapeer.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    public List<MenuDTO> findByMenuPriceBetween(Integer menuPrice1, Integer menuPrice2) {
        List<Menu> menuList = menuRepository.findByMenuPriceBetween(menuPrice1, menuPrice2);
        return menuList.stream().map(menu -> modelMapeer.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    public List<MenuDTO> findByMenuNameContaining(String menuName) {
        List<Menu> menuList = menuRepository.findByMenuNameContaining(menuName);
        return menuList.stream().map(menu -> modelMapeer.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }
}
