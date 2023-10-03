package com.rh.testerh.ui.view.dialog;

import com.rh.testerh.Dictionary;
import com.rh.testerh.model.Tarefas;
import com.rh.testerh.service.TarefasService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import static com.rh.testerh.ui.utils.ComponentsUtils.setBinder;

public class DialogPrincipalAdd extends Dialog {
    private final TarefasService tarefasService;
    public DialogPrincipalAdd(TarefasService tarefasService,String title, Grid<Tarefas> grid) {
        this.tarefasService = tarefasService;

        Tarefas tarefas = new Tarefas();

        Binder<Tarefas> binder = new Binder<>(Tarefas.class);

        createHeader(title);
        createForm(binder, tarefas);
        createFooter(binder, grid);
    }

    public void createHeader(String title) {
        setHeaderTitle(title);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    public void createFooter(Binder<Tarefas> binder, Grid<Tarefas> grid) {
        Button saveButton = new Button(Dictionary.SALVAR, e -> {
            Notification notification;
            if (binder.validate().isOk()) {
                Tarefas tarefa = binder.getBean();

                if (!tarefasService.existsByNome(tarefa.getNome())) {
                    tarefasService.save(tarefa);
                    notification = Notification.show(Dictionary.ADICIONADO_COM_SUCESSO, 10000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    grid.setItems(tarefasService.findAllByOrderByOrdemApresentacao());
                    close();
                } else {
                    notification = Notification.show(Dictionary.NOME_JA_EXISTENTE, 10000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                notification = Notification.show(Dictionary.PREENCHIMENTO_OBRIGATORIO, 10000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button cancelButton = new Button(Dictionary.CANCELAR, e -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        getFooter().add(saveButton, cancelButton);
    }

    public void createForm(Binder<Tarefas> binder, Tarefas tarefa) {

        TextField nameField = new TextField(Dictionary.NOME);
        setBinder(nameField, Tarefas::getNome, Tarefas::setNome, binder);

        BigDecimalField custoField = new BigDecimalField(Dictionary.CUSTO);
        setBinder(custoField, Tarefas::getCusto, Tarefas::setCusto, binder);
        custoField.getStyle().set("padding",  "5px");
        DatePicker dtLimiteField = new DatePicker(Dictionary.DATA_LIMITE);
        setBinder(dtLimiteField, Tarefas::getDtLimite, Tarefas::setDtLimite, binder);

        binder.setBean(tarefa);
        add(nameField, custoField, dtLimiteField);
    }
}
