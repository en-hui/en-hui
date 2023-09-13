package com.enhui.fn;

import org.apache.beam.sdk.transforms.DoFn;

public class PrintTextFn extends DoFn<String, Void> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        String text = c.element();
        System.out.println(text);
    }
}





