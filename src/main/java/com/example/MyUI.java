package com.example;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

/**
 * Created by yl on 27.08.17.
 */

@Theme("valo")
@SpringUI
@Push
public class MyUI extends UI {

    private Label pushLabel;
    private Slider slider;
    private Button startBtn;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        startBtn = new Button("Start");
        slider = new Slider();
        slider.setMin(0.0);
        slider.setMax(10.0);
        slider.setValue(0.0);
        slider.setWidth("300px");
        pushLabel = new Label("show slider value");
        layout.addComponent(startBtn);
        layout.addComponent(slider);
        layout.addComponent(pushLabel);
        startBtn.addClickListener(event->{
            new sliderThread().start();
        });

        slider.addValueChangeListener(event->{
            new labelThread().start();
        });
        setContent(layout);
    }

    class sliderThread extends Thread {
        @Override
        public void run() {
            try {
                startBtn.setEnabled(false);
                double currentValue = 0;
                while(slider.getValue() < slider.getMax()) {
                    Thread.sleep(500);
                    access(new Runnable() {
                        @Override
                        public void run() {
                            slider.setValue(slider.getValue() + 1.0);
                        }
                    });
                }

                access(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        slider.setValue(slider.getMin());
                    }
                });


                startBtn.setEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //log exception if needed.
            }
        }
    }

    class labelThread extends Thread {
        @Override
        public void run() {
            access(new Runnable() {
                @Override
                public void run() {
                    pushLabel.setValue(slider.getValue().toString());
                }
            });
        }
    }

}
