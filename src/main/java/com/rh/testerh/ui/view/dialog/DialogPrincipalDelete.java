package com.rh.testerh.ui.view.dialog;

import com.rh.testerh.Dictionary;
import com.rh.testerh.model.Tarefas;
import com.rh.testerh.service.TarefasService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;

import java.util.UUID;

public class DialogPrincipalDelete extends Dialog {
    private final TarefasService tarefasService;

    public DialogPrincipalDelete(TarefasService tarefasService, String title, UUID id, Grid<Tarefas> grid) {
        this.tarefasService = tarefasService;
        createHeader(title);
        createBody();
        createFooter(id, grid);
    }

    public void createHeader(String title) {
        setHeaderTitle(title);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    public void createFooter(UUID id, Grid<Tarefas> grid) {
        Button deleteButton = new Button(Dictionary.EXCLUIR, e -> {
            tarefasService.deleteById(id);
            Notification.show(Dictionary.EXCLUIDO_COM_SUCESSO, 10000, Notification.Position.TOP_CENTER);
            grid.setItems(tarefasService.findAllByOrderByOrdemApresentacao());
            close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button cancelButton = new Button(Dictionary.CANCELAR, e -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        getFooter().add(deleteButton, cancelButton);
    }

    public void createBody() {
        add(new Text("Você deseja realmente excluir esta tarefa?"));
    }
}
