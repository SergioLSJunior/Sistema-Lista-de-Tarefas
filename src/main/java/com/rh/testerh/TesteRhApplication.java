package com.rh.testerh;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@Theme(value = "myapp")
public class TesteRhApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TesteRhApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        Locale.setDefault(new Locale("pt", "BR"));
    }
}
