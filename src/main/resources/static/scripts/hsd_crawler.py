from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import sys
sys.stdout.reconfigure(encoding='utf-8')

options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 헤드리스 모드 활성화
options.add_argument("--disable-gpu")  # GPU 비활성화 (Windows에서 필수)
options.add_argument("--window-size=1920,1080")  # 창 크기 설정
options.add_argument("--disable-extensions") # --disable-extensions 옵션: 크롬 확장 프로그램 비활성화
options.add_argument("--no-sandbox") # --disable-extensions 옵션: 크롬 확장 프로그램 비활성화
options.add_argument("--disable-dev-shm-usage") # --disable-dev-shm-usage 옵션: /dev/shm 사용을 비활성화 (공유 메모리 문제 해결용)
driver = webdriver.Chrome(options=options) # 설정된 옵션을 기반으로 Chrome WebDriver 객체 생성
driver.get("https://www.hsd.co.kr/menu/menu_list") # 특정 URL에 접근

# 페이지 로드 대기
time.sleep(1)

# 대분류 메뉴 가져오기
main_menu_items = driver.find_elements(By.CSS_SELECTOR, ".lnb .dp1")  # 대분류 메뉴

# 대분류 메뉴 순회
for main_menu in main_menu_items:
    try:
        # 대분류 클릭
        main_title = main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").text
        print(f"\n=== 대분류: {main_title} ===")
        main_menu.find_element(By.CSS_SELECTOR, ".dp1_tit > a").click()
        time.sleep(1)

        # 소분류 메뉴 가져오기 (현재 대분류의 하위 메뉴)
        sub_menu_items = main_menu.find_elements(By.CSS_SELECTOR, ".dp2 li")

        # 소분류 메뉴 순회
        for sub_menu in sub_menu_items:
            try:
                # 소분류 클릭
                sub_title = sub_menu.find_element(By.CSS_SELECTOR, "a").text
                print(f"\n--- 소분류: {sub_title} ---")
                sub_menu.find_element(By.CSS_SELECTOR, "a").click()
                time.sleep(1)

                # 현재 소분류의 메뉴 데이터 가져오기
                menu_items = driver.find_elements(By.CSS_SELECTOR, ".menu_cont .item")
                for item in menu_items:
                    title = item.find_element(By.CSS_SELECTOR, ".item-text h4").text
                    price = item.find_element(By.CSS_SELECTOR, ".item-price strong").text
                    img_url = item.find_element(By.CSS_SELECTOR, ".item-img img").get_attribute("src")
                    print(f"메뉴 이름: {title}, 가격: {price}, 이미지 URL: {img_url}")

            except Exception as e:
                print(f"소분류 '{sub_title}' 처리 중 오류 발생: {e}")

    except Exception as e:
        print(f"대분류 '{main_title}' 처리 중 오류 발생: {e}")

# 드라이버 종료
driver.quit()
