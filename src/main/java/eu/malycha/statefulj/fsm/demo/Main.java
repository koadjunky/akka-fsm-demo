package eu.malycha.statefulj.fsm.demo;

import java.util.LinkedList;
import java.util.List;

import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.StateActionPair;
import org.statefulj.fsm.model.Transition;
import org.statefulj.fsm.model.impl.StateActionPairImpl;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

public class Main {

    // Events
    static String eventA = "Event A";
    static String eventB = "Event B";

    // States
    static State<Foo> stateA = new StateImpl<>("State A");
    static State<Foo> stateB = new StateImpl<>("State B");
    static State<Foo> stateC = new StateImpl<>("State C", true); // End State

    // Actions
    //
    static final Action<Foo> actionA = new HelloAction<>("World");
    static final Action<Foo> actionB = new HelloAction<>("Folks");

    public static void main(String[] args) throws TooBusyException {
        stateA.addTransition(eventA, stateB, actionA);
        stateB.addTransition(eventB, stateC, actionB);
        stateB.addTransition(eventA, new Transition<Foo>() {

            @Override
            public StateActionPair<Foo> getStateActionPair(Foo stateful, String event, Object... args) throws RetryException {
                State<Foo> next;
                if (stateful.isBar()) {
                    next = stateB;
                } else {
                    next = stateC;
                }

                // Move to the next state without taking any action
                //
                return new StateActionPairImpl<>(next, null);
            }
        });

        List<State<Foo>> states = new LinkedList<>();
        states.add(stateA);
        states.add(stateB);
        states.add(stateC);

        MemoryPersisterImpl<Foo> persister =
            new MemoryPersisterImpl<>(
                states,   // Set of States
                stateA);  // Start State

        // FSM
        //
        FSM<Foo> fsm = new FSM<>("Foo FSM", persister);

        Foo foo = new Foo();

        fsm.onEvent(foo, eventA);
        foo.setBar(true);
        fsm.onEvent(foo, eventA);
        foo.setBar(false);
        fsm.onEvent(foo, eventA);
    }
}
