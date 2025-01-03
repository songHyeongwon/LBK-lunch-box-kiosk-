from selenium import webdriver
from selenium.webdriver.common.by import By
from dataclasses import dataclass, field
from typing import List
import time
import sys
import json
import hashlib

sys.stdout.reconfigure(encoding='utf-8')

def generate_consistent_id(prefix: str, unique_string: str) -> str:
    """Generate a consistent unique ID using a prefix and a unique string."""
    hash_object = hashlib.md5(unique_string.encode("utf-8"))
    hash_id = hash_object.hexdigest()[:12]  # 해시 값의 일부를 사용
    return f"{prefix}-{hash_id}"

@dataclass
class HsdBrand:
    id: str
    name: str

@dataclass
class HsdCategory:
    id: str
    name: str
    parent_id: str = None

@dataclass
class HsdMenu:
    id: str
    name: str
    price: int
    image_url: str
    category_id: str
    brand_id: str

options = webdriver.ChromeOptions()
options.add_argument("--headless")                      # 헤드리스 모드 활성화
options.add_argument("--disable-gpu")                   # GPU 비활성화 (Windows에서 필수)
options.add_argument("--window-size=1920,1080")         # 창 크기 설정
options.add_argument("--disable-extensions")            # 크롬 확장 프로그램 비활성화
options.add_argument("--no-sandbox")                    # --no-sandbox 옵션
options.add_argument("--disable-dev-shm-usage")         # 공유 메모리 문제 해결용

driver = webdriver.Chrome(options=options)              # 설정된 옵션을 기반으로 Chrome WebDriver 객체 생성
driver.get("https://www.hsd.co.kr/menu/menu_list")      # 특정 URL에 접근
time.sleep(1)                                           # 페이지 로드 대기

brand_name = "한솥 도시락"
brand = HsdBrand(id=generate_consistent_id("B", brand_name), name=brand_name)  # 단일 브랜드 생성
categories = []  # 모든 카테고리를 저장할 리스트
menus = []        # 모든 메뉴를 저장할 리스트

# 대분류 메뉴 가져오기
main_menu_items = driver.find_elements(By.CSS_SELECTOR, ".lnb .dp1")

for main_menu in main_menu_items:
    try:
        # 대분류 클릭
        main_title = main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").text
        main_category = HsdCategory(id=generate_consistent_id("C", f"{brand_name}:{main_title}"), name=main_title)
        categories.append(main_category)
        main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").click()
        time.sleep(1)

        # 소분류 메뉴 가져오기
        sub_menu_items = main_menu.find_elements(By.CSS_SELECTOR, ".dp2 li")
        for sub_menu in sub_menu_items:
            sub_title = sub_menu.find_element(By.CSS_SELECTOR, "a").text
            sub_category = HsdCategory(id=generate_consistent_id("C", f"{brand_name}:{main_title}:{sub_title}"), name=sub_title, parent_id=main_category.id)
            categories.append(sub_category)
            sub_menu.find_element(By.CSS_SELECTOR, "a").click()
            time.sleep(1)

            # 메뉴 데이터 가져오기
            menu_items = driver.find_elements(By.CSS_SELECTOR, ".menu_cont .item")
            for item in menu_items:
                title = item.find_element(By.CSS_SELECTOR, ".item-text h4").text
                price_text = item.find_element(By.CSS_SELECTOR, ".item-price strong").text.replace(",", "")
                img_url = item.find_element(By.CSS_SELECTOR, ".item-img img").get_attribute("src")
                price = int(price_text) if price_text.isdigit() else 0

                # 메뉴를 저장
                menu = HsdMenu(
                    id=generate_consistent_id("M", f"{brand_name}:{main_title}:{sub_title}:{title}"),
                    name=title,
                    price=price,
                    image_url=img_url,
                    category_id=sub_category.id,
                    brand_id=brand.id
                )
                menus.append(menu)

    except Exception as e:
        print(f"오류 발생: {e}")

# 드라이버 종료
driver.quit()

# JSON 변환
result = {
    "brand": brand.__dict__,
    "categories": [category.__dict__ for category in categories],
    "menus": [menu.__dict__ for menu in menus]
}

# JSON 출력
print(json.dumps(result, ensure_ascii=False, indent=4))
