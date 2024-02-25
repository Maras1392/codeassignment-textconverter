package com.homework.textconverter.config;

import com.homework.textconverter.service.TextConversionProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SpringApplicationEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ApplicationArguments applicationArguments;

    private final TextConversionProcessor conversionProcessor;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        String[] args = applicationArguments.getSourceArgs();
        if (args.length == 0) {
            log.error("No filepath provided.");
            throw new RuntimeException("No filepath provided. Please re-run with filepath specified");
        } else if (args.length==1) {
            String filepath = args[0];
            this.conversionProcessor.convertText(filepath, null);
        } else {
            String filepath = args[0];
            String type = args[1];
            this.conversionProcessor.convertText(filepath, type);
        }
    }
}