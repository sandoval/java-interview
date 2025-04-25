package com.vingcard.athos.interview.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vingcard.athos.interview.persistence.entity.Lock;
import com.vingcard.athos.interview.persistence.repository.LockRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("locks")
@PermitAll
public class LockView extends VerticalLayout {
    private final LockRepository lockRepository;
    private final Grid<Lock> grid = new Grid<>(Lock.class, false);
    private final Binder<Lock> binder = new Binder<>(Lock.class);
    private Lock editingLock;

    @Autowired
    public LockView(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
        add(new HorizontalLayout(
            new RouterLink("Home", HomeView.class),
            new RouterLink("Gateways", GatewayView.class)
        ));
        grid.addColumn(Lock::getSerial).setHeader("Serial");
        grid.addColumn(Lock::getName).setHeader("Name");
        grid.addColumn(Lock::getMacAddress).setHeader("MAC Address");
        grid.addColumn(Lock::isOnline).setHeader("Online");
        grid.addColumn(Lock::getVersion).setHeader("Version");
        grid.setItems(lockRepository.findAll());
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editLock(event.getValue());
            }
        });
        add(grid);
        addLockForm();
    }

    private void addLockForm() {
        TextField serial = new TextField("Serial");
        serial.setMaxLength(16);
        TextField name = new TextField("Name");
        name.setMaxLength(50);
        TextField macAddress = new TextField("MAC Address");
        TextField version = new TextField("Version");
        version.setMaxLength(20);
        Button save = new Button("Save");
        Button delete = new Button("Delete");
        Button clear = new Button("Clear");

        add(serial, name, macAddress, version, save, delete, clear);

        binder.forField(serial).bind(Lock::getSerial, Lock::setSerial);
        binder.forField(name).bind(Lock::getName, Lock::setName);
        binder.forField(macAddress).bind(Lock::getMacAddress, Lock::setMacAddress);
        binder.forField(version).bind(Lock::getVersion, Lock::setVersion);

        save.addClickListener(e -> {
            Lock lock = editingLock != null ? editingLock : new Lock();
            binder.writeBeanIfValid(lock);
            if (editingLock == null) {
                lock.setOnline(false);
            }
            lockRepository.save(lock);
            grid.setItems(lockRepository.findAll());
            editingLock = null;
            binder.readBean(null);
        });
        delete.addClickListener(e -> {
            if (editingLock != null) {
                lockRepository.delete(editingLock);
                grid.setItems(lockRepository.findAll());
                editingLock = null;
                binder.readBean(null);
            }
        });
        clear.addClickListener(e -> {
            editingLock = null;
            binder.readBean(null);
        });
    }

    private void editLock(Lock lock) {
        editingLock = lock;
        binder.readBean(lock);
    }
}
