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

import java.util.List;
import java.util.function.Function;

public class ComponentsUtils {
    public static <T, V> void createGridColumn(Grid<T> grid, Function<T, V> valueExtractor, String header) {
        grid.addColumn(new TextRenderer<>(item -> String.valueOf(valueExtractor.apply(item))))
                .setHeader(header)
                .setAutoWidth(true);
        grid.setColumnReorderingAllowed(true);
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
