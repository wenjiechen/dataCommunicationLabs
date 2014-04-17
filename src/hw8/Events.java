package hw8;

import Fsm.Event;

class PASSIVE extends Event {
  public PASSIVE(String name) {
    super(name);
  }
}

class ACTIVE extends Event {
  public ACTIVE(String name) {
    super(name);
  }
}

class SYN extends Event {
  public SYN(String name) {
    super(name);
  }
}

class SYNACK extends Event {
  public SYNACK(String name) {
    super(name);
  }
}

class RDATA extends Event {
  public RDATA(String name) {
    super(name);
  }
}

class SDATA extends Event {
  public SDATA(String name) {
    super(name);
  }
}

class FIN extends Event {
  public FIN(String name) {
    super(name);
  }
}

class CLOSE extends Event {
  public CLOSE(String name) {
    super(name);
  }
}

class TIMEOUT extends Event {
  public TIMEOUT(String name) {
    super(name);
  }
}

class ACK extends Event {
  public ACK(String name) {
    super(name);
  }
}

class BADEVENT extends Event {
  public BADEVENT(String name) {
    super(name);
  }
}