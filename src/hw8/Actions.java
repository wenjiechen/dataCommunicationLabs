package hw8;

import Fsm.Action;
import Fsm.Event;
import Fsm.FSM;

class syn_action extends Action {

  @Override
  public void execute(FSM fsm, Event event) {

  }
}

class syn_ack extends Action {

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("Event " + event + " received, current State is "
        + fsm.currentState());
  }
}

class ack_action extends Action {

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("Event " + event + " received, current State is "
        + fsm.currentState());
  }
}

class fin_action extends Action {

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("Event " + event + " received, current State is "
        + fsm.currentState());
  }
}

class rdatan extends Action {
  private int num = 0;

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("DATA received " + (++num));
  }
}

class sdatan extends Action {
  private int num = 0;

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("DATA sent " + (++num));
  }
}

class noAction extends Action {

  @Override
  public void execute(FSM fsm, Event event) {
    System.out.println("Event " + event + " received, current State is "
        + fsm.currentState());
  }
}