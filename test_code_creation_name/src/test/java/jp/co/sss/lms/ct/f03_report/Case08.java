package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.ConfigUtils;
import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	void test01() {
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

		getEvidence("Case08/02-1.Login_Before");

		// ログインボタンをクリック
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case08/02-2.Login_Success");

		// 成功判定
		assertTrue(currentUrl.contains("/course/detail") && title.contains("コース詳細"),
				"コース詳細画面への遷移に失敗しました");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		// Config から提出済セクション日付を取得
		String sectionDate = ConfigUtils.getSubmitSectionDate();

		// 日付列を持つ行を検索
		WebElement sectionRow = WebDriverUtils.webDriver.findElement(
				By.xpath("//tr[td[contains(text(),'" + sectionDate + "')]]"));

		// 「詳細」ボタンをクリック
		sectionRow.findElement(By.cssSelector("input[type='submit'][value='詳細']")).click();
		Thread.sleep(2000);

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case08/03-1.SectionDetail_Submitted");

		// 成功判定
		assertTrue(currentUrl.contains("/section/detail") && title.contains("セクション詳細"),
				"セクション詳細画面への遷移に失敗しました");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		// 提出済みレポートの「確認する」ボタンを取得
		WebElement confirmButton = WebDriverUtils.webDriver.findElement(
				By.xpath("//input[contains(@value,'を確認する')]"));
		confirmButton.click();

		// ページ遷移待機
		Thread.sleep(2000);
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case08/04-1.Report_Confirm");

		// 成功判定
		assertTrue(currentUrl.contains("/report/regist") && title.contains("レポート登録"),
				"レポート登録画面への遷移に失敗しました");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws Exception {
		// レポート登録画面のフォームを取得
		WebElement reportForm = WebDriverUtils.webDriver.findElement(By.tagName("form"));

		getEvidence("Case08/05-1.Report_EditBefore");

		// テキストエリアの更新
		WebElement textArea = reportForm.findElement(By.tagName("textarea"));
		textArea.clear();
		textArea.sendKeys(ConfigUtils.getReportUpdateText());

		getEvidence("Case08/05-2.Report_EditAfter");

		// 「提出する」ボタンをクリック
		WebElement submitButton = reportForm.findElement(By.cssSelector("button[type='submit']"));
		submitButton.click();
		Thread.sleep(2000);

		getEvidence("Case08/05-3.Report_Resubmit");

		// 提出後、セクション詳細画面に戻る
		// 再度セクション詳細画面の提出ボタンを取得
		WebElement confirmButton = WebDriverUtils.webDriver.findElement(
				By.cssSelector("input[type='submit']"));
		String buttonText = confirmButton.getAttribute("value");

		// 成功判定
		assertTrue(buttonText.contains("提出済み"),
				"提出済みに更新されていません。");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws Exception {
		// 「ようこそ○○さん」リンクを取得
		WebElement welcomeLink = WebDriverUtils.webDriver.findElement(
				By.xpath("//a[small[contains(text(),'ようこそ')]]"));

		// リンクをクリック
		welcomeLink.click();
		Thread.sleep(2000);

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case08/06-1.UserDetail");

		// 成功判定（URLとタイトルで確認）
		assertTrue(
				(currentUrl.contains("/user/detail") || currentUrl.contains("/user/myAccount"))
						&& title.contains("ユーザー"),
				"ユーザー詳細画面への遷移に失敗しました。");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws Exception {
		// Config から提出済みセクション日付と更新内容を取得
		String sectionDate = ConfigUtils.getSubmitSectionDate();
		String expectedText = ConfigUtils.getReportUpdateText();

		// 提出済み日付のレポート行を特定
		WebElement reportRow = WebDriverUtils.webDriver.findElement(
				By.xpath("//tr[td[contains(text(),'" + sectionDate + "')]]"));

		// 「詳細」ボタンをクリック
		WebElement detailButton = reportRow.findElement(
				By.cssSelector("input[type='submit'][value='詳細']"));
		detailButton.click();
		Thread.sleep(2000);

		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		String title = WebDriverUtils.webDriver.getTitle();

		getEvidence("Case08/07-1.ReportDetail");

		// 成功判定（URLとタイトルで確認）
		assertTrue(
				currentUrl.contains("/report/detail") && title.contains("レポート詳細"),
				"レポート詳細画面への遷移に失敗しました。");

		// 修正内容が画面内に表示されているか確認
		boolean isUpdatedTextDisplayed = WebDriverUtils.webDriver.getPageSource().contains(expectedText);
		assertTrue(
				isUpdatedTextDisplayed,
				"修正内容がレポート詳細画面に反映されていません。");
	}
}
