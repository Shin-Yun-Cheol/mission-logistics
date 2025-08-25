package mission.controller;

import mission.exception.MissionException;
import mission.model.DeliveryOrder;
import mission.repository.OrderLog;
import mission.service.DeliveryService;
import mission.util.InputParser;
import mission.view.InputView;
import mission.view.OutputView;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DeliveryController {

    private final DeliveryService deliveryService;
    private final InputView inputView;
    private final OutputView outputView;
    private final AtomicInteger nextId = new AtomicInteger(0);
    private final OrderLog orderLog;

    // 동시 실행 제한: 최대 5개
    private final Semaphore capacity = new Semaphore(5, true);

    public DeliveryController(DeliveryService deliveryService, InputView inputView, OutputView outputView, OrderLog orderLog) {
        this.deliveryService = deliveryService;
        this.inputView = inputView;
        this.outputView = outputView;
        this.orderLog = orderLog;
    }

    // 여러 배송을 연달아 입력받아 각각 스레드로 처리
    public void run() {
        while (true) {
            inputView.showMainMenu();
            int sel = inputView.readMenuSelection();
            if (sel == 0)
                break;
            if (sel == 1) {
                handleOrder();
            } else if (sel == 2) {
                handleSearch();
            } else {
                outputView.printError("잘못된 번호입니다. 0, 1, 2 중에서 선택하세요.");
            }
        }
    }

    // 입력을 비동기 처리(세마포어로 동시 5건 제한)
    private void handleOrder() {
        String raw = inputView.readDelivery();
        if (raw == null)
            return;

        String trimmed = raw.trim();
        if (trimmed.isEmpty())
            return;

        try {
            DeliveryOrder order = InputParser.parseDelivery(trimmed);
            final int id = nextId.incrementAndGet();
            outputView.printAccepted(id);

            Thread t = new Thread(() -> {
                boolean acquired = false;
                try {
                    capacity.acquire();
                    acquired = true;

                    int minutes = deliveryService.estimateMinutes(order.departure(), order.destination());
                    outputView.printStarted(id, minutes);

                    // 로그 기록(요구 형식 그대로 한 줄)
                    orderLog.append(id, order.customer(), order.departure(), order.destination(), minutes);

                    int delaySec = deliveryService.toRealSeconds(minutes);
                    Thread.sleep(delaySec * 1000L);

                    outputView.printCompleted(id);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (MissionException ex) {
                    outputView.printError(ex.getMessage());
                } finally {
                    if (acquired) capacity.release();
                }
            }, "delivery-" + id);

            t.start();

        } catch (IllegalArgumentException | MissionException ex) {
            outputView.printError(ex.getMessage());
        }
    }
    private void handleSearch() {
        String name = inputView.readSearchName();
        if (name == null || name.trim().isEmpty()) {
            outputView.printError("검색어를 입력해 주세요.");
            return;
        }
        List<String> rows = orderLog.findByCustomerLines(name);
        outputView.printSearchHeader();
        outputView.printSearchRows(rows);
    }
}
