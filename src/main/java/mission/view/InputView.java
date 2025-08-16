package mission.view;
import api.Console;

public class InputView {

    public String read() {
        System.out.println("출발지를 입력해주세요. \n> ");
        String departure = Console.readLine();
        System.out.println("도착지를 입력해주세요. \n> ");
        String destination = Console.readLine();

        return departure +"," + destination;
    }

    public String readDelivery() {
        System.out.println("배송 정보를 입력해 주세요. ex)봉화군청-숭실대학교 정보과학관(박호건)");
        System.out.print("> ");

        return Console.readLine();
    }
}

