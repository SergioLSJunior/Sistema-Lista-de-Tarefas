package com.rh.testerh.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class ComponentsUtils {
    public static <T, V> void createGridColumn(Grid<T> grid, Function<T, V> valueExtractor, String header, Class<V> columnType) {
        if (columnType.equals(LocalDate.class)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            grid.addColumn(new TextRenderer<>(item -> {
                        LocalDate date = (LocalDate) valueExtractor.apply(item);
                        return date != null ? dateFormatter.format(date) : "";
                    }))
                    .setHeader(header)
                    .setAutoWidth(true);
        } else if (columnType.equals(BigDecimal.class)) {
            grid.addColumn(new TextRenderer<>(item -> {
                        BigDecimal decimal = (BigDecimal) valueExtractor.apply(item);
                        return decimal != null ? formatBigDecimal(decimal) : "";
                    }))
                    .setHeader(header)
                    .setAutoWidth(true);
        } else {
            grid.addColumn(new TextRenderer<>(item -> String.valueOf(valueExtractor.apply(item))))
                    .setHeader(header)
                    .setAutoWidth(true);
        }
        grid.setColumnReorderingAllowed(true);
    }

    private static String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "";
        }
        return String.format("%,.2f", value);
    }

    public static <T, V> void setBinder(HasValue<?, V> field, ValueProvider<T, V> valueProvider, Setter<T, V> setter, Binder<T> binder) {
        binder.forField(field)
                .asRequired()
                .bind(valueProvider, setter);
    }

    public static <T, V> void createFormOnGrid(
            ValueProvider<T, V> getter,
            Setter<T, V> setter,
            Grid<T> grid,
            HasValue<?, V> field,
            String headerName,
            Binder<T> binder
    ) {
        setBinder(field, getter, setter, binder);
        grid.getColumns().stream()
                .filter(column -> column.getHeaderText().equalsIgnoreCase(headerName))
                .findFirst()
                .ifPresent(column -> column.setEditorComponent((Component) field));
    }

}
