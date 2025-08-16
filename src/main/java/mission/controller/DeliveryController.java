package mission.controller;

import mission.exception.MissionException;
import mission.model.DeliveryOrder;
import mission.service.DeliveryService;
import mission.util.InputParser;
import mission.view.InputView;
import mission.view.OutputView;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DeliveryController {

    private final DeliveryService deliveryService;
    private final InputView inputView;
    private final OutputView outputView;
    private final AtomicInteger nextId = new AtomicInteger(0);

    // 동시 실행 제한: 최대 5개
    private final Semaphore capacity = new Semaphore(5, true);

    public DeliveryController(DeliveryService deliveryService, InputView inputView, OutputView outputView) {
        this.deliveryService = deliveryService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    // 여러 배송을 연달아 입력받아 각각 스레드로 처리
    public void run() {
        while (true) {
            String raw = inputView.readDelivery();
            if (raw == null)
                break;

            String trimmed = raw.trim();
            if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                DeliveryOrder order = InputParser.parseDelivery(trimmed);
                int id = nextId.incrementAndGet();
                outputView.printAccepted(id); // 접수는 즉시

                Thread t = new Thread(() -> {
                    boolean acquired = false;
                    try {
                        // 자리가 없으면 대기 메시지
                        if (capacity.availablePermits() == 0) {
                            System.out.printf("배송이 대기 중입니다. (id: %d)%n", id);
                        }

                        // 시작을 위한 슬롯 획득 지점
                        capacity.acquire();
                        acquired = true;

                        // 실제 시작 시에만 예상시간 계산/출력
                        int minutes = deliveryService.estimateMinutes(order.departure(), order.destination());
                        outputView.printStarted(id, minutes);

                        int delaySec = deliveryService.toRealSeconds(minutes);
                        Thread.sleep(delaySec * 1000L);

                        outputView.printCompleted(id);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (MissionException ex) {
                        outputView.printError(ex.getMessage());
                    } finally {
                        if (acquired) {
                            capacity.release(); // 완료 후 슬롯 반환
                        }
                    }
                }, "delivery-" + id);

                t.start();

            } catch (MissionException | IllegalArgumentException ex) {
                outputView.printError(ex.getMessage());
            }
        }
    }
}
