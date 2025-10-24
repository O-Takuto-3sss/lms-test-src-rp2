package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

import jp.co.sss.lms.ct.util.ConfigUtils;
import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		// ログイン画面を開く
		goTo("http://localhost:8080/lms/");

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		// 成功判定
		assertTrue(currentUrl.contains("/lms/") && title.contains("ログイン"),
				"ログイン画面への遷移に失敗しました");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		// ConfigUtilsからIDとパスワードを取得
		String loginId = ConfigUtils.getSuccessLoginId();
		String password = ConfigUtils.getSuccessPassword();
		WebDriverUtils.webDriver.findElement(By.id("loginId")).sendKeys(loginId);
		WebDriverUtils.webDriver.findElement(By.id("password")).sendKeys(password);

		getEvidence("Case03/02-1.Login_Before");

		// ログインボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case03/02-2.Login_Success");

		// 成功判定
		assertTrue(currentUrl.contains("/course/detail") && title.contains("コース詳細"),
				"コース詳細画面への遷移に失敗しました");
	}
}
