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

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
