package com.lingzhi.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 事件发布服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final ApplicationEventPublisher publisher;

    /**
     * 发布事件
     *
     * @param event 事件对象
     */
    public void publish(Object event) {
        log.debug("发布事件: {}", event.getClass().getSimpleName());
        publisher.publishEvent(event);
    }

    /**
     * 发布事件（带事件源）
     *
     * @param event 事件对象
     * @param source 事件源
     */
    public void publish(Object event, Object source) {
        log.debug("发布事件: {}, source={}", event.getClass().getSimpleName(), source);
        publisher.publishEvent(event);
    }
}
