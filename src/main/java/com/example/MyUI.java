package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yl on 27.08.17.
 */

@Theme("valo")
@SpringUI
@Push
public class MyUI extends UI {

    private Label pushLabel;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        Button startBtn = new Button("Start");
        pushLabel = new Label("show current time");
        layout.addComponent(startBtn);
        layout.addComponent(pushLabel);
        startBtn.addClickListener(event->{
            new TimeThread().start();
        });
        setContent(layout);
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                while (true) {
                    Thread.sleep(1000);
                    access(new Runnable() {
                        @Override
                        public void run() {
                            Date date = new Date();
                            pushLabel.setValue(dateFormat.format(date));
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //log exception if needed.
            }
        }
    }
}
