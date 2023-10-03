package com.rh.testerh.ui.view;

import com.rh.testerh.Dictionary;
import com.rh.testerh.model.Tarefas;
import com.rh.testerh.service.TarefasService;
import com.rh.testerh.ui.view.dialog.DialogPrincipalAdd;
import com.rh.testerh.ui.view.dialog.DialogPrincipalDelete;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rh.testerh.ui.utils.ComponentsUtils.*;

@PageTitle(Dictionary.TAREFAS)
@Route(value = Dictionary.TAREFAS)
@RouteAlias(value = "")
public class PrincipalView extends VerticalLayout {

    private final TarefasService tarefasService;

    public PrincipalView(TarefasService tarefasService) {
        this.tarefasService = tarefasService;
        createGrid();
    }

    public void createForm(Grid<Tarefas> grid, Binder<Tarefas> binder, Tarefas tarefa) {

        TextField nameField = new TextField();
        createFormOnGrid(Tarefas::getNome, Tarefas::setNome, grid, nameField, Dictionary.NOME, binder);

        BigDecimalField custoField = new BigDecimalField();
        createFormOnGrid(Tarefas::getCusto, Tarefas::setCusto, grid, custoField, Dictionary.CUSTO, binder);

        DatePicker dtLimiteField = new DatePicker();
        createFormOnGrid(Tarefas::getDtLimite, Tarefas::setDtLimite, grid, dtLimiteField, Dictionary.DATA_LIMITE, binder);

        binder.setBean(tarefa);
    }

    public void createGrid() {
        Grid<Tarefas> grid = new Grid<>();
        grid.setHeight("90vh");
        Editor<Tarefas> editor = grid.getEditor();
        Tarefas tarefa = new Tarefas();
        Binder<Tarefas> binder = new Binder<>(Tarefas.class);

        editor.setBinder(binder);
        editor.setBuffered(true);

        grid.setPartNameGenerator(e -> {
            if (e.getCusto().doubleValue() >= 1000.00) {
                return "more-than-1000";
            }
            return null;
        });

        createGridColumn(grid, Tarefas::getNome, Dictionary.NOME);
        createGridColumn(grid, Tarefas::getCusto, Dictionary.CUSTO);
        createGridColumn(grid, Tarefas::getDtLimite, Dictionary.DATA_LIMITE);
        Grid.Column<Tarefas> editColumn = grid.addComponentColumn(event -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(event);
            });


            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> new DialogPrincipalDelete(tarefasService, Dictionary.EXCLUIR_TAREFA, event.getId(), grid).open());
            return new HorizontalLayout(editButton, deleteButton);
        });

        grid.addComponentColumn(event -> {
            ListDataProvider<Tarefas> dataProvider = (ListDataProvider<Tarefas>) grid.getDataProvider();

            Button moveUpButton = new Button(new Icon(VaadinIcon.ARROW_UP));
            moveUpButton.addClickListener(e -> moveItemUp(grid, event, dataProvider));
            Button moveDownButton = new Button(new Icon(VaadinIcon.ARROW_DOWN));
            moveDownButton.addClickListener(e -> moveItemDown(grid, event, dataProvider));

            int currentIndex = 0;
            List<Tarefas> items = dataProvider.getItems().stream().toList();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(event)) {
                    currentIndex = i;
                    break;
                }
            }

            moveUpButton.setEnabled(currentIndex > 0);
            moveDownButton.setEnabled(currentIndex < items.size() - 1);

            return new HorizontalLayout(moveUpButton, moveDownButton);
        });


        editColumn.setEditorComponent(createButtonsEditGrid(editor, binder, grid));
        createAddButton(grid);
        createForm(grid, binder, tarefa);
        grid.setItems(tarefasService.findAllByOrderByOrdemApresentacao());

        add(grid);
    }

    private void moveItemUp(Grid<Tarefas> grid, Tarefas item, ListDataProvider<Tarefas> dataProvider) {
        List<Tarefas> items = new ArrayList<>(dataProvider.getItems());
        int currentIndex = items.indexOf(item);

        if (currentIndex > 0) {
            Collections.swap(items, currentIndex, currentIndex - 1);

            updateOrderInDatabase(items);

            dataProvider.getItems().clear();
            dataProvider.getItems().addAll(items);
            grid.getDataProvider().refreshAll();
        }
    }

    private void moveItemDown(Grid<Tarefas> grid, Tarefas item, ListDataProvider<Tarefas> dataProvider) {
        List<Tarefas> items = new ArrayList<>(dataProvider.getItems());
        int currentIndex = items.indexOf(item);

        if (currentIndex < items.size() - 1) {
            Collections.swap(items, currentIndex, currentIndex + 1);

            updateOrderInDatabase(items);

            dataProvider.getItems().clear();
            dataProvider.getItems().addAll(items);
            grid.getDataProvider().refreshAll();
        }
    }

    private void updateOrderInDatabase(List<Tarefas> items) {
        int ordem = 1;
        for (Tarefas tarefa : items) {
            tarefa.setOrdemApresentacao(ordem++);
            tarefasService.updateOrdem(tarefa.id, tarefa.getOrdemApresentacao());
        }
    }

    public Component createButtonsEditGrid(Editor<Tarefas> editor, Binder<Tarefas> binder, Grid<Tarefas> grid) {
        Button saveButton = new Button(Dictionary.SALVAR);
        saveButton.addClickListener(e -> {
            Notification notification = new Notification();
            notification.setDuration(3000);
            if (binder.isValid()) {
                if (!tarefasService.existsByNome(binder.getBean().getNome())) {
                    tarefasService.updateTarefa(editor.getItem().getId(), binder.getBean().getNome(), binder.getBean().getDtLimite(), binder.getBean().getCusto());
                    notification = Notification.show(Dictionary.ATUALIZADO_COM_SUCESSO, 10000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    grid.setItems(tarefasService.findAllByOrderByOrdemApresentacao());
                } else {
                    notification.setText(Dictionary.NOME_JA_EXISTENTE);
                    notification = Notification.show(Dictionary.NOME_JA_EXISTENTE, 10000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                notification = Notification.show(Dictionary.PREENCHIMENTO_OBRIGATORIO, 10000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.setDisableOnClick(!binder.isValid());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create());
        cancelButton.addClickListener(e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return new HorizontalLayout(saveButton, cancelButton);
    }

    public void createAddButton(Grid<Tarefas> grid) {
        Button addButton = new Button(Dictionary.ADICIONAR, new Icon(VaadinIcon.PLUS));
        addButton.addClickListener(event -> new DialogPrincipalAdd(tarefasService, Dictionary.ADICIONAR_TAREFA, grid).open());
        add(addButton);
    }

}

