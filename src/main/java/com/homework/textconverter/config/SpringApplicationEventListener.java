package com.homework.textconverter.config;

import com.homework.textconverter.service.TextConversionProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        try {
            String[] args = applicationArguments.getSourceArgs();
            if (args.length == 2 &&
                    !(StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[1]))) {

                String filepath = args[0];
                String type = args[1];
                this.conversionProcessor.convertText(filepath, type);
            } else {
                log.error("Invalid inputs. Received {} invalid inputs", args.length);
                throw new RuntimeException("Invalid inputs. Expected exactly two valid parameters. First for filepath, second for conversion type.");
            }
        } catch (Exception e) {
            log.error("Process failed", e);
        }
    }
}