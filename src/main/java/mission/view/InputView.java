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
}

