package eu.malycha.statefulj.fsm.demo;

import org.statefulj.persistence.annotations.State;

// Stateful Entity
//
public class Foo {

    @State
    String state;   // Memory Persister requires a String

    boolean bar;

    public String getState() {
        return state;
    }

    // Note: there is no setter for the state field
    //       as the value is set by StatefulJ

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isBar() {
        return bar;
    }

}
