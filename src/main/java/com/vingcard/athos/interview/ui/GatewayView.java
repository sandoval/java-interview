package com.vingcard.athos.interview.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vingcard.athos.interview.persistence.entity.Gateway;
import com.vingcard.athos.interview.persistence.repository.GatewayRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("gateways")
@PermitAll
public class GatewayView extends VerticalLayout {
    private final GatewayRepository gatewayRepository;
    private final Grid<Gateway> grid = new Grid<>(Gateway.class, false);
    private final Binder<Gateway> binder = new Binder<>(Gateway.class);
    private Gateway editingGateway;

    @Autowired
    public GatewayView(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
        add(new HorizontalLayout(
            new RouterLink("Home", HomeView.class),
            new RouterLink("Locks", LockView.class)
        ));
        grid.addColumn(Gateway::getSerial).setHeader("Serial");
        grid.addColumn(Gateway::getMacAddress).setHeader("MAC Address");
        grid.addColumn(Gateway::isOnline).setHeader("Online");
        grid.addColumn(Gateway::getVersion).setHeader("Version");
        grid.setItems(gatewayRepository.findAll());
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editGateway(event.getValue());
            }
        });
        add(grid);
        addGatewayForm();
    }

    private void addGatewayForm() {
        TextField serial = new TextField("Serial");
        serial.setMaxLength(16);
        TextField macAddress = new TextField("MAC Address");
        TextField version = new TextField("Version");
        version.setMaxLength(20);
        Button save = new Button("Save");
        Button delete = new Button("Delete");
        Button clear = new Button("Clear");

        add(serial, macAddress, version, save, delete, clear);

        binder.forField(serial).bind(Gateway::getSerial, Gateway::setSerial);
        binder.forField(macAddress).bind(Gateway::getMacAddress, Gateway::setMacAddress);
        binder.forField(version).bind(Gateway::getVersion, Gateway::setVersion);

        save.addClickListener(e -> {
            Gateway gateway = editingGateway != null ? editingGateway : new Gateway();
            binder.writeBeanIfValid(gateway);
            if (editingGateway == null) {
                gateway.setOnline(false);
            }
            gatewayRepository.save(gateway);
            grid.setItems(gatewayRepository.findAll());
            editingGateway = null;
            binder.readBean(null);
        });
        delete.addClickListener(e -> {
            if (editingGateway != null) {
                gatewayRepository.delete(editingGateway);
                grid.setItems(gatewayRepository.findAll());
                editingGateway = null;
                binder.readBean(null);
            }
        });
        clear.addClickListener(e -> {
            editingGateway = null;
            binder.readBean(null);
        });
    }

    private void editGateway(Gateway gateway) {
        editingGateway = gateway;
        binder.readBean(gateway);
    }
}
