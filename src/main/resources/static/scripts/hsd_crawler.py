from selenium import webdriver
from selenium.webdriver.common.by import By
from dataclasses import dataclass, field
from typing import List
import time
import sys
import json

sys.stdout.reconfigure(encoding='utf-8')

@dataclass
class HsdCategory:
    name: str
    sub_categories: List['HsdCategory'] = field(default_factory=list)
    menus: List['HsdMenu'] = field(default_factory=list)

@dataclass
class HsdMenu:
    name: str
    price: int
    image_url: str

options = webdriver.ChromeOptions()
options.add_argument("--headless")                      # 헤드리스 모드 활성화
options.add_argument("--disable-gpu")                   # GPU 비활성화 (Windows에서 필수)
options.add_argument("--window-size=1920,1080")         # 창 크기 설정
options.add_argument("--disable-extensions")            # --disable-extensions 옵션: 크롬 확장 프로그램 비활성화
options.add_argument("--no-sandbox")                    # --disable-extensions 옵션: 크롬 확장 프로그램 비활성화
options.add_argument("--disable-dev-shm-usage")         # --disable-dev-shm-usage 옵션: /dev/shm 사용을 비활성화 (공유 메모리 문제 해결용)

driver = webdriver.Chrome(options=options)              # 설정된 옵션을 기반으로 Chrome WebDriver 객체 생성
driver.get("https://www.hsd.co.kr/menu/menu_list")      # 특정 URL에 접근
time.sleep(1)                                           # 페이지 로드 대기

categories = []  # 모든 카테고리를 저장할 리스트

# 대분류 메뉴 가져오기
main_menu_items = driver.find_elements(By.CSS_SELECTOR, ".lnb .dp1")

for main_menu in main_menu_items:
    try:
        # 대분류 클릭
        main_title = main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").text
        main_category = HsdCategory(name=main_title)
        main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").click()
        time.sleep(1)

        # 소분류 메뉴 가져오기
        sub_menu_items = main_menu.find_elements(By.CSS_SELECTOR, ".dp2 li")
        for sub_menu in sub_menu_items:
            sub_title = sub_menu.find_element(By.CSS_SELECTOR, "a").text
            sub_category = HsdCategory(name=sub_title)
            main_category.sub_categories.append(sub_category)
            sub_menu.find_element(By.CSS_SELECTOR, "a").click()
            time.sleep(1)

            # 메뉴 데이터 가져오기
            menu_items = driver.find_elements(By.CSS_SELECTOR, ".menu_cont .item")
            for item in menu_items:
                title = item.find_element(By.CSS_SELECTOR, ".item-text h4").text
                price_text = item.find_element(By.CSS_SELECTOR, ".item-price strong").text.replace(",", "")
                img_url = item.find_element(By.CSS_SELECTOR, ".item-img img").get_attribute("src")
                price = int(price_text) if price_text.isdigit() else 0

                # 메뉴를 하위 카테고리에 추가
                menu = HsdMenu(name=title, price=price, image_url=img_url)
                sub_category.menus.append(menu)

        categories.append(main_category)

    except Exception as e:
        print(f"오류 발생: {e}")

# 드라이버 종료
driver.quit()

# 객체를 JSON으로 변환
result = [
    {
        "name": category.name,
        "sub_categories": [
            {
                "name": sub.name,
                "menus": [
                    {
                        "name": menu.name,
                        "price": menu.price,
                        "image_url": menu.image_url
                    } for menu in sub.menus
                ]
            } for sub in category.sub_categories
        ]
    } for category in categories
]

# JSON 출력
print(json.dumps(result, ensure_ascii=False, indent=4))