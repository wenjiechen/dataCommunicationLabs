package hw8;

import Fsm.State;

class CLOSED extends State {

  public CLOSED(String name) {
    super(name);
  }
}

class LISTEN extends State {

  public LISTEN(String name) {
    super(name);
  }
}

class SYN_RCVD extends State {

  public SYN_RCVD(String name) {
    super(name);
  }
}

class SYN_SENT extends State {

  public SYN_SENT(String name) {
    super(name);
  }
}

class ESTABLISHED extends State {

  public ESTABLISHED(String name) {
    super(name);
  }
}

class FIN_WAIT_1 extends State {

  public FIN_WAIT_1(String name) {
    super(name);
  }
}

class FIN_WAIT_2 extends State {

  public FIN_WAIT_2(String name) {
    super(name);
  }
}

class CLOSING extends State {

  public CLOSING(String name) {
    super(name);
  }
}

class TIME_WAIT extends State {

  public TIME_WAIT(String name) {
    super(name);
  }
}

class EMPTY extends State {

  public EMPTY(String name) {
    super(name);
  }
}

class LAST_ACK extends State {

  public LAST_ACK(String name) {
    super(name);
  }
}
