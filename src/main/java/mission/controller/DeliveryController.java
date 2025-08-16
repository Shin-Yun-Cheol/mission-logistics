package mission.controller;

import mission.exception.MissionException;
import mission.model.DeliveryOrder;
import mission.service.DeliveryService;
import mission.util.InputParser;
import mission.view.InputView;
import mission.view.OutputView;

import java.util.concurrent.atomic.AtomicInteger;

public class DeliveryController {

    private final DeliveryService deliveryService;
    private final InputView inputView;
    private final OutputView outputView;
    private final AtomicInteger nextId = new AtomicInteger(0);

    public DeliveryController(DeliveryService deliveryService, InputView inputView, OutputView outputView) {
        this.deliveryService = deliveryService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    /** 여러 배송을 연달아 입력받아 각각 스레드로 처리 */
    public void run() {
        while (true) {
            String raw = inputView.readDelivery();
            if (raw == null)
                break;

            String trimmed = raw.trim();

            if (trimmed.isEmpty()) {
                break;
            }

            try {
                DeliveryOrder order = InputParser.parseDelivery(trimmed);
                int id = nextId.incrementAndGet();
                outputView.printAccepted(id);

                int minutes = deliveryService.estimateMinutes(order.departure(), order.destination());
                outputView.printStarted(id, minutes);

                int delaySec = deliveryService.toRealSeconds(minutes);

                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(delaySec * 1000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    outputView.printCompleted(id);
                }, "delivery-" + id);

                t.start();

            } catch (MissionException | IllegalArgumentException ex) {
                outputView.printError(ex.getMessage());
            }
        }
    }
}
