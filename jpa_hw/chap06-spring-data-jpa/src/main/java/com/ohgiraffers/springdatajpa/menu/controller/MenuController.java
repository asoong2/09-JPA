package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagenation;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuCode}") // url로 넘어온 값을 @PathVariable로 뽑아올 수 있다
    public String findMenuByCode(@PathVariable int menuCode, Model model) {
        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);
        return "menu/detail";
    }

    /* 페이징 이전 버전 */
/*    @GetMapping("/list")
    public String findMenuList(Model model) {
        List<MenuDTO> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);
        return "menu/list";
    }*/

    /* 페이징 이후 버전 */
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model) {

        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
        log.info("pageable : {}", pageable); // 이 객체가 뭘 가지고 있는지?

        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        /* Page 객체가 담고 있는 정보 */
        log.info("조회한 내용 목록 : {}", menuList.getContent());    //List<MenuDTO>
        log.info("총 페이지 수 : {}", menuList.getTotalPages());
        log.info("총 메뉴 수 : {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시 될 요소 수 : {}", menuList.getSize());
        log.info("해당 페이지에 실제 요소 수 : {}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부 : {}", menuList.isFirst());
        log.info("마지막 페이지 여부 : {}", menuList.isLast());
        log.info("정렬 방식 : {}", menuList.getSort());
        log.info("여러 페이지 중 현재 인덱스 : {}", menuList.getNumber());

        PagingButtonInfo paging = Pagenation.getPagingButtonInfo(menuList);

        model.addAttribute("menuList", menuList);
        model.addAttribute("paging", paging);
        return "menu/list";
    }

    @GetMapping("/querymethod")
    public void queryMethod() {}

    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model) {

        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);
        model.addAttribute("menuList", menuList);

        return "menu/searchResult";
    }

    /* 숙제 1. 메뉴 이름 혹은 메뉴 코드로 조회하기 */
    @GetMapping("/querymethod2")
    public void queryMethod2() {}

    @GetMapping("/search2")
    public String findByMenuCodeOrMenuName(@RequestParam(name = "menuCode", required = false) Integer menuCode,
                                            @RequestParam(name = "menuName", required = false) String menuName,
                                            Model model) {
        List<MenuDTO> menuList = menuService.findByMenuCodeOrMenuName(menuCode, menuName);
        model.addAttribute("menuList", menuList);
        return "menu/searchCodeOrName";
    }

    /* 숙제 2. 원하는 가격대 조회하기 */
    @GetMapping("/querymethod3")
    public void queryMethod3() {}

    @GetMapping("/search3")
    public String findByMenuPriceBetween(@RequestParam(name = "menuPrice1") Integer menuPrice1,
                                     @RequestParam(name = "menuPrice2") Integer menuPrice2,
                                     Model model) {
        List<MenuDTO> menuList = menuService.findByMenuPriceBetween(menuPrice1, menuPrice2);
        model.addAttribute("menuList", menuList);
        return "menu/searchPriceBetween";
    }

    /* 숙제 3. 특정 단어를 포함하는 메뉴 찾기 */
    @GetMapping("/querymethod4")
    public void queryMethod4() {}

    @GetMapping("/search4")
    public String findByMenuNameContaining(@RequestParam(name="menuName") String menuName,
                                           Model model) {
        List<MenuDTO> menuList = menuService.findByMenuNameContaining(menuName);
        model.addAttribute("menuList", menuList);
        return "menu/searchNameContaining";
    }



    @GetMapping("/regist")
    public void registPage() {}


    @GetMapping("/category")
    // 데이터로 응답 : 응답 바디에 들어갈 데이터
    @ResponseBody
    public List<CategoryDTO> findCategoryList(){
        // jpql로 동작할 구문 호출
        return menuService.findAllCategory();
    }

    @PostMapping("regist")
    public String registMenu(MenuDTO menu) {

        menuService.registNewMenu(menu);

        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage() {}

    @PostMapping("/modify")
    public String modifyMenu(MenuDTO menu) {

        menuService.modifyMenu(menu);

        return "redirect:/menu/" + menu.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage() {}

    @PostMapping("delete")
    public String deleteMenu(@RequestParam Integer menuCode) {

        menuService.deleteMenu(menuCode);

        return "redirect:/menu/list";
    }
}
