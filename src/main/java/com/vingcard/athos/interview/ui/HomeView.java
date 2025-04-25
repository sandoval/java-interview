package com.vingcard.athos.interview.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.html.H1;

@Route("")
public class HomeView extends VerticalLayout {
    public HomeView() {
        add(new H1("Welcome to the Interview App"));
        add(new RouterLink("Gateways", GatewayView.class));
        add(new RouterLink("Locks", LockView.class));
    }
}
