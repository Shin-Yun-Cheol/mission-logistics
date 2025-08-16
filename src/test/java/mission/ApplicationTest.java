package mission;

import api.TestEnvironment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ApplicationTest extends TestEnvironment {

    @Test
    void testApplication() throws Exception {
        // api.Console.setMockInput(...) (package-private) 리플렉션으로 호출
        Class<?> consoleClass = Class.forName("api.Console");
        try {
            // setMockInput(List<String>) 우선 시도
            Method m = consoleClass.getDeclaredMethod("setMockInput", List.class);
            m.setAccessible(true);
            m.invoke(null, List.of("봉화군청", "숭실대학교 정보과학관"));
        } catch (NoSuchMethodException e) {
            // 없으면 setMockInput(String...) 시그니처 시도
            Method m2 = consoleClass.getDeclaredMethod("setMockInput", String[].class);
            m2.setAccessible(true);
            m2.invoke(null, new Object[]{ new String[]{"봉화군청", "숭실대학교 정보과학관"} });
        }

        // 메인 실행 (출력은 콘솔에서 확인)
        runMain();
    }

    @Test
    void 배송_단건_실행_간단체크() throws Exception {
        // api.Console.setMockInput(...) 리플렉션으로 호출
        Class<?> consoleClass = Class.forName("api.Console");
        try {
            // setMockInput(List<String>) 시도
            Method m = consoleClass.getDeclaredMethod("setMockInput", List.class);
            m.setAccessible(true);
            m.invoke(null, List.of(
                    // 출발-도착(주문자) 형식, 동일 장소로 지연 최소화
                    "봉화군청-숭실대학교 정보과학관(박호건)",
                    "exit" // 컨트롤러 종료
            ));
        } catch (NoSuchMethodException e) {
            // setMockInput(String...) 시그니처 시도
            Method m2 = consoleClass.getDeclaredMethod("setMockInput", String[].class);
            m2.setAccessible(true);
            m2.invoke(null, new Object[]{ new String[] {
                    "봉화군청-숭실대학교 정보과학관(박호건)",
                    "exit"
            }});
        }

        // 메인 실행 (출력은 콘솔에서 확인)
        runMain();

        // 백그라운드 스레드가 완료 메시지를 찍을 시간(약 1초) 살짝 대기
        Thread.sleep(1200);
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
